package com.api.books.controller;

import com.api.books.Models.Book;
import com.api.books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class Controller {

    private final BookService bookService;

    @Autowired
    public Controller(final BookService bookService){
        this.bookService=bookService;
    }
    @PutMapping(path="/books/{isbn}")
    public ResponseEntity<Book> createUpdateBook(@PathVariable String isbn, @RequestBody Book book){
        book.setIsbn(isbn);
        final boolean isBookExists= bookService.isBookExists(book);
        final Book savedBook= bookService.create(book);

        if(isBookExists){
            return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
        }else{
            return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> retrieveBook(@PathVariable String isbn){
        final Optional<Book> foundBook= bookService.findById(isbn);

        return foundBook.map(book->new ResponseEntity<Book>(book,HttpStatus.OK))
                .orElse(new ResponseEntity<Book>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> retrieveAllBookLists(){
        List<Book> savedBooks = bookService.bookLists();
        return new ResponseEntity<List<Book>>(savedBooks, HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBookById(@PathVariable String isbn){
        boolean response= bookService.deleteById(isbn);

        if(response){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
