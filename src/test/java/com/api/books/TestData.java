package com.api.books;

import com.api.books.Models.Book;
import com.api.books.Models.BookEntity;

public class TestData {

    public TestData(){}
    public static Book testBook(){
        return Book.builder()
                .isbn("1234")
                .title("The Waves")
                .author("Woolf")
                .build();
    }

    public static BookEntity testBookEntity(){
       return BookEntity.builder()
                .isbn("1234")
                .title("The Waves")
                .author("Woolf")
                .build();

    }

}
