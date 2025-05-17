package com.test.test.controller;

import com.test.test.dto.*;
import com.test.test.entity.Status;
import com.test.test.service.SecretaryService;
import com.test.test.service.Visitservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/secretary")
@PreAuthorize("hasAuthority('NURSE')")
public class ControllerSecretary {

    private Visitservice visitservice;

    private SecretaryService secretaryService;
    @Autowired
    public ControllerSecretary(Visitservice visitservice, SecretaryService secretaryService) {
        this.visitservice = visitservice;
        this.secretaryService = secretaryService;
    }

    @GetMapping("/visit")
    public SuccessResponse<List<AllVisitResponse>> entityVisits() {
        return new SuccessResponse<>(secretaryService.returnAllVisit());
    }

    @GetMapping("/visit/{visitid}")
    public SuccessResponse<VisitDto> userVisti(@PathVariable int visitid) {
        return new SuccessResponse<>(secretaryService.userVisitById(visitid));
    }

    @PostMapping("/visit/{visitid}/defect")
    public SuccessResponse<DefectDto> defect(@PathVariable int visitid, @RequestBody DefectDto defectDto) {

        return new SuccessResponse<>(secretaryService.defect(visitid, defectDto));
    }

    @PostMapping("/visit/{visitid}/ok")
    public SuccessResponse<Void> treatment(@PathVariable int visitid) {
        secretaryService.treatment(visitid);
        return new SuccessResponse<>(null);
    }

    @GetMapping("/visit/{visitid}/treatment")
    public SuccessResponse<SecBodyDto> treatmentReview(@PathVariable int visitid) {
        return new SuccessResponse<>(secretaryService.treatmentReview(visitid));
    }

    @PostMapping("/visit/{visitid}/treatment/send")
    public SuccessResponse<Void> designReviewSend(@PathVariable int visitid, @RequestBody SecBodyDto secBodyDto) {
        secretaryService.designReviewSend(visitid, secBodyDto);
        return new SuccessResponse<>(null);
    }

    @GetMapping("/visit/{visitid}/turn")
    public SuccessResponse<TurnResponDto> trun(@PathVariable int visitid) {
        return new SuccessResponse<>(secretaryService.turn(visitid));
    }

    @GetMapping("/visit/turn/by-date")
    public SuccessResponse<List<DateResponDto>> getVisitsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DateResponDto> dateResponDtos = secretaryService.getVisitsByDate(date);
        return new SuccessResponse<>(dateResponDtos);
    }

    @PostMapping("/visit/{visitid}/turn/registration")
    public SuccessResponse<Void> trunRegistration(@PathVariable int visitid, @RequestBody TurnDateDto turnDateDto) {
        secretaryService.trunRegistration(visitid, turnDateDto);
        return new SuccessResponse<>(null);
    }

    @GetMapping("/weekly-report")
    public SuccessResponse<List<VisitCountByDayDTO>> getWeeklyReport(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<VisitCountByDayDTO> result = visitservice.getWeeklyVisitCounts(date);
        return new SuccessResponse<>(result);
    }
}
