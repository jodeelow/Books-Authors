package com.project.BooksAndAuthors.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BooksAndAuthors.TestDataUtil;
import com.project.BooksAndAuthors.domain.dto.AuthorDto;
import com.project.BooksAndAuthors.domain.dto.BookDto;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.domain.entities.BookEntity;
import com.project.BooksAndAuthors.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests
{
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnsHTTPStatus201() throws Exception
    {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();

        BookDto bookDto = TestDataUtil.createTestBookDto(authorDto);

        String bookJson = this.objectMapper.writeValueAsString(bookDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception
    {
        //AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();

        BookDto bookDto = TestDataUtil.createTestBookDto(null);

        //System.out.println(bookDto);

        String bookJson = this.objectMapper.writeValueAsString(bookDto);

        //System.out.println(bookJson);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDto.getAuthor())
        );
    }

    @Test
    public void testThatGetAllBooksReturnsHTTP200Ok() throws Exception
    {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAllBooksReturnsTheCorrectListOfBooks() throws Exception
    {
        BookEntity bookEntity = TestDataUtil.createTestBook(null);
        BookEntity savedBookEntity = this.bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        //this is for listAll without pagination
//        this.mockMvc.perform(
//                MockMvcRequestBuilders.get("/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].isbn").value(savedBookEntity.getIsbn())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].title").value(savedBookEntity.getTitle())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].author").value(savedBookEntity.getAuthorEntity())
//        );

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(savedBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value(savedBookEntity.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].author").value(savedBookEntity.getAuthorEntity())
        );
    }

    @Test
    public void testThatGetBookReturnsHTTPStatus200OkWhenFound() throws Exception
    {
        BookEntity bookEntity = TestDataUtil.createTestBook(null);
        this.bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHTTPStatus404WhenNotFound() throws Exception
    {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/books/awd")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsHTTP200OkWhenUpdated() throws Exception
    {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String bookDtoJson = this.objectMapper.writeValueAsString(bookDto);

        BookEntity bookEntity = TestDataUtil.createTestBook2(null);
        BookEntity savedBook = this.bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsTheCorrectUpdatedBookWhenItExists() throws Exception
    {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String bookDtoJson = this.objectMapper.writeValueAsString(bookDto);

        BookEntity bookEntity = TestDataUtil.createTestBook2(null);
        BookEntity savedBook = this.bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDto.getAuthor())
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsHTTP200OkWhenBookIsUpdated() throws Exception
    {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String bookDtoJson = this.objectMapper.writeValueAsString(bookDto);

        BookEntity bookEntity = TestDataUtil.createTestBook2(null);
        this.bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsHTTP404NotFoundWhenBookIsNotFound() throws Exception
    {
        BookEntity bookEntity = TestDataUtil.createTestBook2(null);
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String bookDtoJson = this.objectMapper.writeValueAsString(bookDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsTheCorrectUpdatedBook() throws Exception
    {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();
        BookDto bookDto = TestDataUtil.createTestBookDto(authorDto);
        bookDto.setTitle(null);
        String bookDtoJson = this.objectMapper.writeValueAsString(bookDto);

        BookEntity bookEntity = TestDataUtil.createTestBook2(null);
        this.bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntity.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDto.getAuthor())
        );
    }

    @Test
    public void testThatDeleteBookReturnsHTTP204NoContentForExistingBook() throws Exception
    {
        BookEntity bookEntity = TestDataUtil.createTestBook2(null);
        this.bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookReturnsHTTP204NoContentForNoExistingBook() throws Exception
    {
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + "3214")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
