package manager.ecommerce.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO dùng để đăng ký tài khoản người dùng")
public class RegisterDto {

    @Schema(description = "Họ tên người dùng", example = "Nguyễn Văn A", required = true)
    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Schema(description = "Tên đăng nhập (username)", example = "nguyenvana", required = true)
    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Schema(description = "Email của người dùng", example = "nguyenvana@gmail.com", required = true)
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Mật khẩu (tối thiểu 6 ký tự)", example = "Abc123@", required = true)
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Xác nhận lại mật khẩu", example = "Abc123@", required = true)
    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;

    @Schema(description = "Số điện thoại (bắt đầu bằng +84 hoặc 0)", example = "0901234567", required = true)
    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^(\\+84|0)[1-9][0-9]{8}$", message = "Invalid phone number format")
    private String phone;

    @Schema(description = "Địa chỉ của người dùng", example = "123 Lê Lợi, Q.1, TP.HCM", required = true)
    @NotBlank(message = "Address must not be blank")
    private String address;

    @Schema(description = "Ảnh đại diện người dùng (dạng file)", type = "string", format = "binary")
    private MultipartFile avatar;

    @Schema(description = "Giới tính của người dùng", example = "Male", allowableValues = {"Male", "Female", "Other"}, required = true)
    @NotBlank(message = "Gender must not be blank")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Schema(description = "Ngày sinh (định dạng yyyy-MM-dd)", example = "2000-01-01", required = true)
    @NotBlank(message = "Birthday must not be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birthday must be in the format yyyy-MM-dd")
    private String birthday;
}

