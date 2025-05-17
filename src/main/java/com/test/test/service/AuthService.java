package com.test.test.service;

import com.test.test.dto.AuthRespon;
import com.test.test.dto.RegisterRequest;
import com.test.test.dto.SingUpDto;
import com.test.test.entity.CustomUserDetails;
import com.test.test.entity.Role;
import com.test.test.entity.User;
import com.test.test.exeption.BusinessException;
import com.test.test.repository.Repository;
import com.test.test.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final Repository userRepository;
    private final OtpService otpService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       Repository userRepository,
                       OtpService otpService
                    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.otpService = otpService;

    }

    public AuthRespon login(SingUpDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            // هر استثنایی از نوع authentication مثل BadCredentialsException یا UsernameNotFoundException
            throw new BusinessException("user.not.found","نام کاربری یا رمز عبور اشتباه است",HttpStatus.NOT_FOUND );
        }

        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("user.not.found","نام کاربری یا رمز عبور اشتباه است",HttpStatus.NOT_FOUND );

        }
        if (user.getRole() != Role.PATIENT) {
            String token=jwtService.generateToken(user);
            return AuthRespon.builder().token(token).build();
        }
        otpService.sendOtp(user.getPhone());
        return AuthRespon.builder().data(null).build();
    }





    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new BusinessException("user.exists", "Username already exists",HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setRole(Role.PATIENT);

        userRepository.save(user);
    }
}
