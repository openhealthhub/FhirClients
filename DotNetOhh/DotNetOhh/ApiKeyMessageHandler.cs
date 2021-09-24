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
        public const string ApiKey = "ad880601-b7e6-4d86-901d-b6fca96fc725";
        public const string ApiKeyHeader = "x-api-key";

        private readonly string _token;

        public ApiKeyMessageHandler()
        {
            _token = AuthenticationUtil.authenticate();
        }

        protected async override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,
            CancellationToken cancellationToken)
        {
            request.Headers.Add(ApiKeyHeader, ApiKey);
            request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", _token);
            return await base.SendAsync(request, cancellationToken);
        }
    }
}