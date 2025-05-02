﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using Hl7.Fhir.Model;
using Hl7.Fhir.Rest;
using Hl7.Fhir.Serialization;
using Newtonsoft.Json;

namespace DotNetOhh
{
    internal class Program
    {
        const string FhirUrl = "https://api.openhealthhub.com/OpenHealthhub/fhir-sandbox/4/";

        public static void Main(string[] args)
        {
            var settings = new FhirClientSettings
            {
                PreferredFormat = ResourceFormat.Json,
                VerifyFhirVersion = true
            };


            var client = new FhirClient(FhirUrl, settings, new ApiKeyMessageHandler());

            CarePlan(client);

            CareTeam(client);

            Practitioners(client);

            CreateSubscription(client);

            ReadAndDecryptQuestionnaireResponse(client);

            ReadQuestionnaire(client);

            UploadKey();
        }

        private static void UploadKey()
        {
            HttpClient http = new HttpClient();

            var publicKey = File.ReadAllBytes(Path.Combine(Directory.GetCurrentDirectory(), "sandbox.pub"));
            var encodedKey = System.Convert.ToBase64String(publicKey);
            var content = new ByteArrayContent(Encoding.ASCII.GetBytes(encodedKey));
            content.Headers.ContentType = MediaTypeHeaderValue.Parse("text/plain");

            http.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer",
                AuthenticationUtil.authenticate());
            http.DefaultRequestHeaders.Add(ApiKeyMessageHandler.ApiKeyHeader, ApiKeyMessageHandler.ApiKey);

            HttpResponseMessage response = http.PostAsync(FhirUrl + "/Binary", content).Result;
            Console.Out.WriteLine(response.StatusCode);
        }

        private static void CarePlan(FhirClient client)
        {
            var patient = GetPatient();
            var carePlan = new CarePlan
            {
                Period = new Period(FhirDateTime.Now(), null),
                Contained = new List<Resource> {patient},
                Subject = new ResourceReference("#patient"),
                InstantiatesCanonical = new List<string> {"PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6"}
            };

            var findPlan = client.Read<CarePlan>("CarePlan/1");

            Console.Out.WriteLine(findPlan.InstantiatesCanonical.First());

            var searchPlan = client.Search<CarePlan>(new[]
            {
                "instantiates-canonical=PlanDefinition/4944e73f-e447-49ba-a64c-a246b9ef4bdd", "patient.identifier=1234"
            });

            searchPlan.Entry.ForEach(cp => Console.WriteLine(cp.FullUrl));

            var planWithPractitioners = client.Search<CarePlan>(new[]
                {"_id=1", "_include=CarePlan:care-team", "_include=CareTeam:participant"});

            planWithPractitioners.Entry.ForEach(cp => Console.WriteLine(cp.FullUrl));

            var plan = client.Create(carePlan);

            Console.Out.WriteLine(plan.InstantiatesCanonical.First());

            carePlan.Id = "1";
            var updatedPlan = client.Update(carePlan);

            Console.Out.WriteLine(updatedPlan.InstantiatesCanonical.First());

            client.Delete("CarePlan/1");
        }

        private static void CareTeam(FhirClient client)
        {
            var findTeam = client.Read<CarePlan>("CareTeam/1");

            Console.Out.WriteLine(findTeam.InstantiatesCanonical.First());

            var searchTeam = client.Search<CareTeam>(new[] {"_id=1", "_include=CareTeam:participant"});

            searchTeam.Entry.ForEach(cp => Console.WriteLine(cp.FullUrl));
        }

        private static void Practitioners(FhirClient client)
        {
            var practitioner = client.Read<Practitioner>("Practitioner/1");

            Console.Out.WriteLine(practitioner);

            var searchPractitioner = client.Search<Practitioner>(new[] {"_has:CareTeam:_id=1"});

            searchPractitioner.Entry.ForEach(cp => Console.WriteLine(cp.FullUrl));
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

        private static Patient GetPatient()
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
            return patient;
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
                {"based-on.instantiates-canonical=PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806"});
            enc.Entry.ForEach(component => Console.WriteLine(component.FullUrl));
        }

        private static void CreateSubscription(FhirClient client)
        {
            var resource = new Subscription
            {
                Status = Subscription.SubscriptionStatus.Requested,
                Criteria = "QuestionnaireResponse",
                Channel = new Subscription.ChannelComponent()
                {
                    Type = Subscription.SubscriptionChannelType.RestHook,
                    Header = new List<string>
                        {"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"}
                }
            };
            var subscription = client.Create(resource);
            Console.Out.WriteLine(subscription.Id);
        }

        private static void Batch(FhirClient client)
        {
                Bundle bundle = new Bundle
                {
                    Type = Bundle.BundleType.Batch,
                    Entry = new List<Bundle.EntryComponent>()
                };

                bundle.Entry.Add(new Bundle.EntryComponent
                {
                    Resource = new CarePlan
                    {
                       Period = new Period(FhirDateTime.Now(), null),
                       InstantiatesCanonical = new List<string> { "PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6" }
                    },
                    Request = new Bundle.RequestComponent
                    {
                        Method = Bundle.HTTPVerb.POST,
                        Url = "CarePlan"
                    }
                });

                bundle.Entry.Add(new Bundle.EntryComponent
                {
                    Request = new Bundle.RequestComponent
                    {
                        Method = Bundle.HTTPVerb.GET,
                        Url = "CarePlan/1"
                    }
                });

                var result = client.Transaction(bundle);
        }
    }
}
