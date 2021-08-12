from config.settings import server
import fhirclient.models.observation as o

obs = o.Observation.read('1', server)

print(obs.resource_type, obs.id)

observations = o.Observation.where(struct={
    'identifier': 'identifier',
    'device-name': 'devicename'
}).perform_resources(server)

for o in observations:
    print(o.as_json())
