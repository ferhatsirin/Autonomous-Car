/*
 * WorkQueue.h
 *
 *  Created on: Mar 20, 2020
 *      Author: ferhat
 */

#ifndef INC_WORKQUEUE_H_
#define INC_WORKQUEUE_H_

#include "stm32f4xx_hal.h"

typedef struct{
	uint8_t data[3];
}Work;

typedef struct {
	uint16_t size;
	uint16_t capacity;
	uint16_t rear;
	uint16_t front;
	Work* work;

}WorkQueue;


WorkQueue* createQueue(uint16_t capacity);

// Queue is full when size becomes equal to the capacity
uint8_t isFull(WorkQueue* queue);

// Queue is empty when size is 0
uint8_t isEmpty(WorkQueue* queue);

// Function to add an item to the queue.
// It changes rear and size
void enqueue(WorkQueue* queue, uint8_t* item, uint8_t length);

// Function to remove an item from queue.
// It changes front and size
uint8_t* dequeue(WorkQueue* queue);

// Function to get front of queue
uint8_t* front(WorkQueue* queue);

// Function to get rear of queue
uint8_t* rear(WorkQueue* queue);


#endif /* INC_WORKQUEUE_H_ */
