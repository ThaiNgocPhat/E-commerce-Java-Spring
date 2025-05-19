package manager.ecommerce.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO chứa mã OTP xác thực")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpDto {

    @Schema(description = "Mã OTP", example = "123456")
    @NotBlank(message = "OTP is required")
    private String otp;
}

