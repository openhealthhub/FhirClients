using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace DotNetOhh
{
        public class ApiKeyMessageHandler : HttpClientHandler
        {
                private readonly string _token;

                public ApiKeyMessageHandler()
                {
                        _token = authenticate()["access_token"];
                }
                protected async override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
                {
                        request.Headers.Add("x-api-key", "ad880601-b7e6-4d86-901d-b6fca96fc725");
                        request.Headers.Add("Authorization", $"Bearer {_token}");
                        return await base.SendAsync(request, cancellationToken);
                }
                
                private static Dictionary<string, string> authenticate()
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
                        return JsonConvert.DeserializeObject<Dictionary<string, string>>(
                                response.Content.ReadAsStringAsync().Result);
                }
        }
}