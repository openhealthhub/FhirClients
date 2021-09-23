using System;
using System.Collections.Generic;
using System.Net.Http;
using Hl7.Fhir.Model;
using Hl7.Fhir.Rest;
using Hl7.Fhir.Serialization;
using Newtonsoft.Json;

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


            var client = new FhirClient("https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir-sandbox/4/",
                settings, new ApiKeyMessageHandler());

            ReadObservation(client);

            CreateSubscription(client);

            ReadAndDecryptQuestionnaireResponse(client);

            CreateAppointment(client);

            ReadQuestionnaire(client);

            ReadPlanDefinition(client);
        }

        private static void ReadPlanDefinition(FhirClient client)
        {
            var planDefinition = client.Read<PlanDefinition>("PlanDefinition/4944e73f-e447-49ba-a64c-a246b9ef4bdd");
            Console.Out.WriteLine(planDefinition.Description);
            
            var searchDefinition = client.Search<PlanDefinition>(new[]
                {"publisher=Program Creator", "definition=Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d"});
            searchDefinition.Entry.ForEach(component => Console.WriteLine(component.FullUrl));
        }

        private static void ReadQuestionnaire(FhirClient client)
        {
            var questionnaire = client.Read<Questionnaire>("Questionnaire/1");
            Console.Out.WriteLine(questionnaire.Description);
        }

        private static void CreateAppointment(FhirClient client)
        {
            var patient = new Patient
            {
                Id = "patient",
                Identifier = new List<Identifier>
                {
                    new Identifier
                    {
                        System = "urn:oid:2.16.840.1.113883.2.4.99",
                        Value = "1234"
                    }
                },
                Name = new List<HumanName> {new HumanName {Text = "Test Patient"}},
                Telecom = new List<ContactPoint>
                {
                    new ContactPoint
                    {
                        System = ContactPoint.ContactPointSystem.Email,
                        Value = "test@patient.ohh"
                    }
                }
            };

            var app = new Appointment
            {
                Start = DateTimeOffset.Now,
                Contained = new List<Resource> {patient},
                Participant = new List<Appointment.ParticipantComponent>
                {
                    new Appointment.ParticipantComponent
                    {
                        Actor = new ResourceReference {Reference = "#patient"},
                        Status = ParticipationStatus.NeedsAction
                    }
                },
                SupportingInformation = new List<ResourceReference>
                    {new ResourceReference {Reference = "PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6"}},
                Extension = new List<Extension>
                {
                    new Extension
                    {
                        Url = "http://openhealthhub.com/fhir/StructureDefinition/appointment-pin",
                        Value = new FhirString("59gladtc")
                    }
                }
            };

            var a = client.Create(app);

            Console.Out.WriteLine(a.Description);
        }

        private static void ReadAndDecryptQuestionnaireResponse(FhirClient client)
        {
            var qr = client.Read<QuestionnaireResponse>("QuestionnaireResponse/57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca");
            new Decrypter().Decrypt(qr);

            qr.Item.ForEach(item =>
            {
                Console.WriteLine(item.LinkId);
                item.Answer.ForEach(answer => Console.WriteLine(answer.Value.ToString()));
            });

            var enc = client.Search<QuestionnaireResponse>(new[]
                {"patient.identifier=6226217e", "based-on=PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806"});
            enc.Entry.ForEach(component => Console.WriteLine(component.FullUrl));
        }

        private static void CreateSubscription(FhirClient client)
        {
            var resource = new Subscription
            {
                Status = Subscription.SubscriptionStatus.Requested,
                Criteria = "QuestionnaireResponse",
                Channel = new Subscription.ChannelComponent() {Type = Subscription.SubscriptionChannelType.RestHook}
            };
            var subscription = client.Create(resource);
            Console.Out.WriteLine(subscription.Id);
        }

        private static void ReadObservation(FhirClient client)
        {
            var obs = client.Read<Observation>("Observation/1");
            Console.Out.WriteLine(obs.Category[0].Text);


            var bun = client.Search<Observation>(new[] {"identifier=patientnumber", "device-name=blub"});

            bun.Entry.ForEach(component => Console.WriteLine(component.ToJson()));
        }
    }
}
