package com.project.BooksAndAuthors.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BooksAndAuthors.TestDataUtil;
import com.project.BooksAndAuthors.domain.dto.AuthorDto;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.services.AuthorService;
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


//loads the complete application context(with controller, service, repository and all other beans)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//mockmvc used for testing
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests
{
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHTTP201Created() throws Exception
    {
        AuthorDto author = TestDataUtil.createTestAuthorDto1();
        author.setId(null);
        String authorJson = this.objectMapper.writeValueAsString(author);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception
    {
        AuthorDto author = TestDataUtil.createTestAuthorDto1();
        author.setId(null);
        String authorJson = this.objectMapper.writeValueAsString(author);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author.getAge())
        );
    }

    @Test
    public void testThatGetAllAuthorsReturnsHTTP200Ok() throws Exception
    {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAllAuthorsReturnsTheCorrectListOfAuthors() throws Exception
    {
        //added authorEntity to DB using the service
        AuthorEntity author = TestDataUtil.createTestAuthor1();
        this.authorService.saveAuthor(author);

        //this is for listAll which doesn't use pagination
//        this.mockMvc.perform(
//                MockMvcRequestBuilders.get("/authors")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                //$ - root element, $[0] - the first elem in the array
//                //attributes are extracted and converted from JSON in order to be compared
//                MockMvcResultMatchers.jsonPath("$[0].id").value(author.getId())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].name").value(author.getName())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].age").value(author.getAge())
//        );

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                //$ - root element, $[0] - the first elem in the array
                //attributes are extracted and converted from JSON in order to be compared
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(author.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].age").value(author.getAge())
        );
    }

    @Test
    public void testThatGetAuthorReturnsHTTPStatus200WhenFound() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        this.authorService.saveAuthor(authorEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHTTP404WhenNotFound() throws Exception
    {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/213")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsTheCorrectAuthor() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        this.authorService.saveAuthor(authorEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHTTP200WhenFound() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        authorEntity.setId(12L);
        AuthorEntity savedAuthor = this.authorService.saveAuthor(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();

        String authorJson = this.objectMapper.writeValueAsString(authorDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHTTP404WhenNotFound() throws Exception
    {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();
        authorDto.setId(123324L);
        String authorJson = this.objectMapper.writeValueAsString(authorDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsTheCorrectUpdatedAuthor() throws Exception
    {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();
        String authorDtoJson = this.objectMapper.writeValueAsString(authorDto);

        AuthorEntity authorEntity = TestDataUtil.createTestAuthor3();
        AuthorEntity savedAuthor = this.authorService.saveAuthor(authorEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsHTTPStatus200OkWhenAuthorIsFoundAndUpdated() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        this.authorService.saveAuthor(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();
        authorDto.setAge(0);
        String authorDtoJson = this.objectMapper.writeValueAsString(authorDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsHTTP404NotFoundWhenAuthorIsNotFound() throws Exception
    {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();
        String authorDtoJson = this.objectMapper.writeValueAsString(authorDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsTheCorrectUpdatedAuthor() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        this.authorService.saveAuthor(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDto1();
        authorDto.setAge(1);
        String authorDtoJson = this.objectMapper.writeValueAsString(authorDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHTTP204NoContentForExistingAuthor() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        this.authorService.saveAuthor(authorEntity);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + authorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHTTP204NoContentForNonExistingAuthor() throws Exception
    {
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + 13438)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
