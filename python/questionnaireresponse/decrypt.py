from typing import List

import gnupg
from config.settings import client

encryptedAnswersUrl = 'http://openhealthhub.com/StructureDefinition/encryptedAnswers'


response = client.resources('QuestionnaireResponse').search(_id='1')
encryptedAnswers: List[Extension] = list(filter(lambda extension: encryptedAnswers == extension.url,
                         response.extension))

gpg = gnupg.GPG()
with open('../../sandbox.key') as file:
    gpg.import_keys(file.read(), passphrase='api-sandbox')

if len(encryptedAnswers) > 0:
    dec = gpg.decrypt(encryptedAnswers[0].valueString)
    print(dec)

