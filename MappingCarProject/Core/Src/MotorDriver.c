#include "MotorDriver.h"
#include "Magnetometer.h"
#include <stdlib.h>
#include "BluetoothCom.h"

static GPIO_TypeDef* pinType;
static uint16_t left1;
static uint16_t left2;
static uint16_t right1;
static uint16_t right2;
static TIM_HandleTypeDef* mTimer;
static short value;
static uint8_t data;

void initMotorDriver(GPIO_TypeDef* pinT,uint16_t l1,uint16_t l2,uint16_t r1,uint16_t r2,TIM_HandleTypeDef* tim){
	pinType =pinT;

	left1 =l1;
	left2 =l2;
	right1 =r1;
	right2 =r2;

	mTimer =tim;

	resetMotorPins();
}


void goForward(uint16_t unit){

	value =unit;

	HAL_TIM_Base_Start_IT(mTimer);

	HAL_GPIO_WritePin(pinType, left1 | right1, GPIO_PIN_SET);
}

void setMotorValue(short val){
	value =val;
}

short getMotorValue(){

	return value;
}


void turnCar(uint16_t degree){

	uint16_t pinL, pinR;
	uint16_t counter, currentD;

	currentD =readCompass(1);

	if(((degree - currentD + 360) % 360) <= 180){  // clockwise
		pinL =left1;
		pinR =right2;

	}else{ //counter clockwise
		pinL =left2;
		pinR =right1;
	}

	counter = 1000;
	HAL_GPIO_WritePin(pinType, pinL | pinR , GPIO_PIN_SET);
	while(abs(currentD-degree) > 5  && counter != 0){
		HAL_Delay(5);
		currentD =readCompass(2);
		--counter;
	}
	resetMotorPins();

	readCompass(1);
}

void resetMotorPins(){

	HAL_GPIO_WritePin(pinType, left1 | left2 | right1 | right2, GPIO_PIN_RESET);

}

void motorGoForwardReturn(){
	data =20; // communication code
	sendData(&data,1);

}

void sendMotorMissionEnded(){
	data =21;
	sendData(&data,1);
}

void motorTimerCallback(){

	--value;
	if(value <= 0){
		resetMotorPins();
		HAL_TIM_Base_Stop_IT(mTimer);
	}
}



