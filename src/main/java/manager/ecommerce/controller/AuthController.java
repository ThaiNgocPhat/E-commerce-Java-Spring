package manager.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import manager.ecommerce.dto.MessageResponse;
import manager.ecommerce.dto.ResponseWrapper;
import manager.ecommerce.dto.req.FormLogin;
import manager.ecommerce.dto.req.OtpDto;
import manager.ecommerce.dto.req.RegisterDto;
import manager.ecommerce.dto.req.ResendOtpDto;
import manager.ecommerce.dto.res.JwtResponse;
import manager.ecommerce.service.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API liên quan đến đăng ký, đăng nhập, xác thực OTP")
public class AuthController {

    private final IAuthService authService;

    @Operation(summary = "Đăng ký tài khoản", description = "Đăng ký tài khoản mới, gửi OTP đến email")
    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper<MessageResponse>> register(@Valid @ModelAttribute RegisterDto req) throws MessagingException {
        ResponseWrapper<MessageResponse> response = authService.register(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Xác thực OTP", description = "Nhập mã OTP đã gửi để xác thực email")
    @PostMapping("/verify")
    public ResponseEntity<ResponseWrapper<MessageResponse>> verify(@Valid @RequestBody OtpDto otp) {
        ResponseWrapper<MessageResponse> response = authService.verify(otp);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Gửi lại OTP", description = "Gửi lại mã OTP mới tới email đã đăng ký")
    @PostMapping("/resend-otp")
    public ResponseEntity<ResponseWrapper<MessageResponse>> resendOtp(@RequestBody ResendOtpDto req) throws MessagingException {
        ResponseWrapper<MessageResponse> response = authService.resendOtp(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Đăng nhập", description = "Đăng nhập hệ thống bằng email và mật khẩu")
    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<JwtResponse>> login(@Valid @RequestBody FormLogin req) {
        ResponseWrapper<JwtResponse> response = authService.login(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

