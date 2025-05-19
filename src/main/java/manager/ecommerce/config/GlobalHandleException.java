package manager.ecommerce.config;

import manager.ecommerce.dto.ResponseError;
import manager.ecommerce.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(HttpBadRequest.class)
    public ResponseEntity<ResponseError> handleBadRequest (HttpBadRequest e){
        return new ResponseEntity<>(new ResponseError(400,HttpStatus.BAD_REQUEST,e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpConflict.class)
    public ResponseEntity<ResponseError> handleConflict (HttpConflict e){
        return new ResponseEntity<>(new ResponseError(409,HttpStatus.CONFLICT,e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpForbidden.class)
    public ResponseEntity<ResponseError> handleForbidden (HttpForbidden e){
        return new ResponseEntity<>(new ResponseError(403,HttpStatus.FORBIDDEN,e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpNotFound.class)
    public ResponseEntity<ResponseError> handleNotFound (HttpNotFound e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpUnAuthorized.class)
    public ResponseEntity<ResponseError> handleUnAuthorized (HttpUnAuthorized e){
        return new ResponseEntity<>(new ResponseError(401,HttpStatus.UNAUTHORIZED,e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpInternalServerError.class)
    public ResponseEntity<ResponseError> handleInternalServerError (HttpInternalServerError e){
        return new ResponseEntity<>(new ResponseError(500,HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
