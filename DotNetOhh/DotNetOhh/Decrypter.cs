using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Hl7.Fhir.Model;
using Newtonsoft.Json.Linq;
using nsoftware.IPWorksOpenPGP;

namespace DotNetOhh
{
    public class Decrypter
    {
        private static readonly string EncryptedAnswersUrl =
            "http://openhealthhub.com/StructureDefinition/encryptedAnswers";

        private static readonly string PgpPassphrase = "api-sandbox";

        public void Decrypt(QuestionnaireResponse qr)
        {
            var encryptedAnswers = qr.GetStringExtension(EncryptedAnswersUrl);
            var privateKey = File.ReadAllBytes(Path.Combine(Directory.GetCurrentDirectory(), "sandbox.key"));

            var pgp = new Openpgp();
            pgp.Keys.Add(new Key(privateKey));
            pgp.Keys[0].Passphrase = PgpPassphrase;
            pgp.InputMessage = encryptedAnswers;
            pgp.Decrypt();

            JObject decryptedAnswers = JObject.Parse(pgp.OutputMessage);

            var itemComponents = GetNestedItems(qr.Item);

            itemComponents.ForEach(component => SetAnswers(component, decryptedAnswers));
        }

        private static List<QuestionnaireResponse.ItemComponent> GetNestedItems(
            List<QuestionnaireResponse.ItemComponent> items)
        {
            var nestedItems = items.SelectMany(item =>
            {
                if (item.Item.Count == 0)
                {
                    return Enumerable.Empty<QuestionnaireResponse.ItemComponent>();
                }

                return GetNestedItems(item.Item);
            });
            return items.Concat(nestedItems).ToList();
        }

        private static void SetAnswers(QuestionnaireResponse.ItemComponent component, JObject decryptedAnswers)
        {
            var jsonArray = decryptedAnswers[component.LinkId] as JArray;
            for (int i = 0; i < jsonArray?.Count; i++)
            {
                SetAnswer(component.Answer[i], jsonArray?[i].ToString());
            }
        }

        private static void SetAnswer(QuestionnaireResponse.AnswerComponent answer, string answerValue)
        {
            var value = answer.Value;

            if (value is PrimitiveType p)
            {
                p.ObjectValue = answerValue;
            }

            if (value is Attachment attachment)
            {
                attachment.Data = Convert.FromBase64String(answerValue);
            }

            if (value is Coding coding)
            {
                coding.Code = answerValue;
            }
        }
    }
}