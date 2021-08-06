require './fhir_client'

class VitalSignsClient
  def get_vitalsigns(id)
    FhirClient.new

    FHIR::Observation.read(id)
  end
end
