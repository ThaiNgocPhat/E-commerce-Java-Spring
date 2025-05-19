package manager.ecommerce.service;

import jakarta.mail.MessagingException;
import manager.ecommerce.dto.MessageResponse;
import manager.ecommerce.dto.ResponseWrapper;
import manager.ecommerce.dto.req.FormLogin;
import manager.ecommerce.dto.req.OtpDto;
import manager.ecommerce.dto.req.RegisterDto;
import manager.ecommerce.dto.req.ResendOtpDto;
import manager.ecommerce.dto.res.JwtResponse;

public interface IAuthService {
    ResponseWrapper<MessageResponse> register(RegisterDto req) throws MessagingException;
    ResponseWrapper<MessageResponse> verify(OtpDto otp);
    ResponseWrapper<MessageResponse> resendOtp(ResendOtpDto req) throws MessagingException;
    ResponseWrapper<JwtResponse> login (FormLogin req);
}
