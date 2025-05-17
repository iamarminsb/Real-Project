package com.test.test.service;

import com.test.test.dto.AuthRespon;
import com.test.test.dto.OtpDto;
import com.test.test.dto.RegisterRequest;
import com.test.test.entity.OtpCode;
import com.test.test.entity.Role;
import com.test.test.entity.User;
import com.test.test.exeption.BusinessException;
import com.test.test.repository.OtpRepository;
import com.test.test.repository.Repository;
import com.test.test.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    private final OtpRepository otpRepository;

    private final Repository userRepository;

    private final JwtService jwtService;

    private final SmsService smsService;
    @Autowired
    public OtpService(OtpRepository otpRepository, Repository userRepository, JwtService jwtService, SmsService smsService) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.smsService = smsService;
    }

    public void sendOtp(String phone) {
        if (otpRepository.existsByPhoneAndSentAtAfter(phone, LocalDateTime.now().minusSeconds(60))) {
            throw new RuntimeException("ارسال بیش از حد مجاز");
        }

        String code = String.valueOf(new Random().nextInt(900_000) + 100_000);

        OtpCode otp = OtpCode.builder()
                .phone(phone)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .sentAt(LocalDateTime.now())
                .used(false)
                .build();

        otpRepository.save(otp);

        smsService.sendOtpSms(phone, code);
    }

    public AuthRespon verifyOtp(OtpDto otpDto) {
        OtpCode otp = otpRepository.findFirstByPhoneAndCodeAndUsedFalseOrderByExpiresAtDesc(otpDto.getPhone(), otpDto.getCode())
                .orElseThrow(() -> new BusinessException("otp.not.correct","کد اشتباه است",HttpStatus.BAD_REQUEST));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("otp.expired","کد منقضی شده است",HttpStatus.BAD_REQUEST);
        }

        otp.setUsed(true);
        otpRepository.save(otp);

       User user=userRepository.findByUsername(otpDto.getUsername());

        String token=jwtService.generateToken(user);
        return AuthRespon.builder().token(token).build();

    }
}
