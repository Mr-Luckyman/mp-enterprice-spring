package ru.mentee.library.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.library.api.dto.BookDto;
import ru.mentee.library.api.dto.CreateBookRequest;
import ru.mentee.library.domain.model.Book;
import ru.mentee.library.domain.repository.BookRepository;
import ru.mentee.library.exception.BookNotFoundException;
import ru.mentee.library.mapper.BookMapper;
import ru.mentee.library.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
  private final BookRepository bookRepository;
  private final BookMapper bookMapper;
  private final AuthorValidationService authorValidationService;

  @Override
  public BookDto findBookById(Long id) {
    return bookRepository
        .findById(id)
        .map(bookMapper::toDto)
        .orElseThrow(() -> new BookNotFoundException(id));
  }

  @Override
  public List<BookDto> findAll(Optional<String> author) {
    return author.map(bookRepository::findByAuthor).orElseGet(bookRepository::findAll).stream()
        .map(bookMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public BookDto createBook(CreateBookRequest createBook) {
    if (!authorValidationService.validateAuthor(createBook.getAuthor())) {
      throw new IllegalArgumentException("Author not supported: " + createBook.getAuthor());
    }
    Book save = bookRepository.save(bookMapper.toEntity(createBook));
    return bookMapper.toDto(save);
  }
}
