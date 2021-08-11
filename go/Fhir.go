package main

import "openhealthhub.com/go/appointment"

func main() {
	apt, err := appointment.Read()
	//if err != nil {
	//	panic(err)
	//}
	//print(apt.Description.Value)

	apt, err = appointment.Create()
	if err != nil {
		panic(err)
	}
	print(apt.Description.Value)

}
