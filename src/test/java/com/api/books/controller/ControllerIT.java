package com.api.books.controller;

import com.api.books.Models.Book;
import com.api.books.TestData;
import com.api.books.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookService bookService;

    @Test
    public void testThatBookIsCreatedReturns201() throws Exception{
        Book book= TestData.testBook();
        final ObjectMapper objectMapper= new ObjectMapper();
        final String bookJson= objectMapper.writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/"+book.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

@Test
    public void testThatBookIsUpdatedReturns200() throws Exception{
        final Book book=TestData.testBook();
        bookService.create(book);
        book.setAuthor("Woolf");

        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson= objectMapper.writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/"+book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatRetrieveBookReturnsNotFound() throws Exception{
        final Book book=TestData.testBook();
        mockMvc.perform(MockMvcRequestBuilders.get("/books/"+book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveBookReturns200() throws Exception{
        final Book book =TestData.testBook();
        bookService.create(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/"+book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatRetrieveAllBookListsReturns200WhenBookNotExists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));

    }

    @Test
    public void testThatRetrieveAllBookListsReturns200WhenBookExists() throws Exception{
       final Book book= Book.builder()
               .isbn("12345")
               .title("Waves")
               .author("The Woolf")
               .build();
        bookService.create(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value(book.getTitle()));
    }

    @Test
    public void testThatReturns404WhenBookNotExistsToDelete() throws Exception{
        Book book= TestData.testBook();
        mockMvc.perform(MockMvcRequestBuilders.get("/books/"+book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeleteByIdReturns204WhenBookDeleted() throws Exception{
        Book book= TestData.testBook();
        bookService.create(book);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/"+book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
