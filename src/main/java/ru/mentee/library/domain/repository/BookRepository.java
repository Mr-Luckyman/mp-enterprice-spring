package ru.mentee.library.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mentee.library.domain.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
  List<Book> findByAuthor(String author);
}
