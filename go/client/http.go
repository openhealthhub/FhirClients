package client

import (
	"io/ioutil"
	"net/http"
	"openhealthhub.com/go/config"
)

func Read(resourceIdentifier string) ([]byte, error) {
	get, err := http.Get(config.Api + resourceIdentifier)
	if err != nil {
		return nil, err
	}
	return ioutil.ReadAll(get.Body)
}
