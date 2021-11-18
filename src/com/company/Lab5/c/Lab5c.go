package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var LIMIT = 5

type ArrayManager struct {
	array      []int
	currentSum chan int
}

func NewArrayManager(size int) *ArrayManager {
	array := make([]int, size)

	for i := 0; i < size; i++ {
		array[i] = rand.Intn(int(LIMIT))
	}

	newManager := &ArrayManager{array, make(chan int, 1)}
	newManager.evaluateSum()

	return newManager
}

func (thread *ArrayManager) getCurrentSum() int {
	sum := <-thread.currentSum
	thread.currentSum <- sum
	return sum
}

func (thread *ArrayManager) evaluateSum() {
	sum := 0
	for _, currentItem := range thread.array {
		sum += currentItem
	}

	if len(thread.currentSum) == 1 {
		<-thread.currentSum
	}
	thread.currentSum <- sum
}

func (thread *ArrayManager) arrayModification() {
	index := rand.Intn(len(thread.array))

	if rand.Intn(2) == 0 {
		thread.array[index] = (thread.array[index] - 1) % int(LIMIT)
	} else {
		thread.array[index] = (thread.array[index] + 1) % int(LIMIT)
	}

	thread.evaluateSum()
}

func (thread *ArrayManager) print() {
	sum := thread.getCurrentSum()
	fmt.Printf("Elements: %v\nSum: %d\n", thread.array, sum)
}

func initialize() {
	var waitGroup sync.WaitGroup

	threads := make([]ArrayManager, 3)
	sums := make([]int, 3)

	for i := 0; i < 3; i++ {
		threads[i] = *(NewArrayManager(100))
		sums[i] = threads[i].getCurrentSum()
	}

	if sums[0] == sums[1] && sums[1] == sums[2] {
		fmt.Printf("Sums of three arays are equal and value is %d\n", sums[0])
		return
	}

	for {
		for i := 0; i < 3; i++ {
			waitGroup.Add(1)

			go func(index int) {
				defer waitGroup.Done()
				threads[index].arrayModification()
			}(i)
		}

		waitGroup.Wait()

		for i := 0; i < 3; i++ {
			sums[i] = threads[i].getCurrentSum()
		}

		if sums[0] == sums[1] && sums[1] == sums[2] {
			for i := 0; i < 3; i++ {
				threads[i].print()
			}

			fmt.Printf("Sums of three arays are equal and value is %d\n", sums[0])
			return
		}
	}
}

func main() {
	rand.Seed(time.Now().UnixNano())
	initialize()
}
