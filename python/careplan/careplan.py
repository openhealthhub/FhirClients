import asyncio
import json
import sys

sys.path.append('../')

from config.settings import client
from fhirpy.lib import AsyncFHIRResource


async def get_careplan():
    care_plan: AsyncFHIRResource = (
        await client.resources("CarePlan").search(_id=1).get()
    )

    # Resource support accessing its elements
    # both as attribute and as a dictionary keys
    print(care_plan["instantiatesCanonical"])

    # fetch returns one page of AsyncFHIRResources
    # if you would like to get all resources on all the pages
    # use fetch_all() instead
    care_plans = (
        await client.resources("CarePlan")
        .search(
            instantiates_canonical="PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806"
        )
        .fetch()
    )

    print("Care plans: ".format([resource.serialize() for resource in care_plans]))

    # fetch_raw returns a FHIR Bundle of AsyncResources.
    # Always use fetch_raw with .include/.revinclude
    get_with_practitioners = (
        await client.resources("CarePlan")
        .search(_id=1)
        .include("CarePlan", "care-team")
        .include("CareTeam", "participant")
        .fetch_raw()
    )

    print(get_with_practitioners)

    with open("careplan.json", "r") as file:
        careplan_json = json.load(file)

    # create CarePlan resource
    care_plan = client.resource("CarePlan", **careplan_json)
    await care_plan.save()

    print("Created care plan:\n {}".format(care_plan.serialize()))

    # Update CarePlan
    care_plan.title = "Care plan title"
    await care_plan.save()
    print("Updated care plan:\n {}".format(care_plan.serialize()))

    # Delete CarePlan
    await care_plan.delete()


asyncio.run(get_careplan())
