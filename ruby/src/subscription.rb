require './fhir_client'

class SubscriptionClient
  def create_subscription
    FhirClient.new

    subscription = FHIR::Subscription.new

    subscription.criteria = 'Appointment?name=test'
    subscription.status = 'requested'
    subscription.channel = FHIR::Subscription::Channel.new
    subscription.channel.type = 'rest-hook'
    subscription.channel.endpoint = 'https://your-webhook/endpoint'
    subscription.channel.header = ['Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8']

    FHIR::Subscription.create(subscription)
  end
end
