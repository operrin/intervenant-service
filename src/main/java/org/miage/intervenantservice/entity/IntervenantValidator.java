package org.miage.intervenantservice.entity;

import java.util.Set;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class IntervenantValidator {

  private final Validator validator;

  IntervenantValidator(Validator validator) {
    this.validator = validator;
  }

  public void validate(IntervenantInput intervenant) {
    Set<ConstraintViolation<IntervenantInput>> violations = validator.validate(intervenant);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}