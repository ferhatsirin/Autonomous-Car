/*
 * MotorDriver.h
 *
 *  Created on: Mar 14, 2020
 *      Author: ferhat
 */

#ifndef INC_MOTORDRIVER_H_
#define INC_MOTORDRIVER_H_

#include "stm32f4xx_hal.h"

void initMotorDriver(GPIO_TypeDef* pinT,uint16_t l1,uint16_t l2,uint16_t r1,uint16_t r2,TIM_HandleTypeDef* tim);
void goForward(uint16_t unit);
void turnCar(uint16_t degree);
void resetMotorPins();
void setMotorValue(short val);
short getMotorValue();
void motorGoForwardReturn();
void motorTimerCallback();
void sendMotorMissionEnded();

#endif /* INC_MOTORDRIVER_H_ */
