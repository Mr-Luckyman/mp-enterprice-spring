package ru.mentee.library.service.validation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("russian")
public class RussianAuthorValidator implements AuthorValidator {
  @Override
  public boolean isSupported(String authorName) {
    return authorName.matches(".*[а-яА-Я].*");
  }
}
