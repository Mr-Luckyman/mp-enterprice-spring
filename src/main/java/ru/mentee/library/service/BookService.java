package ru.mentee.library.service;

import java.util.List;
import java.util.Optional;
import ru.mentee.library.api.dto.BookDto;
import ru.mentee.library.api.dto.CreateBookRequest;

public interface BookService {
  BookDto findBookById(Long id);

  List<BookDto> findAll(Optional<String> author);

  BookDto createBook(CreateBookRequest createBook);
}
