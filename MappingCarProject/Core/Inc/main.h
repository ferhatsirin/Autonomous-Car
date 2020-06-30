/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.h
  * @brief          : Header for main.c file.
  *                   This file contains the common defines of the application.
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2020 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/* USER CODE END Header */

/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __MAIN_H
#define __MAIN_H

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "stm32f4xx_hal.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Exported types ------------------------------------------------------------*/
/* USER CODE BEGIN ET */

/* USER CODE END ET */

/* Exported constants --------------------------------------------------------*/
/* USER CODE BEGIN EC */

/* USER CODE END EC */

/* Exported macro ------------------------------------------------------------*/
/* USER CODE BEGIN EM */

/* USER CODE END EM */

/* Exported functions prototypes ---------------------------------------------*/
void Error_Handler(void);

/* USER CODE BEGIN EFP */

/* USER CODE END EFP */

/* Private defines -----------------------------------------------------------*/
#define RightSensorEcho_Pin GPIO_PIN_0
#define RightSensorEcho_GPIO_Port GPIOC
#define RightSensorTrg_Pin GPIO_PIN_1
#define RightSensorTrg_GPIO_Port GPIOC
#define Temp_Sensor_Pin GPIO_PIN_3
#define Temp_Sensor_GPIO_Port GPIOC
#define LeftSensorTrg_Pin GPIO_PIN_0
#define LeftSensorTrg_GPIO_Port GPIOA
#define LeftSensorEcho_Pin GPIO_PIN_1
#define LeftSensorEcho_GPIO_Port GPIOA
#define CenterSensorTrg_Pin GPIO_PIN_4
#define CenterSensorTrg_GPIO_Port GPIOA
#define CenterSensorEcho_Pin GPIO_PIN_0
#define CenterSensorEcho_GPIO_Port GPIOB
#define LeftMotor1_Pin GPIO_PIN_1
#define LeftMotor1_GPIO_Port GPIOB
#define LeftMotor2_Pin GPIO_PIN_2
#define LeftMotor2_GPIO_Port GPIOB
#define RightMotor2_Pin GPIO_PIN_14
#define RightMotor2_GPIO_Port GPIOB
#define RightMotor1_Pin GPIO_PIN_15
#define RightMotor1_GPIO_Port GPIOB
#define USART1_TX_BLT_Pin GPIO_PIN_9
#define USART1_TX_BLT_GPIO_Port GPIOA
#define USART1_RX_BLT_Pin GPIO_PIN_10
#define USART1_RX_BLT_GPIO_Port GPIOA
#define Flame_Sensor_Pin GPIO_PIN_11
#define Flame_Sensor_GPIO_Port GPIOC
#define Flame_Sensor_EXTI_IRQn EXTI15_10_IRQn
#define I2C1_SCL_Compass_Pin GPIO_PIN_6
#define I2C1_SCL_Compass_GPIO_Port GPIOB
#define I2C1_SDA_Compass_Pin GPIO_PIN_7
#define I2C1_SDA_Compass_GPIO_Port GPIOB
/* USER CODE BEGIN Private defines */

/* USER CODE END Private defines */

#ifdef __cplusplus
}
#endif

#endif /* __MAIN_H */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
