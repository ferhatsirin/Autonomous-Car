/*
 * DelayMS.h
 *
 *  Created on: Mar 24, 2020
 *      Author: ferhat
 */

#ifndef INC_DELAYMS_H_
#define INC_DELAYMS_H_
#include "stm32f4xx_hal.h"

// initialize timer
void initDelayMS(TIM_HandleTypeDef* tim);

// delay in microsecond
void delay_us(uint16_t delay);

// set timer counter
void setCounter(uint32_t cnt);

// get timer counter
uint32_t getCounter();


#endif /* INC_DELAYMS_H_ */
