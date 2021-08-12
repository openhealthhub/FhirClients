from typing import List

import gnupg
import fhirclient.models.questionnaireresponse as qr
from fhirclient.models.extension import Extension
from fhirclient.models.resource import Resource

from config.settings import server
encryptedAnswersUrl = 'http://openhealthhub.com/StructureDefinition/encryptedAnswers'
encryptedProfile = 'http://openhealthhub.com/StructureDefinition/EncryptedQuestionnaireResponse'


def is_encrypted(resource: Resource):
    encProfile = list(filter(lambda p: encryptedProfile == p, resource.meta.profile))
    return len(encProfile) > 0


response = qr.QuestionnaireResponse.read('1', server)

if is_encrypted(response):
    encryptedAnswers: List[Extension] = list(filter(lambda extension: encryptedAnswersUrl == extension.url,
                             response.extension))

    gpg = gnupg.GPG()
    with open('../../sandbox.key') as file:
        gpg.import_keys(file.read(), passphrase='api-sandbox')

    if len(encryptedAnswers) > 0:
        dec = gpg.decrypt(encryptedAnswers[0].valueString)
        print(dec)
