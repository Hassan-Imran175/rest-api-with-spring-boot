package com.api.books.services.Impl;

import com.api.books.Models.Book;
import com.api.books.Models.BookEntity;
import com.api.books.repositories.BookRepository;
import com.api.books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(final BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    @Override
    public Book create(Book book) {
        final BookEntity bookEntity= bookToBookEntity(book);
        final BookEntity savedBookEntity= bookRepository.save(bookEntity);
        return bookEntityToBook(savedBookEntity);
    }

    public BookEntity bookToBookEntity(Book book){
        return BookEntity.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }

    public Book bookEntityToBook(BookEntity bookEntity){
        return Book.builder()
                .isbn(bookEntity.getIsbn())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .build();
    }

    @Override
    public boolean isBookExists(Book book){
        return bookRepository.existsById(book.getIsbn());
    }

    @Override
    public Optional<Book> findById(String isbn) {
        final Optional<BookEntity> foundBook= bookRepository.findById(isbn);
        return foundBook.map(book-> bookEntityToBook(book));
    }

    @Override
    public List<Book> bookLists(){
        List<BookEntity> foundBooks = (List<BookEntity>) bookRepository.findAll();
        return foundBooks.stream().map(book->bookEntityToBook(book)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String isbn) {
        Optional<Book> findBook = findById(isbn);

        if(findBook.isPresent()){
            bookRepository.deleteById(isbn);
            return true;
        }else{
            return false;
        }
    }
}