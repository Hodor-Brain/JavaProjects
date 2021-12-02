package main

import (
	"fmt"
	"math/rand"
	"time"
)

type Semaphore interface {
	Lock()
	Unlock()
}

type semaphore struct {
	semC chan struct{}
}

func New(maxConcurrency int) Semaphore {
	return &semaphore{
		semC: make(chan struct{}, maxConcurrency),
	}
}

func (s *semaphore) Lock() {
	s.semC <- struct{}{}
}

func (s *semaphore) Unlock() {
	<-s.semC
}

func trainPassed(TrainId int) {
	fmt.Println(
		"Train",
		TrainId, "at", time.Now().Format("15:04:05"))
	time.Sleep(time.Duration(rand.Intn(3000)+500) * time.Millisecond)
}

func main() {
	semaf := New(2)
	exit := false

	trains := rand.Intn(10) + 10

	for i := 1; i <= trains; i++ {
		semaf.Lock()
		go func(v int) {
			defer semaf.Unlock()
			trainPassed(v)
			if v == trains {
				exit = true
			}
		}(i)
	}

	for {
		if exit == true {
			break
		}
	}

}
