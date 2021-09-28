using System.Collections.Generic;
using System.Net.Http;
using Newtonsoft.Json;

namespace DotNetOhh
{
    public class AuthenticationUtil
    {
        public static string authenticate()
        {
            const string authUrl =
                "https://auth.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token";
            IEnumerable<KeyValuePair<string, string>> auth = new[]
            {
                new KeyValuePair<string, string>("client_id", "api-sandbox"),
                new KeyValuePair<string, string>("client_secret", "95810e52-4307-41f5-99a4-d873ab63b536"),
                new KeyValuePair<string, string>("grant_type", "client_credentials")
            };
            HttpClient http = new HttpClient();
            HttpContent content = new FormUrlEncodedContent(auth);
            HttpResponseMessage response = http.PostAsync(authUrl, content).Result;
            var respBody = JsonConvert.DeserializeObject<Dictionary<string, string>>(
                response.Content.ReadAsStringAsync().Result);
            return respBody["access_token"];
        }
    }
}
