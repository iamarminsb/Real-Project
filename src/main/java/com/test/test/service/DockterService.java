package com.test.test.service;

import com.test.test.config.RequireAuth;
import com.test.test.config.VisitValidator;
import com.test.test.dto.*;
import com.test.test.entity.EntityVisit;
import com.test.test.entity.Status;
import com.test.test.entity.Teeth;
import com.test.test.entity.TeethStatus;
import com.test.test.exeption.BusinessException;
import com.test.test.repository.TeethRepository;
import com.test.test.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequireAuth
public class DockterService {

    @Autowired
    VisitRepository visitRepository;
    @Autowired
    TeethRepository teethRepository;

    public DockterService(VisitRepository visitRepository, TeethRepository teethRepository) {
        this.visitRepository = visitRepository;
        this.teethRepository = teethRepository;
    }

    public List<AllVisitResponse> returnAllVisitChecked() {
        List<EntityVisit> entityVisits = visitRepository.findAll();
        List<EntityVisit> entityVisits1 = entityVisits.stream().filter(entityVisit -> entityVisit.getStatus() == Status.TREATMENT).toList();
        List<AllVisitResponse> allVisitRespons = entityVisits1.stream().map(entityVisit1 -> new AllVisitResponse(entityVisit1.getUser().getUsername(), entityVisit1.getStatus(), entityVisit1.getId(), entityVisit1.getUpdatedAt())).toList();
        return allVisitRespons;
    }


    public DocDto userVisit(int visitid) {
        EntityVisit entityVisit = visitRepository.findById(visitid).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        if (entityVisit.getStatus() != Status.TREATMENT) {
            throw new BusinessException("visit.status.invalid","وضعیت نوبت معتبر نیست",HttpStatus.BAD_REQUEST);

        }
        DocDto docDto = new DocDto();
        docDto.setUsername(entityVisit.getUser().getUsername());
        docDto.setElat(entityVisit.getElat());
        docDto.setSabeghe(entityVisit.getSabeghe());
        docDto.setDaromasrafi(entityVisit.getDaromasrafi());
        List<Teeth> teeth = new ArrayList<>();
        teeth.add(Teeth.implant);
        teeth.add(Teeth.to_fill);
        teeth.add(Teeth.nervousness);
        docDto.setTeeth(teeth);
        return docDto;

    }

    public TreatmentDto returnTeethStatus(int id, TreatmentDto treatmentDto) {
        EntityVisit entityVisit = visitRepository.findById(id).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit,Status.TREATMENT);
        TeethStatus teethStatus = new TeethStatus();
        teethStatus.setTeeth(treatmentDto.getTeeth());
        teethStatus.setNumberOfTeeths(treatmentDto.getNumberofteeths());
        teethStatus.setEntityVisit(entityVisit);
        if (teethRepository.existsByEntityVisitAndTeethAndNumberOfTeeths(entityVisit, treatmentDto.getTeeth(), treatmentDto.getNumberofteeths())) {
            throw new BusinessException("teeth.duplicate","این دندان با نیازمندی مشخص قبلاً ثبت شده است.",HttpStatus.BAD_REQUEST);
        }
        entityVisit.add(teethStatus);
        teethRepository.save(teethStatus);
        treatmentDto.setIdTeeth(teethStatus.getId());
        return treatmentDto;


    }

    @Transactional
    public void deleteTreatment(int id, DeleteTreatmentDto deleteTreatmentDto) {
        EntityVisit entityVisit = visitRepository.findById(id).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit,Status.TREATMENT);


        TeethStatus teethStatus = teethRepository.findByIdAndEntityVisit(deleteTreatmentDto.getId(), entityVisit)
                .orElseThrow(() -> new BusinessException("teeth.visit.not.found","وضعیت دندانی برای این ویزیت با این ID پیدا نشد",HttpStatus.NOT_FOUND))
        ;

        // حذف رکورد از دیتابیس
        teethRepository.delete(teethStatus);

        // اگر orphanRemoval=true نیست و از لیست در entityVisit نگهداری می‌کنی:
        entityVisit.getTeethStatuses().remove(teethStatus);

    }

    public void registration(int id, TreatmentDto treatmentDto) {
        EntityVisit entityVisit = visitRepository.findById(id).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit,Status.TREATMENT);
        entityVisit.setDocText(treatmentDto.getDocText());
        entityVisit.setStatus(Status.DESIGN_REVIEW);
        visitRepository.save(entityVisit);
    }


    public List<SecDto> getTreatment(int visitid) {
        EntityVisit entityVisit = visitRepository.findById(visitid).orElseThrow(() -> new BusinessException("visit.not.found","ویزیتی برای این نشانی پیدا نشد",HttpStatus.NOT_FOUND));
        VisitValidator.validateStatus(entityVisit,Status.TREATMENT);
        List<TeethStatus> teethStatuses = teethRepository.findByEntityVisit(entityVisit);
        List<SecDto> secDtos = teethStatuses.stream().map(teethStatus -> new SecDto(teethStatus.getTeeth(), teethStatus.getNumberOfTeeths())).toList();
        return secDtos;

    }
}
