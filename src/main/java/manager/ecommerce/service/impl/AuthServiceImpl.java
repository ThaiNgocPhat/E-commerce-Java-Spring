package manager.ecommerce.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import manager.ecommerce.dto.MessageResponse;
import manager.ecommerce.dto.ResponseWrapper;
import manager.ecommerce.dto.req.FormLogin;
import manager.ecommerce.dto.req.OtpDto;
import manager.ecommerce.dto.req.RegisterDto;
import manager.ecommerce.dto.req.ResendOtpDto;
import manager.ecommerce.dto.res.JwtResponse;
import manager.ecommerce.entity.Role;
import manager.ecommerce.entity.User;
import manager.ecommerce.entity.contants.RoleEnum;
import manager.ecommerce.exception.HttpBadRequest;
import manager.ecommerce.exception.HttpConflict;
import manager.ecommerce.exception.HttpInternalServerError;
import manager.ecommerce.exception.HttpNotFound;
import manager.ecommerce.repository.IRoleRepository;
import manager.ecommerce.security.jwt.JwtProvider;
import manager.ecommerce.service.IAuthService;
import manager.ecommerce.util.MailService;
import manager.ecommerce.util.UploadService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import manager.ecommerce.repository.IUserRepository;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UploadService uploadService;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MailService mailService;

    @Override
    public ResponseWrapper<MessageResponse> register(RegisterDto req) throws MessagingException {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new HttpConflict("Username already exists");
        }

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new HttpConflict("Email already exists");
        }

        if (!req.getConfirmPassword().equals(req.getPassword())) {
            throw new HttpBadRequest("Password and confirm password do not match");
        }

        String avatar = "DEFAULT_AVATAR";
        if (req.getAvatar() != null && !req.getAvatar().isEmpty()) {
            try {
                avatar = uploadService.uploadFileToDrive(req.getAvatar());
            } catch (IOException e) {
                throw new HttpInternalServerError("Error while uploading avatar");
            }
        }

        String otp = jwtProvider.generateOTP();

        Role role = roleRepository.findByRoleName(RoleEnum.USER)
                .orElseThrow(() -> new HttpNotFound("Role not found"));

        User user = modelMapper.map(req, User.class);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setAvatar(avatar);
        user.setRoles(Set.of(role));
        user.setOtp(otp);
        user.setIsVerified(false);
        user.setIsDeleted(false);
        user.setStatus(false);
        user.setIsTimeExpired(LocalDateTime.now());

        userRepository.save(user);

        mailService.sendOTPEmail(
                user.getEmail(),
                user.getUsername(),
                user.getEmail(),
                otp
        );

        ResponseWrapper<MessageResponse> response = new ResponseWrapper<>();
        response.setCode(201);
        response.setStatus(HttpStatus.CREATED);
        response.setData(new MessageResponse("Register successfully. Please check your email for OTP verification."));
        return response;
    }

    @Override
    public ResponseWrapper<MessageResponse> verify(OtpDto otpDto) {
        if (otpDto.getOtp() == null || otpDto.getOtp().trim().isEmpty()) {
            throw new HttpBadRequest("OTP must not be empty");
        }

        User user = userRepository.findByOtpAndIsVerifiedFalse(otpDto.getOtp())
                .orElseThrow(() -> new HttpBadRequest("Invalid OTP"));
        LocalDateTime now = LocalDateTime.now();
        if (user.getIsTimeExpired() == null || Duration.between(user.getIsTimeExpired(), now).toMinutes() > 5) {
            throw new HttpBadRequest("OTP has expired. Please request a new one.");
        }

        user.setIsVerified(true);
        user.setStatus(true);
        user.setOtp(null);
        user.setIsTimeExpired(null);
        userRepository.save(user);

        ResponseWrapper<MessageResponse> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(new MessageResponse("OTP verified successfully"));
        return response;
    }
    public ResponseWrapper<MessageResponse> resendOtp(ResendOtpDto req) throws MessagingException {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new HttpNotFound("User not found"));

        if (user.getIsVerified()) {
            throw new HttpBadRequest("User is already verified");
        }

        String newOtp = jwtProvider.generateOTP();
        user.setOtp(newOtp);
        user.setIsTimeExpired(LocalDateTime.now());
        userRepository.save(user);

        mailService.sendOTPEmail(
                user.getEmail(),
                user.getEmail(),
                "Resend OTP",
                newOtp
        );

        ResponseWrapper<MessageResponse> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(new MessageResponse("OTP sent successfully"));
        return response;
    }

    @Override
    public ResponseWrapper<JwtResponse> login(FormLogin req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new HttpBadRequest("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new HttpBadRequest("Wrong password");
        }

        if (user.getStatus() == null || !user.getStatus()) {
            throw new HttpBadRequest("Account is inactive");
        }

        if (user.getIsVerified() == null || !user.getIsVerified()) {
            throw new HttpBadRequest("Account is not verified");
        }

        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            throw new HttpBadRequest("User has no roles");
        }

        String token = jwtProvider.generateAccessToken(user.getUsername(), roles);

        JwtResponse response = new JwtResponse();
        response.setToken(token);

        ResponseWrapper<JwtResponse> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setStatus(HttpStatus.OK);
        responseWrapper.setCode(200);
        responseWrapper.setData(response);

        return responseWrapper;
    }
}
