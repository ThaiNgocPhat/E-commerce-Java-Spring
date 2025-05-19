package manager.ecommerce.exception;

public class HttpNotFound extends RuntimeException
{
    public HttpNotFound(String message)
    {
        super(message);
    }
}
