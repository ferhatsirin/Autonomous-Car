#include "Magnetometer.h"
#include "BluetoothCom.h"
#include <math.h>
#include <stdlib.h>
#include "DelayMS.h"

static I2C_HandleTypeDef* hi2c;
static const uint8_t deviceAddress =0x1A;
static uint8_t i2cData[7];
static uint8_t data[3];
static const double radyan =2*M_PI;
static const double toDegree =180.0/M_PI;
static int16_t compass_x;
static int16_t compass_y;
static double degree;
static uint16_t degree_i,lastDegree;
static uint8_t count;


// initialize magnetometer sensor to be used with provided pins
void initMagnetometer(I2C_HandleTypeDef* i2c){
	hi2c =i2c;

	count =0;
	lastDegree =0;

	softResetMagnetometer();
}

void initializeRegisters(){

	data[0] =9;
	data[1] =0x1d;
	HAL_I2C_Master_Transmit(hi2c,deviceAddress,data, 2, 100); // set mode

	data[0] =10;
	data[1] = 0x41;
	HAL_I2C_Master_Transmit(hi2c,deviceAddress,data, 2, 100); // set enable

	data[0] =11;
	data[1] = 0x01;
	HAL_I2C_Master_Transmit(hi2c,deviceAddress, data , 2, 100); // set reset

	data[0] =0;
	HAL_I2C_Master_Transmit(hi2c, deviceAddress,data, 1, 100);   // set address

}

/*
void hardResetMagnetometer(){

	HAL_GPIO_WritePin(powerPinType, powerPin, GPIO_PIN_RESET);
	HAL_Delay(1000);
	HAL_GPIO_WritePin(powerPinType, powerPin, GPIO_PIN_SET);
	HAL_Delay(10);

	initializeRegisters();
}
*/


void softResetMagnetometer(){
	data[0] =10;
	data[1] = 0xC1;
	HAL_I2C_Master_Transmit(hi2c,deviceAddress,data, 2, 100); // set enable
    HAL_Delay(5);

	initializeRegisters();
	HAL_Delay(10);
}

// get degree from magnetometer data
uint16_t getDegree(){

	degree=atan2(compass_y,compass_x);

	degree +=0.10006553796;  //magnetic declination

	if(degree < 0){
		degree +=radyan;
	}else if(degree > radyan){
		degree -=radyan;
	}

	degree = degree * toDegree;

	return (uint16_t)degree;
}

// read magnetometer register and save it to the compass variables
void readMagnetometerReg(){

	HAL_I2C_Master_Receive(hi2c, deviceAddress, i2cData, 7,100);

	compass_x =(((int16_t)i2cData[1]) << 8) | i2cData[0];
	compass_y =(((int16_t)i2cData[3]) << 8) | i2cData[2];

}


// read compass degree and send data
uint16_t readCompass(uint8_t motorDriver){


	if(motorDriver == 0 &&  count > 500 ){
		softResetMagnetometer();
		count =0;
	}else if(motorDriver == 1){
		softResetMagnetometer();
	}else{
		++count;
	}


	/*
	if(motorDriver != 2 && (motorDriver == 1 || count > 100)){
		hardResetMagnetometer();
		count =0;
		HAL_Delay(10);
	}

	++count;
*/
	readMagnetometerReg();

	degree_i =getDegree();

	if(abs(degree_i-lastDegree) > 4 ){

		data[0]=40; // communication code
		data[1] =(degree_i >> 8) & 0xFF; // first 8 byte
		data[2] =degree_i  & 0xFF;  // last 8 byte

		sendData(data,3);

		lastDegree =degree_i;
	}


	return degree_i;
}


