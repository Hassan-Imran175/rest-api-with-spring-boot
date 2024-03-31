package com.api.books.services;

import com.api.books.Models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
  Book create(Book book);

  boolean isBookExists(Book book);

  Optional<Book> findById(String isbn);

  List<Book> bookLists();

  boolean deleteById(String isbn);
}
