#include "common_data.h"
#include "cmsis_os2.h"

common_data_t common_data;
#define _COMMON_DATA_SEMAPHORE_LIMIT 1
osSemaphoreId_t _common_data_semaphore;
osSemaphoreAttr_t _common_data_semaphore_attrs = {
  .name = "_common_data_semaphore"
};


void init_common_data(void){
  _common_data_semaphore = osSemaphoreNew(_COMMON_DATA_SEMAPHORE_LIMIT, 0, &_common_data_semaphore_attrs);
}
void set_common_data(common_data_t *src){
}
void get_common_data(common_data_t *dst);
