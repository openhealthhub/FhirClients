public class ApiKeyMessageHandler : HttpClientHandler
{
        protected async override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
                request.Headers.add("x-api-key", "ad880601-b7e6-4d86-901d-b6fca96fc725");
                return await base.SendAsync(request, cancellationToken);
        }
}
