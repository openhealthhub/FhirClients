import asyncio

from config.settings import client
from fhirpy.lib import AsyncFHIRResource


async def questionnaire_response():
    questionnaire_response: AsyncFHIRResource = (
        await client.resources("QuestionnaireResponse")
        .search(_id="57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca")
        .get()
    )

    # or

    questionnaire_response: AsyncFHIRResource = await client.reference(
        "QuestionnaireResponse", "57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca"
    ).to_resource()

    print(questionnaire_response.resource_type, questionnaire_response.id)

    qrs = (
        await client.resources("QuestionnaireResponse")
        .search(
            based_on__instantiates_canonical="PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806"
        )
        .fetch()
    )

    for q in qrs:
        print(q.reference)


asyncio.run(questionnaire_response())
