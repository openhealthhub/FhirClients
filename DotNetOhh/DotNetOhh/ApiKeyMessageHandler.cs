public class ApiKeyMessageHandler : HttpClientHandler
{
        protected async override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
                request.Headers.add("x-api-key", "90da723b-70f7-400b-8ddb-8574c45bff13");
                return await base.SendAsync(request, cancellationToken);
        }
}
