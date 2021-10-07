package main

import (
	"fmt"
	"math/rand"
	"time"
)

const (
	TOBACCO int = iota
	PAPER
	MATCHES
)

func smoker(output string, infoChannel chan int, color string, colorReset string) {
	for {
		select {
		case <-infoChannel:
			fmt.Println(output)
			time.Sleep(2 * time.Second)
		}
	}
}

func defineGoodName(good int) string {
	result := ""
	switch good {
	case TOBACCO:
		result = "Tobacco"
	case PAPER:
		result = "Paper"
	case MATCHES:
		result = "Matches"
	}
	return result
}

func initiate(out chan string) {
	tobacco := make(chan int, 1)
	paper := make(chan int, 1)
	matches := make(chan int, 1)

	colorRed := "\033[31m"
	colorGreen := "\033[32m"
	colorBlue := "\033[34m"
	colorReset := "\033[0m"

	go smoker("Smoker with tobacco is smoking.", tobacco, colorRed, colorReset)
	go smoker("Smoker with paper is smoking.", paper, colorGreen, colorReset)
	go smoker("Smoker with matches is smoking.", matches, colorBlue, colorReset)

	for {
		select {
		case <-out:
			return
		default:
			fmt.Println("\nMediator is placing goods on the table:")
			good1 := rand.Intn(3)
			good2 := rand.Intn(3)
			for good1 == good2 {
				good2 = rand.Intn(3)
			}

			good1Name := defineGoodName(good1)
			good2Name := defineGoodName(good2)

			fmt.Printf("%s and %s.\n\n", good1Name, good2Name)

			switch {
			case good1 == TOBACCO && good2 == PAPER || good2 == TOBACCO && good1 == PAPER:
				matches <- 3
			case good1 == TOBACCO && good2 == MATCHES || good2 == TOBACCO && good1 == MATCHES:
				paper <- 2
			case good1 == PAPER && good2 == MATCHES || good2 == PAPER && good1 == MATCHES:
				tobacco <- 1
			}

			time.Sleep(2 * time.Second)
		}
	}
}

func main() {
	mainChan := make(chan string)
	fmt.Println("Smoking has started.")
	go initiate(mainChan)
	time.Sleep(10 * time.Second)
	mainChan <- "Enough"
	fmt.Println("\nSmoking has ended.")
}
