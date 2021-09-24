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
                "https://auth-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token";
            IEnumerable<KeyValuePair<string, string>> auth = new[]
            {
                new KeyValuePair<string, string>("client_id", "api-sandbox"),
                new KeyValuePair<string, string>("client_secret", "915e87d4-16ee-4ca5-b701-b38b6afce8ff"),
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