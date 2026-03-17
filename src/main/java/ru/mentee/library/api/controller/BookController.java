package ru.mentee.library.api.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.library.api.dto.BookDto;
import ru.mentee.library.api.dto.CreateBookRequest;
import ru.mentee.library.filter.BookSearchFilter;
import ru.mentee.library.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Validated
public class BookController {
  private final BookService bookService;
  private final ObjectFactory<BookSearchFilter> bookSearchFilter;

  @GetMapping
  public ResponseEntity<List<BookDto>> getBooks(
      @RequestParam(required = false) Optional<String> author) {
    return ResponseEntity.ok(bookService.findAll(author));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
    return ResponseEntity.ok(bookService.findBookById(id));
  }

  @PostMapping
  public ResponseEntity<BookDto> createBook(@Valid @RequestBody CreateBookRequest request) {
    BookDto created = bookService.createBook(request);
    URI location = URI.create("/api/v1/books/" + created.id());
    return ResponseEntity.created(location).body(created);
  }

  @GetMapping("/search-id")
  public ResponseEntity<BookSearchFilter> searchBooks() {
    return ResponseEntity.ok(bookSearchFilter.getObject());
  }
}
