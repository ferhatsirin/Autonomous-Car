/*
 * BluetoothCom.h
 *
 *  Created on: Mar 14, 2020
 *      Author: ferhat
 */

#ifndef INC_BLUETOOTHCOM_H_
#define INC_BLUETOOTHCOM_H_

#include "stm32f4xx_hal.h"

void initBluetooth(UART_HandleTypeDef* huart,uint8_t* data, uint8_t length);
void sendData(uint8_t * data,int length);
void sendDataDMA(uint8_t * data,int length);

#endif /* INC_BLUETOOTHCOM_H_ */
