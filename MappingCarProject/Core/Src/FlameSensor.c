#include "FlameSensor.h"
#include "BluetoothCom.h"

static GPIO_TypeDef* fpinType;
static uint16_t fpin;
static uint8_t data[2];
static uint8_t lastData;

// initialize flame sensor with pins
void initFlameSensor(GPIO_TypeDef* pinT,uint16_t pinN){

	fpinType =pinT;
	fpin =pinN;
	lastData =0;
}

//read the flame sensor and sends its data
void readFlameSensor(){

	data[0] =50;  //communication code

	if (HAL_GPIO_ReadPin(fpinType, fpin) == GPIO_PIN_SET){
		if(lastData == 0){
			data[1] = 1;
			sendData(data,2);
			lastData =1;
		}
	}else{
		if(lastData == 1){
			data[1] = 0;
			sendData(data,2);
			lastData =0;
		}
	}

}
