package manager.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseError {
    private int code;
    private HttpStatus message;
    private Object details;
}
