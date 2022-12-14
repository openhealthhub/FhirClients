import asyncio
from typing import List

import gnupg
from config.settings import client

encryptedAnswersUrl = 'https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/encryptedAnswers'
encryptedProfile = 'https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/EncryptedQuestionnaireResponse'


def is_encrypted(resource):
    encProfile = list(filter(lambda p: encryptedProfile == p, resource.meta.profile))
    return len(encProfile) > 0


async def decrypt_questionnaire_response():
    resource = await client.resources('QuestionnaireResponse').search(_id=1).get()

    if is_encrypted(resource):
        encryptedAnswers = list(filter(lambda extension: encryptedAnswersUrl == extension.url,
                                 resource.extension))

        gpg = gnupg.GPG()
        with open('../../sandbox.key') as file:
            gpg.import_keys(file.read(), passphrase='api-sandbox')

        if len(encryptedAnswers) > 0:
            dec = gpg.decrypt(encryptedAnswers[0].valueString)
            print(dec)


asyncio.run(decrypt_questionnaire_response())
