package com.api.books.services.Impl;

import com.api.books.Models.Book;
import com.api.books.Models.BookEntity;
import com.api.books.TestData;
import com.api.books.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;
    @Test
    public void testThatBookIsSaved(){
        Book book= TestData.testBook();
        BookEntity bookEntity= TestData.testBookEntity();

        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);

        Book result= bookServiceImpl.create(book);
        assertEquals(result,book);
    }

    @Test
    public void testThatIsBookExistsReturnsFalseWhenNoBookExists(){
        Book book=TestData.testBook();
        when(bookRepository.existsById(any())).thenReturn(false);
        boolean result= bookServiceImpl.isBookExists(book);
        assertEquals(result,false);
    }

    @Test
    public void testThatIsBookExistsReturnsTrueWhenBookExists(){
        Book book=TestData.testBook();
        when(bookRepository.existsById(any())).thenReturn(true);
        boolean result= bookServiceImpl.isBookExists(book);
        assertEquals(result,true);
    }

    @Test
    public void testFindByIdWhenNoBookExists(){
        Book book= TestData.testBook();

        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.empty());
        Optional<Book> result = bookServiceImpl.findById(book.getIsbn());
        assertEquals(result,Optional.empty());
    }

    @Test
    public void testFindByIdWhenBookExists(){
        Book book = TestData.testBook();
        BookEntity bookEntity= TestData.testBookEntity();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));
        Optional<Book> result= bookServiceImpl.findById(book.getIsbn());
        assertEquals(result,Optional.of(book));
    }

    @Test
    public void testBookListsWhenBookNotExists(){

        when(bookRepository.findAll()).thenReturn(new ArrayList<BookEntity>());
        List<Book> result= bookServiceImpl.bookLists();
        assertEquals(result.size(), 0);
    }

    @Test
    public void testBookListsWhenBookExists(){
        BookEntity bookEntity= TestData.testBookEntity();
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));
        List<Book> result= bookServiceImpl.bookLists();
        assertEquals(result.size(),1);
    }

    @Test
    public void testThatDeleteByIdReturnsFalse(){
        Book book=TestData.testBook();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.empty());
        boolean result= bookServiceImpl.deleteById(book.getIsbn());
        assertEquals(result, false);
    }

    @Test
    public void testThatDeleteByIdReturnsTrue(){
        Book book= TestData.testBook();
        BookEntity bookEntity= TestData.testBookEntity();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));
        boolean result= bookServiceImpl.deleteById(book.getIsbn());
        assertEquals(result,true);
    }

}
