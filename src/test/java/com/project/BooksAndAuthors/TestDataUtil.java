package com.project.BooksAndAuthors;

import com.project.BooksAndAuthors.domain.dto.AuthorDto;
import com.project.BooksAndAuthors.domain.dto.BookDto;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.domain.entities.BookEntity;

public final class TestDataUtil
{
    private TestDataUtil()
    {
    }


    public static AuthorEntity createTestAuthor1()
    {
        return AuthorEntity.builder()
                .id(1L)
                .name("aba")
                .age(18)
                .build();
    }

    public static AuthorEntity createTestAuthor2()
    {
        return AuthorEntity.builder()
                .id(2L)
                .name("bbb")
                .age(19)
                .build();
    }

    public static AuthorEntity createTestAuthor3()
    {
        return AuthorEntity.builder()
                .id(3L)
                .name("ccc")
                .age(20)
                .build();
    }

    public static AuthorDto createTestAuthorDto1()
    {
        return AuthorDto.builder()
                .id(1L)
                .name("aba")
                .age(18)
                .build();
    }

    public static BookEntity createTestBook(final AuthorEntity authorEntity)
    {
        return BookEntity.builder()
                .isbn("9781442499550")
                .title("Cool Book")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDto(final AuthorDto authorDto)
    {
        return BookDto.builder()
                .isbn("9781442499550")
                .title("Cool Book")
                .author(authorDto)
                .build();
    }

    public static BookEntity createTestBook1(final AuthorEntity authorEntity)
    {
        return BookEntity.builder()
                .isbn("9781442499551")
                .title("Cool Book1")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBook2(final AuthorEntity authorEntity)
    {
        return BookEntity.builder()
                .isbn("9781442499552")
                .title("Cool Book2")
                .authorEntity(authorEntity)
                .build();
    }


}
