/*
 * FlameSensor.h
 *
 *  Created on: Mar 27, 2020
 *      Author: ferhat
 */

#ifndef INC_FLAMESENSOR_H_
#define INC_FLAMESENSOR_H_

#include "stm32f4xx_hal.h"

// initialize flame sensor with pins
void initFlameSensor(GPIO_TypeDef* pinT,uint16_t pinN);

//read the flame sensor and sends its data
void readFlameSensor();


#endif /* INC_FLAMESENSOR_H_ */
