require './fhir_client'

class CareTeamClient
  def get_careteam(id)
    FhirClient.new

    FHIR::CareTeam.read(id)
  end

  def get_careteam_with_practitioners(id)
    FhirClient.new

    params = {
      '_id': id,
      '_include': 'CareTeam:participant'
    }
    FHIR::CareTeam.search(params)
  end

end
