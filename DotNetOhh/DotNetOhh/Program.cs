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
            var questionnaire = client.Read<Questionnaire>("Questionnaire/1");
            
            Console.Out.WriteLine(questionnaire.Description);
        }
    }
}