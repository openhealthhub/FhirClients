package binary

import (
	"bytes"
	b64 "encoding/base64"
	"fmt"
	"io/ioutil"
	"net/http"
	"openhealthhub.com/go/client"
	"openhealthhub.com/go/config"
)

func Create() (int, error) {
	publicKey, err := ioutil.ReadFile("../sandbox.pub")
	if err != nil {
		return 0, err
	}

	pubKeysBytes := []byte(publicKey)
	encodedBytes := []byte(b64.StdEncoding.EncodeToString(pubKeysBytes))

	req, err := http.NewRequest("POST", fmt.Sprintf("%s/%s", config.Api, "Binary"), bytes.NewBuffer(encodedBytes))
	if err != nil {
		return 0, err
	}

	token := client.Authenticate()
	req.Header.Add("Authorization", "Bearer "+token)
	req.Header.Add("Content-Type", "text/plain")
	// TODO Add API-KEY

	resp, err := http.DefaultClient.Do(req)

	if err != nil {
		return 0, err
	}

	return resp.StatusCode, nil
}
