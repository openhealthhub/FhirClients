using System;
using Hl7.Fhir.Model;

namespace DotNetOhh
{
    using Hl7.Fhir.Rest;
    internal class Program
    {
        public static void Main(string[] args)
        {
            var settings = new FhirClientSettings
            {
                PreferredFormat = ResourceFormat.Json,
                VerifyFhirVersion = true
            };

            var client = new FhirClient("https://api-sandbox-staging.openhealthhub.com/fhir/", settings);
            
            var obs = client.Read<Observation>("Observation/1");
            Console.Out.WriteLine(obs.Category[0].Text);

            var resource = new Subscription
            {
                Channel = new Subscription.ChannelComponent(){Type = Subscription.SubscriptionChannelType.RestHook}
            };
            var subscription = client.Create(resource);
            
            Console.Out.WriteLine(subscription.Id);

            var qr = client.Read<QuestionnaireResponse>("QuestionnaireResponse/1");
            new Decrypter().Decrypt(qr);
        }
    }
}