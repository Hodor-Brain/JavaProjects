package main

import (
	"fmt"
	"strings"
	"sync"
	"time"
)

type Status int

const (
	Unvisited Status = iota
	InProcess
	Visited
)

type City struct {
	name        string
	contagious  []*City
	ticketPrice []int
}

func (city *City) getGraphviz() string {
	resultString := fmt.Sprintf("\"%s\"\n", city.name)
	for index, nearbyCity := range city.contagious {
		resultString += fmt.Sprintf("\"%s\" -> \"%s\" [ label = \"%d\" ]\n",
			city.name, nearbyCity.name, city.ticketPrice[index])
	}

	return resultString
}

func (city *City) changeRoutePrice(destination string, newPrice int) {
	for index, adjacentVertex := range city.contagious {
		if adjacentVertex.name == destination {
			city.ticketPrice[index] = newPrice
			return
		}
	}

	fmt.Printf("Can't change ticketPrice of unexisting between %s and %s!\n", city.name, destination)
}

func (city *City) removeRoute(destination string) {
	for index, adjacentVertex := range city.contagious {
		if adjacentVertex.name == destination {
			city.contagious = append(city.contagious[:index], city.contagious[index+1:]...)
			city.ticketPrice = append(city.ticketPrice[:index], city.ticketPrice[index+1:]...)
			return
		}
	}

	fmt.Printf("Can't remove unexisting route between %s and %s!\n", city.name, destination)
}

func (city *City) removeContagiousCities() {
	for _, adjacentVertex := range city.contagious {
		city.removeRoute(adjacentVertex.name)
		adjacentVertex.removeRoute(city.name)
	}
}

func (city *City) dfs(conditionOfVertex map[string]Status, previous map[string]string) {
	conditionOfVertex[city.name] = InProcess

	for _, adjacentVertex := range city.contagious {
		if conditionOfVertex[adjacentVertex.name] == Unvisited {
			previous[adjacentVertex.name] = city.name
			adjacentVertex.dfs(conditionOfVertex, previous)
		}
	}

	conditionOfVertex[city.name] = Visited
}

type CitiesGraph struct {
	citiesList []*City
}

func (citiesGraph *CitiesGraph) getCity(cityName string) *City {
	for _, city := range citiesGraph.citiesList {
		if city.name == cityName {
			return city
		}
	}
	return nil
}

func (citiesGraph *CitiesGraph) addCity(cityName string) {
	if citiesGraph.exists(cityName) {
		fmt.Printf("City \"%s\" already exists in a CitiesGraph\n", cityName)
		return
	}

	citiesGraph.citiesList = append(citiesGraph.citiesList,
		&City{
			name: cityName, contagious: []*City{}, ticketPrice: []int{},
		})
}

func (citiesGraph *CitiesGraph) removeCity(cityName string) {
	if !citiesGraph.exists(cityName) {
		fmt.Printf("City \"%s\" doesn`t exist in a CitiesGraph\n", cityName)
	}

	for index, city := range citiesGraph.citiesList {
		if city.name == cityName {
			city.removeContagiousCities()
			citiesGraph.citiesList = append(citiesGraph.citiesList[:index], citiesGraph.citiesList[index+1:]...)
		}
	}
}

func (citiesGraph *CitiesGraph) exists(city string) bool {
	return citiesGraph.getCity(city) != nil
}

func (citiesGraph *CitiesGraph) removeRoute(departure, destination string) {
	if departure == destination {
		fmt.Printf("Departure %s and destination %s are the same city!", departure, destination)
		return
	}

	fromVertex := citiesGraph.getCity(departure)
	toVertex := citiesGraph.getCity(destination)

	if fromVertex == nil || toVertex == nil {
		fmt.Println("Incorrect city name occurred")
		return
	}

	fromVertex.removeRoute(destination)
	toVertex.removeRoute(departure)
}

func (citiesGraph *CitiesGraph) addEdge(from, to string, ticketPrice int) {
	if from == to {
		fmt.Printf("Departure %s and destination %s are the same city!\n", from, to)
		return
	}

	fromVertex := citiesGraph.getCity(from)
	toVertex := citiesGraph.getCity(to)

	if fromVertex == nil || toVertex == nil {
		fmt.Println("Wrong data!")
		return
	}

	fromVertex.contagious = append(fromVertex.contagious, toVertex)
	fromVertex.ticketPrice = append(fromVertex.ticketPrice, ticketPrice)

	toVertex.contagious = append(toVertex.contagious, fromVertex)
	toVertex.ticketPrice = append(toVertex.ticketPrice, ticketPrice)
}

func (citiesGraph *CitiesGraph) changePrice(departure, destination string, newPrice int) {
	if !citiesGraph.exists(departure) || !citiesGraph.exists(destination) {
		fmt.Printf("No route between %s and %s!", departure, destination)
		return
	}

	firstVertex := citiesGraph.getCity(departure)
	secondVertex := citiesGraph.getCity(destination)

	firstVertex.changeRoutePrice(destination, newPrice)
	secondVertex.changeRoutePrice(departure, newPrice)
}

func (citiesGraph *CitiesGraph) dfs(departure string) map[string]string {
	conditionOfVertex := make(map[string]Status)
	previous := make(map[string]string)

	for _, city := range citiesGraph.citiesList {
		conditionOfVertex[city.name] = Unvisited
		previous[city.name] = ""
	}

	initialVertex := citiesGraph.getCity(departure)

	initialVertex.dfs(conditionOfVertex, previous)

	for _, city := range citiesGraph.citiesList {
		if conditionOfVertex[city.name] == Unvisited {
			city.dfs(conditionOfVertex, previous)
		}
	}

	return previous
}

func (citiesGraph *CitiesGraph) getAdjacentPathCost(departure, destination string) int {
	firstVertex := citiesGraph.getCity(departure)
	if firstVertex == nil {
		fmt.Printf("There is no city %s\n", departure)
		return -1
	}

	for index, adjacentVertex := range firstVertex.contagious {
		if adjacentVertex.name == destination {
			return firstVertex.ticketPrice[index]
		}
	}

	panic("Vertices are not contagious")
}

func (citiesGraph *CitiesGraph) getRouteCost(departure, destination string) (bool, int) {
	ticketPrice := 0
	previous := citiesGraph.dfs(departure)

	previousCity := destination
	currentCity := destination

	for {
		previousCity = currentCity
		currentCity = previous[currentCity]

		if currentCity == "" {
			return false, -1
		}

		ticketPrice += citiesGraph.getAdjacentPathCost(previousCity, currentCity)

		if currentCity == departure {
			return true, ticketPrice
		}
	}
}

func (citiesGraph *CitiesGraph) getGraphviz(name string) string {
	name = strings.ReplaceAll(name, " ", "_")

	graphString := fmt.Sprintf("\ndigraph %s {\n", name)

	for _, city := range citiesGraph.citiesList {
		graphString += city.getGraphviz()
	}

	graphString += "}"

	return graphString
}

type GraphWithLock struct {
	container CitiesGraph
	mutex     sync.RWMutex
}

func (citiesGraph *GraphWithLock) changeRoutePrice(departure, destination string, newPrice int) {
	citiesGraph.mutex.Lock()
	citiesGraph.container.changePrice(departure, destination, newPrice)
	citiesGraph.mutex.Unlock()
}

func (citiesGraph *GraphWithLock) addRoute(departure, destination string, ticketPrice int) {
	citiesGraph.mutex.Lock()
	citiesGraph.container.addEdge(departure, destination, ticketPrice)
	citiesGraph.mutex.Unlock()
}

func (citiesGraph *GraphWithLock) removeRoute(departure, destination string) {
	citiesGraph.mutex.Lock()
	citiesGraph.container.removeRoute(departure, destination)
	citiesGraph.mutex.Unlock()
}

func (citiesGraph *GraphWithLock) addCity(city string) {
	citiesGraph.mutex.Lock()
	citiesGraph.container.addCity(city)
	citiesGraph.mutex.Unlock()
}

func (citiesGraph *GraphWithLock) removeCity(city string) {
	citiesGraph.mutex.Lock()
	citiesGraph.container.removeCity(city)
	citiesGraph.mutex.Unlock()
}

func (citiesGraph *GraphWithLock) getRouteCost(departure, destination string) {
	citiesGraph.mutex.Lock()
	exists, cost := citiesGraph.container.getRouteCost(departure, destination)
	citiesGraph.mutex.Unlock()

	if exists {
		fmt.Printf("\"%s\" -> \"%s\" route cost is %d\n", departure, destination, cost)
	} else {
		fmt.Printf("No route between \"%s\" and \"%s\"\n", departure, destination)
	}
}

func (citiesGraph *GraphWithLock) printGraphviz(name string) {
	citiesGraph.mutex.Lock()
	fmt.Println(citiesGraph.container.getGraphviz(name))
	citiesGraph.mutex.Unlock()
}

func changePriceThread(citiesGraph *GraphWithLock) {
	citiesGraph.changeRoutePrice("Kyiv", "Lviv", 200)
	citiesGraph.changeRoutePrice("Herson", "Lviv", 500)
	citiesGraph.changeRoutePrice("Kyiv", "Zhytomyr", 300)
	citiesGraph.changeRoutePrice("Bila Tserkva", "Lviv", 385)
}

func addDeleteRoutesThread(citiesGraph *GraphWithLock) {
	citiesGraph.addRoute("Herson", "Kharkiv", 1000)
	citiesGraph.addRoute("Herson", "Kyiv", 750)
	citiesGraph.addRoute("Herson", "Bila Tserkva", 1500)
	citiesGraph.removeRoute("Herson", "Kyiv")
	citiesGraph.removeRoute("Ukrainka", "Kyiv")
	citiesGraph.addRoute("Ukrainka", "Obukhiv", 750)
	citiesGraph.removeRoute("Herson", "Bila Tserkva")
	citiesGraph.removeRoute("Lviv", "Kharkiv")

}

func addDeleteCitiesThread(citiesGraph *GraphWithLock) {
	citiesGraph.addCity("IvanoNefrankivsk")
	citiesGraph.addCity("Varash")
	citiesGraph.addCity("Lugansk")
	citiesGraph.removeCity("Rivne")
	citiesGraph.addCity("Obukhiv")
	citiesGraph.removeCity("Kramatorsk")
	citiesGraph.addCity("Zhmerynka")
	citiesGraph.addCity("Rivne")
	citiesGraph.removeCity("Lviv")
	citiesGraph.addCity("Yalta")
	citiesGraph.removeCity("Boryspil")
	citiesGraph.removeCity("Zhmerynka")
}

func routeFinderThread(citiesGraph *GraphWithLock) {
	citiesGraph.getRouteCost("Kyiv", "Ukrainka")
	citiesGraph.getRouteCost("Herson", "Lviv")
	citiesGraph.getRouteCost("Bila Tserkva", "Lviv")
	citiesGraph.getRouteCost("Herson", "Kharkiv")
	citiesGraph.getRouteCost("Kyiv", "Obukhiv")
	citiesGraph.getRouteCost("Zhytomyr", "Lviv")
}

func main() {
	citiesGraph := &GraphWithLock{}

	citiesGraph.addCity("Kyiv")
	citiesGraph.addCity("Ukrainka")
	citiesGraph.addCity("Lviv")
	citiesGraph.addCity("Kharkiv")
	citiesGraph.addCity("Herson")
	citiesGraph.addCity("Bila Tserkva")
	citiesGraph.addCity("Zhytomyr")
	citiesGraph.addCity("IvanoFrankivsk")
	citiesGraph.addCity("Kramatorsk")
	citiesGraph.addCity("Lugansk")
	citiesGraph.addCity("Yalta")
	citiesGraph.addCity("Obukhiv")
	citiesGraph.addCity("Boryspil")

	citiesGraph.addRoute("Kyiv", "Lviv", 150)
	citiesGraph.addRoute("Herson", "Lviv", 550)
	citiesGraph.addRoute("Kyiv", "Zhytomyr", 400)
	citiesGraph.addRoute("Bila Tserkva", "Lviv", 370)
	citiesGraph.addRoute("Ukrainka", "Kyiv", 35)
	citiesGraph.addRoute("Zhytomyr", "Lviv", 600)
	citiesGraph.addRoute("Herson", "Kharkiv", 1000)
	citiesGraph.addRoute("Herson", "Kyiv", 750)
	citiesGraph.addRoute("Kharkiv", "Lviv", 754)
	citiesGraph.addRoute("Kyiv", "Obukhiv", 35)

	citiesGraph.printGraphviz("Initial")

	go changePriceThread(citiesGraph)
	go addDeleteRoutesThread(citiesGraph)
	go addDeleteCitiesThread(citiesGraph)
	go routeFinderThread(citiesGraph)

	time.Sleep(10 * time.Second)
	citiesGraph.printGraphviz("Final")
}
