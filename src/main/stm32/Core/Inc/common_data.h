#ifndef __COMMON_DATA__
#define __COMMON_DATA__

#include "lsm6dsl.h"

typedef struct common_data {
  LSM6DSL_AxesRaw_t gyro;
  LSM6DSL_AxesRaw_t accel;
} common_data_t;

extern common_data_t common_data;

void init_common_data(void);
void set_common_data(common_data_t *src);
void get_common_data(common_data_t *dst);

#endif // !__COMMON_DATA__
