package com.test.test.config;

import com.test.test.entity.EntityVisit;
import com.test.test.entity.Status;
import com.test.test.exeption.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class VisitValidator {

    private VisitValidator() {
    }

    public static void validateStatus(EntityVisit visit, Status expectedStatus) {
        if (visit == null || visit.getStatus() != expectedStatus) {
            throw new BusinessException("visit.status.invalid","وضعیت نوبت معتبر نیست",HttpStatus.BAD_REQUEST);

        }
    }
}
