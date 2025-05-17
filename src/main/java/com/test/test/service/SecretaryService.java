package com.test.test.service;

import com.test.test.config.VisitValidator;
import com.test.test.dto.*;
import com.test.test.entity.EntityDefect;
import com.test.test.entity.EntityVisit;
import com.test.test.entity.Status;
import com.test.test.entity.TeethStatus;
import com.test.test.exeption.BusinessException;
import com.test.test.repository.DefectRepository;
import com.test.test.repository.TeethRepository;
import com.test.test.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SecretaryService {

    @Autowired
    VisitRepository visitRepository;
    @Autowired
    TeethRepository teethRepository;
    @Autowired
    DefectRepository defectRepository;
    public SecretaryService(VisitRepository visitRepository, TeethRepository teethRepository, DefectRepository defectRepository) {
        this.visitRepository = visitRepository;
        this.teethRepository = teethRepository;
        this.defectRepository = defectRepository;
    }

    public List<AllVisitResponse> returnAllVisit(){
        List<EntityVisit> entityVisits= visitRepository.findAll();
        return entityVisits.stream().map(entityVisit -> new AllVisitResponse(entityVisit.getUser().getUsername(),entityVisit.getStatus(),entityVisit.getId(),entityVisit.getUpdatedAt())).toList();
    }
    public VisitDto userVisitById(int id){
        EntityVisit entityVisit = visitRepository.findById(id).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        return new VisitDto(entityVisit.getDaromasrafi(),entityVisit.getSabeghe(),entityVisit.getElat(),entityVisit.getStatus(),entityVisit.getUser().getUsername());

    }

    @Transactional
    public DefectDto defect(int id,DefectDto defectDto){
        EntityDefect entityDefect=new EntityDefect();
        EntityVisit entityVisit = visitRepository.findById(id).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit,Status.CHECK_DOCUMENTS);
        entityDefect.setText(defectDto.getText());
        entityDefect.setVisit(entityVisit);
        entityVisit.setStatus(Status.DOCUMENT_DEFECTS);
        defectDto.setStatusText(Status.DOCUMENT_DEFECTS);
        defectDto.setCreatedAt(LocalDateTime.now());
        defectRepository.save(entityDefect);
        visitRepository.save(entityVisit);
        return defectDto;

    }
    @Transactional
    public void treatment(int id){
        EntityVisit entityVisit = visitRepository.findById(id).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit, Status.CHECK_DOCUMENTS);
        entityVisit.setStatus(Status.TREATMENT);
        visitRepository.save(entityVisit);

    }



    public SecBodyDto treatmentReview(int visitid) {
        EntityVisit entityVisit = visitRepository.findById(visitid).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit, Status.DESIGN_REVIEW);
        List<TeethStatus> teethStatuses=teethRepository.findByEntityVisit(entityVisit);
        SecBodyDto secBodyDto=new SecBodyDto();
        secBodyDto.setSecList(teethStatuses.stream().map(teethStatus -> new SecDto(teethStatus.getTeeth(),teethStatus.getNumberOfTeeths())).toList());
        secBodyDto.setDocText(entityVisit.getDocText());
        return secBodyDto;


    }



    public void designReviewSend(int visitid, SecBodyDto secBodyDto) {
        EntityVisit entityVisit = visitRepository.findById(visitid).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit, Status.DESIGN_REVIEW);
        entityVisit.setSecText(secBodyDto.getSecText());
        entityVisit.setStatus(Status.READY);
        visitRepository.save(entityVisit);


    }

    public TurnResponDto turn(int visitid) {
        EntityVisit entityVisit = visitRepository.findById(visitid).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit, Status.READY);
        List<TeethStatus> teethStatuses=teethRepository.findByEntityVisit(entityVisit);
        TurnResponDto turnResponDto=new TurnResponDto();
        turnResponDto.setUsername(entityVisit.getUser().getUsername());
        turnResponDto.setStatus(entityVisit.getStatus());
        turnResponDto.setDetails(teethStatuses.stream().map(teethStatus -> new SecDto(teethStatus.getTeeth(),teethStatus.getNumberOfTeeths())).toList());
        return turnResponDto;
    }

    public List<DateResponDto> getVisitsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<EntityVisit> entityVisits = visitRepository.findByScheduledAtBetween(startOfDay, endOfDay);

        return entityVisits.stream()
                .map(v -> new DateResponDto(v.getUser().getUsername(), v.getScheduledAt()))
                .toList();
    }

    public void trunRegistration(int visitid, TurnDateDto turnDateDto) {
        EntityVisit entityVisit = visitRepository.findById(visitid).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit, Status.READY);
        entityVisit.setScheduledAt(turnDateDto.getScheduledAt());
        entityVisit.setStatus(Status.WAITING);
        visitRepository.save(entityVisit);

    }
}
