import asyncio
from typing import List

import gnupg
from config.settings import client

encryptedAnswersUrl = 'http://openhealthhub.com/StructureDefinition/encryptedAnswers'
encryptedProfile = 'http://openhealthhub.com/StructureDefinition/EncryptedQuestionnaireResponse'


def is_encrypted(resource):
    encProfile = list(filter(lambda p: encryptedProfile == p, resource.meta.profile))
    return len(encProfile) > 0


async def decrypt_questionnaire_response():
    response = await client.resource('QuestionnaireResponse').execute('1', 'GET')

    if is_encrypted(response):
        encryptedAnswers = list(filter(lambda extension: encryptedAnswersUrl == extension.url,
                                 response.extension))

        gpg = gnupg.GPG()
        with open('../../sandbox.key') as file:
            gpg.import_keys(file.read(), passphrase='api-sandbox')

        if len(encryptedAnswers) > 0:
            dec = gpg.decrypt(encryptedAnswers[0].valueString)
            print(dec)


asyncio.run(decrypt_questionnaire_response())
