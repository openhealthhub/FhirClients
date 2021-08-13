from config.settings import server
from config.settings import client

obs = client.resources('Observation').search(_id='1')

print(obs.resource_type, obs.id)

observations = client.resources('Observation').search(identifier='identifier', device_name='devicename')

for o in observations:
    print(o.as_json())
