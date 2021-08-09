require './fhir_client'

class VitalSignsClient
  def get_vitalsigns(id)
    FhirClient.new

    FHIR::Observation.read(id)
  end

  def search_vitalsigns
    FhirClient.new

    params = {
      'identifier': '1234',
      'device-name': 'testDevice'
    }
    FHIR::Observation.search(params)
  end
end
