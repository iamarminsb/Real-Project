package com.test.test.controller;

import com.test.test.dto.*;
import com.test.test.service.AuthService;
import com.test.test.service.OtpService;
import com.test.test.service.Visitservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    private Visitservice visitservice;
    private AuthService authService;
    private OtpService otpService;

    @Autowired
    public Controller(Visitservice visitservice, AuthService authService, OtpService otpService) {
        this.visitservice = visitservice;
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<SuccessResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new SuccessResponse<>("ثبت ‌نام با موفقیت انجام شد."));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse<AuthRespon>> login(@RequestBody SingUpDto request) {
        return ResponseEntity.ok(new SuccessResponse<>(authService.login(request)));
    }

    @PostMapping("/auth/verify-otp")
    public ResponseEntity<SuccessResponse<AuthRespon>> login(@RequestBody OtpDto otpDto) {
        return ResponseEntity.ok(new SuccessResponse<>(otpService.verifyOtp(otpDto)));
    }

    @PostMapping("/visit")
    public ResponseEntity<SuccessResponse<VisitDto>> visit(@RequestBody VisitDto visitDto) {
        return ResponseEntity.ok(new SuccessResponse<>(visitservice.savevisit(visitDto)));
    }

    @GetMapping("/allvisit")
    public ResponseEntity<SuccessResponse<List<AllVisitResponse>>> getAllVisit() {
        return ResponseEntity.ok(new SuccessResponse<>(visitservice.getAllVisit()));
    }

    @GetMapping("/allvisit/{visitid}")
    public SuccessResponse<VisitResponDto> getVisit(@PathVariable int visitid) {
        return new SuccessResponse<>(visitservice.getVisit(visitid));

    }

    @GetMapping("/visit/status")
    public ResponseEntity<SuccessResponse<VisitResponDto>> visitStatus() {
        return ResponseEntity.ok(new SuccessResponse<>(visitservice.visitStatus()));
    }

//    @GetMapping("/visit/defect")
//    public ResponseEntity<SuccessResponse<DefectDto>> getDefect() {
//        return ResponseEntity.ok(new SuccessResponse<>(visitservice.getDefect()));
//    }

//    @GetMapping("/visit/doc-treatment")
//    public ResponseEntity<SuccessResponse<List<SecDto>>> visitView() {
//        return ResponseEntity.ok(new SuccessResponse<>(visitservice.visitView()));
//    }

    @PostMapping("/visit/status/request-turn")
    public ResponseEntity<SuccessResponse<String>> turn() {
        visitservice.visitTurn();
        return ResponseEntity.ok(new SuccessResponse<>("نوبت‌گیری با موفقیت انجام شد."));
    }

    @DeleteMapping("/visit/cancle")
    public ResponseEntity<SuccessResponse<String>> cancle() {
        visitservice.cancleVisit();
        return ResponseEntity.ok(new SuccessResponse<>("ویزیت با موفقیت لغو شد."));
    }

    @PutMapping("/visit/update")
    public ResponseEntity<SuccessResponse<VisitDto>> updateVisit(@RequestBody VisitDto visitDto) {
        return ResponseEntity.ok(new SuccessResponse<>(visitservice.updateVisit(visitDto)));
    }


//    @GetMapping("/visit/turn-date")
//    public ResponseEntity<SuccessResponse<TurnDateDto>> getTurnDate() {
//        return ResponseEntity.ok(new SuccessResponse<>(visitservice.getTurnDate()));
//    }

    @PostMapping("/visit/status/registration")
    public ResponseEntity<SuccessResponse<String>> registrationTurn() {
        visitservice.registrationTurn();
        return ResponseEntity.ok(new SuccessResponse<>("منتظر دیدار شما هستیم"));
    }
}