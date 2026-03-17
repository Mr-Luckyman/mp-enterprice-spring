package ru.mentee.library.service.validation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("foreign")
public class ForeignAuthorValidator implements AuthorValidator {
  @Override
  public boolean isSupported(String authorName) {
    return authorName.matches(".*[a-zA-Z].*");
  }
}
