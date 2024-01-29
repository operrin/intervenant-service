package org.miage.intervenantservice.entity;

import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class IntervenantValidator {
    
    private final Validator validator;

    IntervenantValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(IntervenantInput intervenant) {
        var constraintViolations = validator.validate(intervenant);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
