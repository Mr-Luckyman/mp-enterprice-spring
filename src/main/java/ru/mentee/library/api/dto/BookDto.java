package ru.mentee.library.api.dto;

import java.time.LocalDate;

public record BookDto(
    Long id,
    String title,
    String author,
    String isbn,
    LocalDate publishedDate,
    boolean available) {}
