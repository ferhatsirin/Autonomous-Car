#include "BluetoothCom.h"

static UART_HandleTypeDef* blUart;

void initBluetooth(UART_HandleTypeDef* huart,uint8_t* data, uint8_t length){
	blUart =huart;
	HAL_UART_Receive_DMA(blUart, data, length);
}


void sendDataDMA(uint8_t * data,int length){
	HAL_UART_Transmit_DMA(blUart,data, length);
}

void sendData(uint8_t *data, int length){
	HAL_UART_Transmit(blUart, data, length, 100);
}

