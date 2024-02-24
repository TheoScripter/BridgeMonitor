#ifndef __USER_SERVICES_H__
#define __USER_SERVICES_H__

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "bluenrg_def.h"
#include "gatt_db.h"
#include "lsm6dsl.h"
#include "cmsis_os2.h"
#include "bluenrg_conf.h"
#include "bluenrg_gatt_aci.h"

tBleStatus Add_Vibration_Service(void);
tBleStatus BlueMS_Environmental_Update(int32_t press, int16_t temp);
tBleStatus Accel_Gyro_Update(uint16_t servHandle, uint16_t charHandle);
tBleStatus Quat_Update(AxesRaw_t *q_axes);
void accelGyroDataRDYCB(void);
int get_accel_gyro_buffer(LSM6DSL_AxesRaw_t *accel_axes, LSM6DSL_AxesRaw_t *gyro_axes);
int start_accel_gyro(void);

#endif
