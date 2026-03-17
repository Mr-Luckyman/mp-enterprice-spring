package ru.mentee.library.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.library.api.dto.BookDto;
import ru.mentee.library.api.dto.CreateBookRequest;
import ru.mentee.library.domain.model.Book;
import ru.mentee.library.domain.repository.BookRepository;
import ru.mentee.library.exception.BookNotFoundException;
import ru.mentee.library.mapper.BookMapper;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

  @Mock private BookRepository bookRepository;

  @Mock private BookMapper bookMapper;

  @Mock private AuthorValidationService authorValidationService;

  @InjectMocks private BookServiceImpl bookService;

  @Test
  void shouldFindBookById() {
    Book book = new Book();
    book.setId(1L);
    book.setTitle("title");
    book.setAuthor("Author");
    book.setIsbn("ISBN");
    book.setPublishedDate(LocalDate.now());
    book.setAvailable(true);

    BookDto expectedDto = new BookDto(1L, "title", "Author", "ISBN", LocalDate.now(), true);

    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(bookMapper.toDto(book)).thenReturn(expectedDto);

    BookDto result = bookService.findBookById(1L);

    assertEquals(expectedDto, result);
    verify(bookRepository).findById(1L);
    verify(bookMapper).toDto(book);
  }

  @Test
  void shouldThrowExceptionWhenBookNotFound() {
    when(bookRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> bookService.findBookById(999L));

    verify(bookRepository).findById(999L);
    verifyNoInteractions(bookMapper);
  }

  @Test
  void shouldFindAllBooksWithoutAuthor() {
    Book bookFirst = new Book();
    bookFirst.setId(1L);
    bookFirst.setTitle("title");
    bookFirst.setAuthor("Author");
    bookFirst.setIsbn("ISBN");
    bookFirst.setPublishedDate(LocalDate.now());
    bookFirst.setAvailable(true);

    Book bookSecond = new Book();
    bookSecond.setId(2L);
    bookSecond.setTitle("title2");
    bookSecond.setAuthor("Author2");
    bookSecond.setIsbn("ISBN2");
    bookSecond.setPublishedDate(LocalDate.now());
    bookSecond.setAvailable(true);

    List<Book> bookList = List.of(bookFirst, bookSecond);

    BookDto expectedFirstDto = new BookDto(1L, "title", "Author", "ISBN", LocalDate.now(), true);
    BookDto expectedSecondDto =
        new BookDto(2L, "title2", "Author2", "ISBN2", LocalDate.now(), true);
    List<BookDto> expectedList = List.of(expectedFirstDto, expectedSecondDto);

    when(bookRepository.findAll()).thenReturn(bookList);
    when(bookMapper.toDto(bookFirst)).thenReturn(expectedFirstDto);
    when(bookMapper.toDto(bookSecond)).thenReturn(expectedSecondDto);

    List<BookDto> result = bookService.findAll(Optional.empty());

    assertEquals(expectedList, result);
    verify(bookRepository).findAll();
    verify(bookMapper).toDto(bookFirst);
    verify(bookMapper).toDto(bookSecond);
  }

  @Test
  void shouldFindAllBooksWithAuthorFilter() {
    Book book = new Book();
    book.setId(1L);
    book.setTitle("title");
    book.setAuthor("Author");
    book.setIsbn("ISBN");
    book.setPublishedDate(LocalDate.now());
    book.setAvailable(true);

    List<Book> bookList = List.of(book);

    BookDto expectedDto = new BookDto(1L, "title", "Author", "ISBN", LocalDate.now(), true);
    List<BookDto> expectedList = List.of(expectedDto);

    when(bookRepository.findByAuthor("Author")).thenReturn(bookList);
    when(bookMapper.toDto(book)).thenReturn(expectedDto);

    List<BookDto> result = bookService.findAll(Optional.of("Author"));

    assertEquals(expectedList, result);
    verify(bookRepository).findByAuthor("Author");
    verify(bookMapper).toDto(book);
  }

  @Test
  void shouldCreateBookSuccessfully() {
    CreateBookRequest request = new CreateBookRequest();
    request.setTitle("title");
    request.setAuthor("Author");
    request.setIsbn("ISBN");
    request.setPublishedDate(LocalDate.now());

    Book book = new Book();
    book.setId(1L);
    book.setTitle("title");
    book.setAuthor("Author");
    book.setIsbn("ISBN");
    book.setPublishedDate(LocalDate.now());
    book.setAvailable(true);

    BookDto expectedDto = new BookDto(1L, "title", "Author", "ISBN", LocalDate.now(), true);

    when(authorValidationService.validateAuthor("Author")).thenReturn(true);
    when(bookMapper.toEntity(request)).thenReturn(book);
    when(bookRepository.save(book)).thenReturn(book);
    when(bookMapper.toDto(book)).thenReturn(expectedDto);

    BookDto result = bookService.createBook(request);

    assertEquals(expectedDto, result);
    verify(authorValidationService).validateAuthor("Author");
    verify(bookMapper).toEntity(request);
    verify(bookRepository).save(book);
    verify(bookMapper).toDto(book);
  }

  @Test
  void shouldThrowExceptionWhenAuthorNotSupported() {
    CreateBookRequest request = new CreateBookRequest();
    request.setTitle("title");
    request.setAuthor("Unknown Author");
    request.setIsbn("ISBN");
    request.setPublishedDate(LocalDate.now());

    when(authorValidationService.validateAuthor("Unknown Author")).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () -> bookService.createBook(request));

    verify(authorValidationService).validateAuthor("Unknown Author");
    verifyNoInteractions(bookMapper);
    verifyNoInteractions(bookRepository);
  }
}
