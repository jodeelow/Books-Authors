package com.project.BooksAndAuthors.mappers.impl;

import com.project.BooksAndAuthors.domain.dto.AuthorDto;
import com.project.BooksAndAuthors.domain.dto.BookDto;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.domain.entities.BookEntity;
import com.project.BooksAndAuthors.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<BookEntity, BookDto>
{

    private ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapTo(BookEntity bookEntity)
    {
        return this.modelMapper.map(bookEntity, BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto)
    {
        return this.modelMapper.map(bookDto, BookEntity.class);
    }
}
