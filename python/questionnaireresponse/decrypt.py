from typing import List

import gnupg
from config.settings import client

encryptedAnswersUrl = 'http://openhealthhub.com/StructureDefinition/encryptedAnswers'
encryptedProfile = 'http://openhealthhub.com/StructureDefinition/EncryptedQuestionnaireResponse'


def is_encrypted(resource):
    encProfile = list(filter(lambda p: encryptedProfile == p, resource.meta.profile))
    return len(encProfile) > 0


response = client.resources('QuestionnaireResponse').search(_id='1')

if is_encrypted(response):
    encryptedAnswers: List[Extension] = list(filter(lambda extension: encryptedAnswers == extension.url,
                             response.extension))

    gpg = gnupg.GPG()
    with open('../../sandbox.key') as file:
        gpg.import_keys(file.read(), passphrase='api-sandbox')

    if len(encryptedAnswers) > 0:
        dec = gpg.decrypt(encryptedAnswers[0].valueString)
        print(dec)
