#include "Temperature.h"
#include "BluetoothCom.h"
#include "DelayMS.h"

static GPIO_TypeDef* pinType;
static uint16_t pin;
static GPIO_InitTypeDef GPIO_InitStruct = {0};
static uint8_t tempData[3];
static TIM_HandleTypeDef* timer;

//initialize sensor to be used with provided pins
void initTemperatureSensor(GPIO_TypeDef* pinT,uint16_t pinN,TIM_HandleTypeDef* tim){
	pinType =pinT;
	pin =pinN;
	timer =tim;

	GPIO_InitStruct.Pin = pin;
	GPIO_InitStruct.Pull = GPIO_PULLUP;
	GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_VERY_HIGH;

	setSensorMode();  // set 12 bit mode
}

// set pin type as output
void setPinOutput(){

	GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;

	HAL_GPIO_Init(pinType, &GPIO_InitStruct);
}


// set pin type as input
void setPinInput(){

	GPIO_InitStruct.Mode = GPIO_MODE_INPUT;

	HAL_GPIO_Init(pinType, &GPIO_InitStruct);
}

// initialize sensor with reset pulse
uint8_t sendResetPulse(){
	uint8_t ret;

	setPinOutput();

	HAL_GPIO_WritePin(pinType, pin, GPIO_PIN_RESET);

	delay_us(480);

	setPinInput();

	delay_us(80);

	if(HAL_GPIO_ReadPin(pinType, pin) == GPIO_PIN_RESET){
		ret = 1;
	}
	else{
		ret = 0;
	}

	delay_us(400);

	return ret;

}

// set sensor mode for 12 bit
void setSensorMode(){

	sendResetPulse();
	writeTempSensor(0xcc);  //skip sensor
	writeTempSensor(0x4e);  // write command

	writeTempSensor(80);  // high temperature
	writeTempSensor(10);  // low temperature
	writeTempSensor(127);  // 12 bit mode

}

// write operation to temp sensor
void writeTempSensor(uint8_t data){


	for(int i=0;i<8;++i){

		if(data & (1 << i )){  // write 1
			setPinOutput();
			HAL_GPIO_WritePin(pinType, pin, GPIO_PIN_RESET);
			delay_us(1);  // wait 1 microsecond
			setPinInput();
			delay_us(60);  // wait period

		}else{  // write 0
			setPinOutput();
			HAL_GPIO_WritePin(pinType, pin, GPIO_PIN_RESET);
			delay_us(60);
			setPinInput();
		}
	}
}

// read operation from temp sensor
uint8_t readTempSensor(){
	uint8_t value =0;

	for(int i=0;i<8;++i){
		setPinOutput();

		HAL_GPIO_WritePin(pinType, pin, GPIO_PIN_RESET);
		delay_us(2);

		setPinInput();

		if(HAL_GPIO_ReadPin(pinType, pin) == GPIO_PIN_SET){
			value |= 1 << i;
		}

		delay_us(60);
	}

	return value;
}

// measure temperature from sensor and send the data
void measureTemperature(){

	sendResetPulse();
	writeTempSensor(0xcc);  // skip rom
	writeTempSensor(0x44);  // measure temperature

	HAL_TIM_Base_Start_IT(timer);
}

void sendTemperatureVal(){

	sendResetPulse();
	writeTempSensor(0xcc);  // skip rom
	writeTempSensor(0xbe);   // read scratchpad

	tempData[0] =60; // temperature communication code
	tempData[2] =readTempSensor();  // lsb byte
	tempData[1] =readTempSensor();  // msb byte

	sendDataDMA(tempData,3);
}





