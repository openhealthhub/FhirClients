package main

import "openhealthhub.com/go/appointment"

func main() {
	appointment, err := appointment.Read()
	if err != nil {
		panic(err)
	}
	print(appointment.Description.Value)

}
