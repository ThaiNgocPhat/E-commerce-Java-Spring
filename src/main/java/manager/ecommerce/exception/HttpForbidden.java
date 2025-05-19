package manager.ecommerce.exception;

public class HttpForbidden extends RuntimeException
{
    public HttpForbidden(String message)
    {
        super(message);
    }
}
