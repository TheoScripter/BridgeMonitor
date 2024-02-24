#ifndef __DEBUG_H__
#define __DEBUG_H__
#ifdef DEBUG
  #define BREAK_1 __asm("bkpt #1")
  #define BREAK_2 __asm("bkpt #2")
#else
  #define BREAK_1 /* empty */
  #define BREAK_2 /* empty */
#endif


int test1(void) {
  BREAK_1; /* hit breakpoint and move PC to next line */
  return 1;
}

int test2(void) {
  BREAK_2; /* hit breakpoint and move PC to next line */
  return 1;
}
#endif
