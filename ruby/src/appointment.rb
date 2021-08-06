require './fhir_client'

class AppointmentClient
  def get_appointment(id)
    FhirClient.new

    FHIR::Appointment.read(id)
  end
end
