/*
 * Temperature.h
 *
 *  Created on: Mar 21, 2020
 *      Author: ferhat
 */

#ifndef INC_TEMPERATURE_H_
#define INC_TEMPERATURE_H_

#include "stm32f4xx_hal.h"

//initialize temperature sensor to be used with provided pins
void initTemperatureSensor(GPIO_TypeDef* pinT,uint16_t pinN,TIM_HandleTypeDef* tim);

// set pin type as output
void setPinOutput();

// set pin type as input
void setPinInput();

// initialize sensor with reset pulse
uint8_t sendResetPulse();

// set sensor mode for 12 bit
void setSensorMode();

// write operation to temp sensor
void writeTempSensor(uint8_t data);

// read operation from temp sensor
uint8_t readTempSensor();

// measure temperature from sensor
void measureTemperature();

//send temperature value
void sendTemperatureVal();


#endif /* INC_TEMPERATURE_H_ */
