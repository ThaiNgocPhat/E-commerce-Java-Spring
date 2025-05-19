package manager.ecommerce.exception;

public class HttpConflict extends RuntimeException
{
    public HttpConflict(String message)
    {
        super(message);
    }
}
