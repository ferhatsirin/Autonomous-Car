#include "WorkQueue.h"
#include <stdlib.h>
#include <string.h>

WorkQueue* createQueue(uint16_t capacity){
    WorkQueue* queue = (WorkQueue*) malloc(sizeof(WorkQueue));
    queue->capacity = capacity;
    queue->front = 0;
    queue->size = 0;
    queue->rear = capacity - 1;
    queue->work = (Work*) malloc(queue->capacity * sizeof(Work));
    return queue;
}

// Queue is full when size becomes equal to the capacity
uint8_t isFull(WorkQueue* queue){
	return (queue->size == queue->capacity);
}

// Queue is empty when size is 0
uint8_t isEmpty(WorkQueue* queue){
	return (queue->size == 0);
}

// Function to add an item to the queue.
// It changes rear and size
void enqueue(WorkQueue* queue, uint8_t* item,uint8_t length){
    if (isFull(queue))
        return;

    queue->rear = (queue->rear + 1)%queue->capacity;
    memcpy(queue->work[queue->rear].data,item,length);
    ++queue->size;
}


// Function to remove an item from queue.
// It changes front and size
uint8_t* dequeue(WorkQueue* queue){
    if (isEmpty(queue))
        return NULL;

    uint8_t* item = queue->work[queue->front].data;
    queue->front = (queue->front + 1)%queue->capacity;
    --queue->size;

    return item;
}

// Function to get front of queue
uint8_t* front(WorkQueue* queue){
    if (isEmpty(queue))
        return NULL;

    return queue->work[queue->front].data;
}

// Function to get rear of queue
uint8_t* rear(WorkQueue* queue){
    if (isEmpty(queue))
        return NULL;

    return queue->work[queue->rear].data;
}
