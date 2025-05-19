package manager.ecommerce.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO gửi lại mã OTP qua username")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResendOtpDto {

    @Schema(description = "Tên người dùng", example = "phat123")
    @NotBlank(message = "Username is required")
    private String username;
}

