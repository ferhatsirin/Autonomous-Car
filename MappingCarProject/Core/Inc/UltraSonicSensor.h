/*
 * UltraSonicSensor.h
 *
 *  Created on: Mar 24, 2020
 *      Author: ferhat
 */

#ifndef INC_ULTRASONICSENSOR_H_
#define INC_ULTRASONICSENSOR_H_

#include "stm32f4xx_hal.h"

void initUltraSonicSensor(GPIO_TypeDef* sensorLT,uint16_t trigL, GPIO_TypeDef* sensorLE,uint16_t echoL,
		GPIO_TypeDef* sensorCT, uint16_t trigC,GPIO_TypeDef* sensorCE, uint16_t echoC,GPIO_TypeDef* sensorRT,
		uint16_t trigR,GPIO_TypeDef* sensorRE, uint16_t echoR,TIM_HandleTypeDef* tim);


uint8_t readSensor(GPIO_TypeDef* sensorT,uint16_t trig,GPIO_TypeDef* sensorE,uint16_t echo);
uint8_t measureLeftDistance();
uint8_t measureCenterDistance();
uint8_t measureRightDistance();
void measureDistances(uint8_t cancel);

#endif /* INC_ULTRASONICSENSOR_H_ */
