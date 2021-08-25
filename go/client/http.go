package client

import (
	"encoding/json"
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

	req.Header.Add("X-API-Key", config.ApiKey)

	token := authenticate()
	req.Header.Add("Authorization", "Bearer "+token)

	if method == "POST" {
		req.Header.Add("Content-Type", "application/json")
	}

	return http.DefaultClient.Do(req)
}

type TokenResponse struct {
	Access_token string
}

func authenticate() string {
	url := config.KeycloakTokenUri
	method := "POST"

	payload := strings.NewReader("client_id=" + config.KeycloakClientId + "&client_secret=" + config.KeycloakClientSecret + "&grant_type=client_credentials")

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")

	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	defer res.Body.Close()

	data := new(TokenResponse)
	err = json.NewDecoder(res.Body).Decode(&data)
	if err != nil {
		fmt.Println(err)
		return ""
	}

	return data.Access_token
}
