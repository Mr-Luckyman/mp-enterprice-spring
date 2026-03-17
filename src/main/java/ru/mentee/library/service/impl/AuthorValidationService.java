package ru.mentee.library.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mentee.library.service.validation.AuthorValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorValidationService {
  private final List<AuthorValidator> authorValidators;

  public boolean validateAuthor(String author) {
    return authorValidators.stream().anyMatch(validator -> validator.isSupported(author));
  }

  @PostConstruct
  public void init() {
    log.info("Author Validation Service Initialized");
  }

  @PreDestroy
  public void destroy() {
    log.info("Author Validation Service Destroyed");
  }
}
