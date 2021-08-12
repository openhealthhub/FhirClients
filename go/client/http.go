package client

import (
	"fmt"
	"github.com/google/fhir/go/jsonformat"
	r4pb "github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/bundle_and_contained_resource_go_proto"
	"google.golang.org/protobuf/proto"
	"io"
	"io/ioutil"
	"net/http"
	"openhealthhub.com/go/config"
	"strings"
)

func Read(resourceIdentifier string) (*r4pb.ContainedResource, error) {
	response, err := get(config.Api + resourceIdentifier)
	if err != nil {
		return nil, err
	}
	return parseResource(response.Body)
}

func Create(resource string, body io.Reader) (*r4pb.ContainedResource, error) {
	post, err := request("POST", fmt.Sprintf("%s/%s", config.Api, resource), body)
	if err != nil {
		return nil, err
	}
	return parseResource(post.Body)
}

func Search(resource string, queryParams ...string) (*r4pb.ContainedResource, error) {
	url := fmt.Sprintf("%s/%s?%s", config.Api, resource, strings.Join(queryParams, "&"))
	get, err := get(url)
	if err != nil {
		return nil, err
	}

	return parseResource(get.Body)
}

func parseResource(body io.Reader) (*r4pb.ContainedResource, error) {
	unmarshal, err := unmarshall(body)
	if err != nil {
		return nil, err
	}

	return unmarshal.(*r4pb.ContainedResource), nil
}

func unmarshall(body io.Reader) (proto.Message, error) {
	unmarshaller, err := jsonformat.NewUnmarshaller(config.TimeZone, config.FhirVersion)
	if err != nil {
		return nil, err
	}

	bytes, err := ioutil.ReadAll(body)
	if err != nil {
		return nil, err
	}

	unmarshal, err := unmarshaller.Unmarshal(bytes)
	if err != nil {
		return nil, err
	}
	return unmarshal, nil
}

func get(url string) (*http.Response, error) {
	return request("GET", url, nil)
}

func request(method, url string, body io.Reader) (*http.Response, error) {
	req, err := http.NewRequest(method, url, body)
	if err != nil {
		return nil, err
	}
	req.Header.Add("X-API-Key", "ad880601-b7e6-4d86-901d-b6fca96fc725")
	if method == "POST" {
		req.Header.Add("Content-Type", "application/json")
	}

	return http.DefaultClient.Do(req)
}
