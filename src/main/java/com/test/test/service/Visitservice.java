package com.test.test.service;

import com.test.test.config.RequireAuth;
import com.test.test.config.VisitValidator;
import com.test.test.dto.*;
import com.test.test.entity.*;
import com.test.test.exeption.BusinessException;
import com.test.test.repository.DefectRepository;
import com.test.test.repository.Repository;
import com.test.test.repository.TeethRepository;
import com.test.test.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequireAuth
@Service
@Component
public class Visitservice {
    @Autowired
    private final Repository repository;
    @Autowired
    private final VisitRepository visitRepository;
    @Autowired
    private final DefectRepository defectRepository;
    @Autowired
    private final TeethRepository teethRepository;


    public Visitservice(Repository repository, VisitRepository visitRepository, DefectRepository defectRepository, TeethRepository teethRepository) {
        this.repository = repository;

        this.visitRepository = visitRepository;

        this.defectRepository = defectRepository;
        this.teethRepository = teethRepository;
    }

    @Transactional
    public VisitDto savevisit(VisitDto visitDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repository.findByUsername(username);

        boolean hasActiveVisit = visitRepository.existsByUserAndIsActiveTrue(user);
        if (hasActiveVisit) {
            throw new BusinessException("visit.exists","شما یک فیزیت فعال دارید",HttpStatus.BAD_REQUEST);

        }

        EntityVisit entityVisit = new EntityVisit();
        entityVisit.setDaromasrafi(visitDto.getDaromasrafi());
        entityVisit.setSabeghe(visitDto.getSabeghe());
        entityVisit.setElat(visitDto.getElat());
        entityVisit.setUser(user);
        user.add(entityVisit);
        entityVisit.setStatus(Status.CHECK_DOCUMENTS);
        entityVisit.setIsActive(true);
        visitDto.setStatusText(Status.CHECK_DOCUMENTS);
        visitRepository.save(entityVisit);
        return visitDto;

    }

    public VisitResponDto visitStatus() {
        EntityVisit entityVisit = getEntityVisit();
        EntityDefect entityDefect=defectRepository.findFirstByVisitOrderByCreatedAtDesc(entityVisit);
        List<TeethStatus> teethStatuses=teethRepository.findByEntityVisit(entityVisit);
        VisitResponDto visitResponDto=VisitResponDto.builder()
                .daromasrafi(entityVisit.getDaromasrafi())
                .elat(entityVisit.getElat())
                .docText(entityVisit.getDocText())
                .sabeghe(entityVisit.getSabeghe())
                .secText(entityVisit.getSecText())
                .scheduledAt(entityVisit.getScheduledAt())
                .statusText(entityVisit.getStatus())
                .defectText(entityDefect.getText())
                .updatedAt(entityVisit.getUpdatedAt())
                .secDtos(teethStatuses.stream().map(teethStatus -> new SecDto(teethStatus.getTeeth(),teethStatus.getNumberOfTeeths())).toList())
                .build();
        return visitResponDto;


    }

    private EntityVisit getEntityVisit() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new BusinessException("user.not.found","کاربر پیدا نشد",HttpStatus.NOT_FOUND);

        }

        EntityVisit entityVisit = visitRepository
                .findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new BusinessException("visit.not.found.user","ویزیتی برای این کاربر ثبت نشده است.",HttpStatus.NOT_FOUND));



        return entityVisit;
    }

//    public List<SecDto> visitView() {
//        EntityVisit entityVisit = getEntityVisit();
//        if (entityVisit.getStatus() != Status.TURNS) {
//            throw new BusinessException("visit.status.invalid","وضعیت نوبت معتبر نیست",HttpStatus.BAD_REQUEST);
//        }
//        List<TeethStatus> teethStatuses = teethRepository.findByEntityVisit(entityVisit);
//        List<SecDto> secDtos = teethStatuses.stream().map(teethStatus -> new SecDto(teethStatus.getTeeth(), teethStatus.getNumberOfTeeths())).toList();
//        return secDtos;
//    }

    public void visitTurn() {
        EntityVisit entityVisit = getEntityVisit();

        if (entityVisit.getStatus() != Status.TURNS) {
            throw new BusinessException("visit.status.invalid","وضعیت نوبت معتبر نیست",HttpStatus.BAD_REQUEST);

        }

        entityVisit.setStatus(Status.READY);
        visitRepository.save(entityVisit);

    }

    public List<VisitCountByDayDTO> getWeeklyVisitCounts(LocalDate anyDateInWeek) {
        // پیدا کردن شنبه و جمعه‌ی اون هفته
        DayOfWeek firstDayOfWeek = DayOfWeek.SATURDAY;
        LocalDate startOfWeek = anyDateInWeek.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<VisitCountByDayDTO> results = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDay = startOfWeek.plusDays(i);
            LocalDateTime start = currentDay.atStartOfDay();
            LocalDateTime end = currentDay.atTime(LocalTime.MAX);

            long count = visitRepository.countByScheduledAtBetween(start, end);
            results.add(new VisitCountByDayDTO(currentDay, count));
        }

        return results;
    }


    public List<AllVisitResponse> getAllVisit() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repository.findByUsername(username);
        List<EntityVisit> entityVisits = visitRepository.findByUserOrderByCreatedAtDesc(user);
        List<AllVisitResponse> allVisitResponses = new ArrayList<>(entityVisits.stream().map(entityVisit -> new AllVisitResponse(entityVisit.getUser().getUsername(),entityVisit.getStatus(),entityVisit.getId(),entityVisit.getCreatedAt())).toList());

        return allVisitResponses;
    }

    public void cancleVisit() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repository.findByUsername(username);

        EntityVisit lastVisit = visitRepository
                .findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() ->  new BusinessException("visit.not.found.user","ویزیتی برای این کاربر ثبت نشده است.",HttpStatus.NOT_FOUND));

        visitRepository.delete(lastVisit);
    }

//    public DefectDto getDefect() {
//        EntityVisit entityVisit = getEntityVisit();
//
//        if (entityVisit.getStatus() != Status.DOCUMENT_DEFECTS) {
//            throw new BusinessException("visit.status.invalid","وضعیت نوبت معتبر نیست",HttpStatus.BAD_REQUEST);
//
//        }
//        EntityDefect entityDefect = defectRepository.findFirstByVisitOrderByCreatedAtDesc(entityVisit);
//        if (entityDefect==null) {
//            new BusinessException("visit.defect.not.found","نقص مدرکی برای این ویزیت وجود ندارد.",HttpStatus.NOT_FOUND);
//        }
//
//        return new DefectDto(
//                entityDefect.getText(),
//                entityVisit.getStatus(),
//                entityDefect.getCreatedAt()
//        );
//    }

    public VisitDto updateVisit(VisitDto visitDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repository.findByUsername(username);

        EntityVisit entityVisit = visitRepository
                .findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() ->new BusinessException("visit.status.invalid","وضعیت نوبت معتبر نیست",HttpStatus.BAD_REQUEST));

        VisitValidator.validateStatus(entityVisit, Status.DOCUMENT_DEFECTS);
        entityVisit.setDaromasrafi(visitDto.getDaromasrafi());
        entityVisit.setSabeghe(visitDto.getSabeghe());
        entityVisit.setElat(visitDto.getElat());
        entityVisit.setStatus(Status.CHECK_DOCUMENTS);
        visitRepository.save(entityVisit);
        visitDto.setStatusText(entityVisit.getStatus());
        return visitDto;
    }





    public TurnDateDto getTurnDate() {
        EntityVisit entityVisit = getEntityVisit();
        TurnDateDto turnDateDto = new TurnDateDto();
        turnDateDto.setScheduledAt(entityVisit.getScheduledAt());
        return turnDateDto;
    }

    public void registrationTurn() {
        EntityVisit entityVisit = getEntityVisit();
        VisitValidator.validateStatus(entityVisit, Status.WAITING);
        entityVisit.setStatus(Status.FINISH);
        entityVisit.setIsActive(false);
        visitRepository.save(entityVisit);
    }


    public VisitResponDto getVisit(int visitid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repository.findByUsername(username);
        EntityVisit entityVisit=visitRepository.findByIdAndUser(visitid, user).orElseThrow(() ->  new BusinessException("visit.not.found.user","ویزیتی برای این کاربر ثبت نشده است.",HttpStatus.NOT_FOUND));
        EntityDefect entityDefect=defectRepository.findFirstByVisitOrderByCreatedAtDesc(entityVisit);
        List<TeethStatus> teethStatuses=teethRepository.findByEntityVisit(entityVisit);
        VisitResponDto visitResponDto=VisitResponDto.builder()
                .daromasrafi(entityVisit.getDaromasrafi())
                .elat(entityVisit.getElat())
                .docText(entityVisit.getDocText())
                .sabeghe(entityVisit.getSabeghe())
                .secText(entityVisit.getSecText())
                .scheduledAt(entityVisit.getScheduledAt())
                .statusText(entityVisit.getStatus())
                .defectText(entityDefect.getText())
                .secDtos(teethStatuses.stream().map(teethStatus -> new SecDto(teethStatus.getTeeth(),teethStatus.getNumberOfTeeths())).toList())
                .build();
        return visitResponDto;


    }
}


