package manager.ecommerce.exception;

public class HttpInternalServerError extends RuntimeException{
    public HttpInternalServerError(String message) {
        super(message);
    }
}
