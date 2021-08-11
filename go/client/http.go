package client

import (
	"io"
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

func Create(resource string, body io.Reader) ([]byte, error) {
	post, err := http.Post(config.Api+resource, "application/json", body)
	if err != nil {
		return nil, err
	}
	return ioutil.ReadAll(post.Body)
}
