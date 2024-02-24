#include "lsm6dsl_reg.h"
#include "lsm6dsl.h"
#include "b_l475e_iot01a2_bus.h"
#include "user_services.h"
#include "cmsis_os2.h"

// sourced from gatt_db.c
/** @brief Macro that stores Value into a buffer in Little Endian Format (2 bytes)*/
#define HOST_TO_LE_16(buf, val)    ( ((buf)[0] =  (uint8_t) (val)    ) , \
                                   ((buf)[1] =  (uint8_t) (val>>8) ) )

/** @brief Macro that stores Value into a buffer in Little Endian Format (4 bytes) */
#define HOST_TO_LE_32(buf, val)    ( ((buf)[0] =  (uint8_t) (val)     ) , \
                                   ((buf)[1] =  (uint8_t) (val>>8)  ) , \
                                   ((buf)[2] =  (uint8_t) (val>>16) ) , \
                                   ((buf)[3] =  (uint8_t) (val>>24) ) )

static struct _sensor_data {
  LSM6DSL_AxesRaw_t accel_axes;
  LSM6DSL_AxesRaw_t gyro_axes;
  osThreadId_t tid;
  osThreadAttr_t tattrs;
  osMutexId_t lock;
  osMutexAttr_t lock_attrs;
} _sensor_data_handle = {
  .lock_attrs = {
    .name = "accel_gyro_lock",
    .attr_bits = osMutexRecursive | osMutexPrioInherit,
    .cb_mem = NULL,
    .cb_size = 0
  },
  .tattrs = {
    .name = "_accel_gyro_thread",
    .stack_size = 128 * 8,
    .priority = (osPriority_t) osPriorityHigh,
  }
};

static LSM6DSL_Object_t _accelGyro;
static volatile uint32_t dataRdyIntReceived;

void accelGyroDataRDYCB(void){
  dataRdyIntReceived++;
}

static void _start_drivers(void) {
  dataRdyIntReceived = 0;
  LSM6DSL_IO_t io_ctx;
  uint8_t id;
  LSM6DSL_AxesRaw_t axes;

  /* Link I2C functions to the LSM6DSL driver */
  io_ctx.BusType     = LSM6DSL_I2C_BUS;
  io_ctx.Address     = LSM6DSL_I2C_ADD_L;
  io_ctx.Init        = BSP_I2C2_Init;
  io_ctx.DeInit      = BSP_I2C2_DeInit;
  io_ctx.ReadReg     = BSP_I2C2_ReadReg;
  io_ctx.WriteReg    = BSP_I2C2_WriteReg;
  io_ctx.GetTick     = BSP_GetTick;
  LSM6DSL_RegisterBusIO(&_accelGyro, &io_ctx);
  printf("Initializing IMU\n");

  /* Read the LSM6DSL WHO_AM_I register */
  LSM6DSL_ReadID(&_accelGyro, &id);
  if (id != LSM6DSL_ID) {
    printf("Wrong sensor ID D:\n");
  }

  /* Initialize the LSM6DSL sensor */
  LSM6DSL_Init(&_accelGyro);

  /* Configure the LSM6DSL accelerometer (ODR, scale and interrupt) */
  LSM6DSL_ACC_SetOutputDataRate(&_accelGyro, LSM6DSL_XL_ODR_12Hz5); /* 12.5 Hz */
  LSM6DSL_ACC_SetFullScale(&_accelGyro, 4);          /* [-4000mg; +4000mg] */
  LSM6DSL_ACC_Set_INT1_DRDY(&_accelGyro, ENABLE);    /* Enable DRDY */
  LSM6DSL_ACC_GetAxesRaw(&_accelGyro, &axes);        /* Clear DRDY */

  // Also configure gyroscope :)
  LSM6DSL_GYRO_SetOutputDataRate(&_accelGyro, LSM6DSL_GY_ODR_12Hz5);
  LSM6DSL_GYRO_SetFullScale(&_accelGyro, LSM6DSL_2000dps);          /* [-4000mg; +4000mg] */
  LSM6DSL_GYRO_Set_INT1_DRDY(&_accelGyro, ENABLE);    /* Enable DRDY */
  LSM6DSL_GYRO_GetAxesRaw(&_accelGyro, &axes);        /* Clear DRDY */

  /* Start the LSM6DSL accelerometer and gyroscope */
  LSM6DSL_ACC_Enable(&_accelGyro);
  LSM6DSL_GYRO_Enable(&_accelGyro);
  _sensor_data_handle.lock = osMutexNew(&_sensor_data_handle.lock_attrs);
}

static osStatus_t _sensor_lock(uint32_t timeout){
  return osMutexAcquire(_sensor_data_handle.lock, timeout);
}

static osStatus_t _sensor_release(void){
  return osMutexRelease(_sensor_data_handle.lock);
}

static int _read_accel(void){
  _sensor_lock(osWaitForever);
  LSM6DSL_ACC_GetAxesRaw(&_accelGyro, &_sensor_data_handle.accel_axes);
  _sensor_release();
  return 0;
}

static int _read_gyro(void){
  _sensor_lock(osWaitForever);
  LSM6DSL_GYRO_GetAxesRaw(&_accelGyro, &_sensor_data_handle.gyro_axes);
  _sensor_release();
  return 0;
}

static int _update_gyro_accel(void){
  _read_accel();
  _read_gyro();
  return 0;
}

static void _update_accel_gyro_thread_cb(void *argument){
  _start_drivers();
  for(;;){
    if (dataRdyIntReceived != 0) {
      dataRdyIntReceived = 0;
      _update_gyro_accel();
      printf("Accel data received!\r\n");
      printf("% 5d, % 5d, % 5d\r\n",  (int) _sensor_data_handle.accel_axes.x, (int) _sensor_data_handle.accel_axes.y, (int) _sensor_data_handle.accel_axes.z);

      printf("Gyro data received!\r\n");
      printf("% 5d, % 5d, % 5d\r\n",  (int) _sensor_data_handle.gyro_axes.x, (int) _sensor_data_handle.gyro_axes.y, (int) _sensor_data_handle.gyro_axes.z);
    }
    osThreadYield();
  }
}

int get_accel_gyro_buffer(LSM6DSL_AxesRaw_t *accel_axes, LSM6DSL_AxesRaw_t *gyro_axes){
  _sensor_lock(osWaitForever);
  memcpy(accel_axes, &_sensor_data_handle.accel_axes, sizeof(_sensor_data_handle.accel_axes));
  memcpy(gyro_axes, &_sensor_data_handle.gyro_axes, sizeof(_sensor_data_handle.gyro_axes));
  _sensor_release();
  return 0;
}


int start_accel_gyro(void){
  _sensor_data_handle.tid = osThreadNew(_update_accel_gyro_thread_cb, NULL, &_sensor_data_handle.tattrs);
  if (_sensor_data_handle.tid != 0) return 0;
  printf("Failed to start accel_gyro thread!\n");
  return -1;
}

