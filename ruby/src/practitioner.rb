require './fhir_client'

class PractitionerClient
  def get_practitioner(id)
    FhirClient.new

    FHIR::Practitioner.read(id)
  end

  def search_practitioners_by_careteam_id(id)
    FhirClient.new

    params = {
      '_has:CareTeam:_id': id
    }
    FHIR::Practitioner.search(params)
  end

end
