/*
 * Magnetometer.h
 *
 *  Created on: Mar 16, 2020
 *      Author: ferhat
 */

#ifndef INC_MAGNETOMETER_H_
#define INC_MAGNETOMETER_H_

#include "stm32f4xx_hal.h"

// initialize magnetometer sensor to be used with provided pins
void initMagnetometer(I2C_HandleTypeDef* i2c);

// read compass degree and send data
uint16_t readCompass(uint8_t motorDriver);

// get degree from magnetometer data
uint16_t getDegree();

// read magnetometer register
void readMagnetometerReg();


// reset the sensor
//void hardResetMagnetometer();


void softResetMagnetometer();

// initialize register of sensor
void initializeRegisters();


#endif /* INC_MAGNETOMETER_H_ */
