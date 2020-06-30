#include "UltraSonicSensor.h"
#include "DelayMS.h"
#include "BluetoothCom.h"


static GPIO_TypeDef* sensorLT, *sensorCT, *sensorRT, *sensorLE, *sensorCE, *sensorRE;
static uint16_t trigL,trigC,trigR,echoL,echoC,echoR;
static uint32_t localTime;
static uint8_t distance, leftD, centerD, rightD;
static const float velocity =0.01716;
static uint8_t data[4];
static TIM_HandleTypeDef* timer;

void initUltraSonicSensor(GPIO_TypeDef* sensorLTP,uint16_t trigLP, GPIO_TypeDef* sensorLEP,uint16_t echoLP,
		GPIO_TypeDef* sensorCTP, uint16_t trigCP,GPIO_TypeDef* sensorCEP, uint16_t echoCP,GPIO_TypeDef* sensorRTP,
		uint16_t trigRP,GPIO_TypeDef* sensorREP, uint16_t echoRP,TIM_HandleTypeDef* tim){

	sensorLT =sensorLTP;
	sensorLE =sensorLEP;
	sensorCT =sensorCTP;
	sensorCE =sensorCEP;
	sensorRT =sensorRTP;
	sensorRE =sensorREP;

	trigL =trigLP;
	trigC =trigCP;
	trigR =trigRP;

	echoL =echoLP;
	echoC =echoCP;
	echoR =echoRP;

	timer =tim;

}

uint8_t readSensor(GPIO_TypeDef* sensorT,uint16_t trig,GPIO_TypeDef* sensorE,uint16_t echo){

	HAL_GPIO_WritePin(sensorT, trig, GPIO_PIN_SET);

	delay_us(10); //10 microseconds

	HAL_GPIO_WritePin(sensorT,trig,GPIO_PIN_RESET);

	__HAL_TIM_SET_COUNTER(timer,0);
	while(!HAL_GPIO_ReadPin(sensorE, echo) && __HAL_TIM_GET_COUNTER(timer) <= 500);
	if(100 < __HAL_TIM_GET_COUNTER(timer) && __HAL_TIM_GET_COUNTER(timer) < 500){
		__HAL_TIM_SET_COUNTER(timer,0);
		while(HAL_GPIO_ReadPin(sensorE,echo) && __HAL_TIM_GET_COUNTER(timer) <= 10000);
		localTime = __HAL_TIM_GET_COUNTER(timer);

		if(localTime < 10000){
			distance =velocity*localTime;
		}
		else{
			distance =0;
		}
	}else{
		distance = 0;
	}

	return distance;
}

uint8_t measureLeftDistance(){
	return readSensor(sensorLT,trigL,sensorLE,echoL);
}

uint8_t measureCenterDistance(){
	return readSensor(sensorCT,trigC,sensorCE,echoC);
}

uint8_t measureRightDistance(){
	return readSensor(sensorRT,trigR,sensorRE,echoR);
}

void measureDistances(uint8_t cancel){

	leftD =measureLeftDistance();
	if(cancel == 1){
		centerD =0;
	}else{
		uint8_t val;
		centerD =0;
		for(uint8_t i =0;i<10;++i){
			val =measureCenterDistance();
			if(centerD < val){
				centerD =val;
			}
		}
	}
	rightD = measureRightDistance();

	data[0] = 30;
	data[1] = leftD;
	data[2] = centerD;
	data[3] = rightD;

	sendData(data,4);

}






