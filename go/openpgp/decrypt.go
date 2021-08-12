package openpgp

import (
	"github.com/ProtonMail/gopenpgp/v2/helper"
	"io/ioutil"
)

const password = "api-sandbox"

func Decrypt(armored string) (string, error) {
	privateKey, err := ioutil.ReadFile("../sandbox.key")
	if err != nil {
		return "", err
	}

	message, err := helper.DecryptMessageArmored(string(privateKey), []byte("api-sandbox"), armored)
	if err != nil {
		return "", err
	}

	return message, nil
}
