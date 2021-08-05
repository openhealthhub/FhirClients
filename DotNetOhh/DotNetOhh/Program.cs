﻿using System;
using Hl7.Fhir.Model;
using Hl7.Fhir.Rest;
using Hl7.Fhir.Serialization;

namespace DotNetOhh
{
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
            
            ReadObservation(client);
            
            CreateSubscription(client);
            
            ReadAndDecryptQuestionnaireResponse(client);
            
            CreateAppointment(client);
            
            ReadQuestionnaire(client);
        }

        private static void ReadQuestionnaire(FhirClient client)
        {
            var questionnaire = client.Read<Questionnaire>("Questionnaire/1");
            Console.Out.WriteLine(questionnaire.Description);
        }

        private static void CreateAppointment(FhirClient client)
        {
            Appointment app = new Appointment
            {
                Status = Appointment.AppointmentStatus.Booked,
                AppointmentType = new CodeableConcept("http://terminology.hl7.org/CodeSystem/v2-0276","FOLLOWUP", "A follow up visit from a previous appointment"),
                Description = "Discussion on the results of your recent MRI",
                Start = DateTimeOffset.Now,
                End = DateTimeOffset.Now
            };
            var a = client.Create(app);
            
            Console.Out.WriteLine(a.Description);
        }

        private static void ReadAndDecryptQuestionnaireResponse(FhirClient client)
        {
            var qr = client.Read<QuestionnaireResponse>("QuestionnaireResponse/1");
            new Decrypter().Decrypt(qr);
            
            qr.Item.ForEach(item =>
            {
                Console.WriteLine(item.LinkId);
                item.Answer.ForEach(answer => Console.WriteLine(answer.Value.ToString()));
            });

            var enc = client.Search<QuestionnaireResponse>(new []{"identifier=patientnumber", "part-of=blub"});
            enc.Entry.ForEach(component => Console.WriteLine(component.FullUrl));
        }

        private static void CreateSubscription(FhirClient client)
        {
            var resource = new Subscription
            {
                Channel = new Subscription.ChannelComponent() {Type = Subscription.SubscriptionChannelType.RestHook}
            };
            var subscription = client.Create(resource);
            Console.Out.WriteLine(subscription.Id);
        }

        private static void ReadObservation(FhirClient client)
        {
            var obs = client.Read<Observation>("Observation/1");
            Console.Out.WriteLine(obs.Category[0].Text);
            
            var bun = client.Search<Observation>(new[]{"identifier=patientnumber"});
            
            bun.Entry.ForEach(component => Console.WriteLine(component.ToJson()));
        }
    }
}