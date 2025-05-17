package com.test.test.service;

import com.test.test.exeption.BusinessException;
import com.test.test.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SmsService {
    @Autowired
    private OtpRepository otpRepository;


    @Value("${kavenegar.apikey}")
    private String apiKey;

    @Value("${kavenegar.sender}")
    private String sender;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtpSms(String phone, String code) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay(); // شروع امروز

        long sentCount = otpRepository.countByPhoneAndSentAtAfter(phone, todayStart);

        if (sentCount >= 5) {
            throw new BusinessException(
                    "otp.limit.exceeded",
                    "شما حداکثر تعداد مجاز ارسال کد در روز را انجام داده‌اید.",
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }

        String url = String.format("https://api.kavenegar.com/v1/%s/sms/send.json", apiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("receptor", phone)
                .queryParam("sender", sender)
                .queryParam("message", "کد ورود شما: " + code);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("ارسال پیامک با خطا مواجه شد: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("خطا در ارسال پیامک: " + e.getMessage(), e);
        }
    }
}
