package ru.mentee.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mentee.library.api.dto.BookDto;
import ru.mentee.library.api.dto.CreateBookRequest;
import ru.mentee.library.domain.model.Book;

@Mapper()
public interface BookMapper {
  BookDto toDto(Book book);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "available", constant = "true")
  Book toEntity(CreateBookRequest createBook);
}
