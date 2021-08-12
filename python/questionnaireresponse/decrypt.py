from typing import List

import gnupg
import fhirclient.models.questionnaireresponse as qr
from fhirclient.models.extension import Extension

from config.settings import server
encryptedAnswersUrl = 'http://openhealthhub.com/StructureDefinition/encryptedAnswers'


response = qr.QuestionnaireResponse.read('1', server)
encryptedAnswers: List[Extension] = list(filter(lambda extension: encryptedAnswers == extension.url,
                         response.extension))

gpg = gnupg.GPG()
with open('../../sandbox.key') as file:
    gpg.import_keys(file.read(), passphrase='api-sandbox')

if len(encryptedAnswers) > 0:
    dec = gpg.decrypt(encryptedAnswers[0].valueString)
    print(dec)

