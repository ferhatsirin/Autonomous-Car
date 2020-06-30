#include "DelayMS.h"

TIM_HandleTypeDef* usTimer;

void initDelayMS(TIM_HandleTypeDef* tim){
	usTimer =tim;
	HAL_TIM_Base_Start(usTimer);
}

// delay in microsecond
void delay_us(uint16_t delay){
	__HAL_TIM_SET_COUNTER(usTimer,0);

	while(__HAL_TIM_GET_COUNTER(usTimer) < delay);

}

void setCounter(uint32_t cnt){
	__HAL_TIM_SET_COUNTER(usTimer,cnt);
}

uint32_t getCounter(){
	return __HAL_TIM_GET_COUNTER(usTimer);
}



