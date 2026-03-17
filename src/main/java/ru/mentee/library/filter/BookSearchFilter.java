package ru.mentee.library.filter;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class BookSearchFilter {
  private final UUID searchId;

  public BookSearchFilter() {
    log.debug("BookSearchFilter constructor");
    searchId = UUID.randomUUID();
  }

  public UUID getSearchId() {
    return searchId;
  }
}
