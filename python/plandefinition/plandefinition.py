import asyncio

from config.settings import client

from fhirpy.lib import AsyncFHIRResource


async def get_plandefinition():
    plandefinition: AsyncFHIRResource = (
        await client.resources("PlanDefinition").search(_id=1).get()
    )

    # or

    plandefinition: AsyncFHIRResource = await client.reference(
        "PlanDefinition", 1
    ).to_resource()

    print(plandefinition.resourceType, plandefinition.id)

    # Fetch returns records from the one page
    # For getting all resources from on all pages use .fetch_all()

    # Keep in mind that this method as well as .fetch() doesn't return any included resources.
    # Use fetch_raw() if you want to get all included resources.
    plandefinitions = (
        await client.resources("PlanDefinition")
        .search(
            definition="Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d",
            publisher="Program Creator",
        )
        .fetch()
    )

    for p in plandefinitions:
        print(p["description"])


asyncio.run(get_plandefinition())
