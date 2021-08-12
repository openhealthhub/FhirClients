require './fhir_client'

class SubscriptionClient
  def create_subscription
    FhirClient.new

    subscription = FHIR::Subscription.new

    subscription.criteria = 'Appointment?name=test'
    subscription.channel = FHIR::Subscription::Channel.new
    subscription.channel.type = 'rest-hook'
    subscription.channel.endpoint = 'https://your-webhook/endpoint'

    FHIR::Subscription.create(subscription)
  end
end
