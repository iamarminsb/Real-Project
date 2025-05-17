package com.test.test.controller;

import com.test.test.dto.*;
import com.test.test.service.DockterService;
import com.test.test.service.Visitservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('DOCTOR')")
public class ControllerDocter {
    private final Visitservice visitservice;

    private final DockterService dockterService;
    @Autowired
    public ControllerDocter(Visitservice visitservice, DockterService dockterService) {
        this.visitservice = visitservice;
        this.dockterService = dockterService;
    }

    @GetMapping("/docter")
    public ResponseEntity<SuccessResponse<List<AllVisitResponse>>> entityVisits() {
        return ResponseEntity.ok(new SuccessResponse<>(dockterService.returnAllVisitChecked()));
    }

    @GetMapping("/docter/{visitid}")
    public ResponseEntity<SuccessResponse<DocDto>> userVisit(@PathVariable int visitid) {
        return ResponseEntity.ok(new SuccessResponse<>(dockterService.userVisit(visitid)));
    }

    @PostMapping("/docter/{visitid}/treatment")
    public ResponseEntity<SuccessResponse<TreatmentDto>> treatment(@PathVariable int visitid,@Valid @RequestBody TreatmentDto treatmentDto) {
        return ResponseEntity.ok(new SuccessResponse<>(dockterService.returnTeethStatus(visitid, treatmentDto)));
    }

    @GetMapping("/docter/{visitid}/get-treatment")
    public SuccessResponse<List<SecDto>> getTreatment(@PathVariable int visitid) {
        return new SuccessResponse<>(dockterService.getTreatment(visitid));
    }

    @DeleteMapping("/docter/{visitid}/delete_treatment")
    public ResponseEntity<SuccessResponse<Void>> deleteTreatment(@PathVariable int visitid,@Valid @RequestBody DeleteTreatmentDto DeleteTreatmentDto) {
        dockterService.deleteTreatment(visitid,DeleteTreatmentDto);
        return ResponseEntity.ok(new SuccessResponse<>(null));
    }

    @PostMapping("/docter/{visitid}/registration")
    public ResponseEntity<SuccessResponse<Void>> registration(@PathVariable int visitid, @RequestBody TreatmentDto treatmentDto) {
        dockterService.registration(visitid, treatmentDto);
        return ResponseEntity.ok(new SuccessResponse<>(null));
    }

    @GetMapping("/weekly-report")
    public ResponseEntity<SuccessResponse<List<VisitCountByDayDTO>>> getWeeklyReport(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<VisitCountByDayDTO> result = visitservice.getWeeklyVisitCounts(date);
        return ResponseEntity.ok(new SuccessResponse<>(result));
    }
}
