package manager.ecommerce.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Thông tin đăng nhập")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FormLogin {

    @Schema(description = "Tên người dùng", example = "phat123")
    @NotBlank(message = "Username không được để trống")
    private String username;

    @Schema(description = "Mật khẩu", example = "123456")
    @NotBlank(message = "Password không được để trống")
    private String password;
}

