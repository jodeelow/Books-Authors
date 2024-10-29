package com.project.BooksAndAuthors.mappers.impl;

import com.project.BooksAndAuthors.domain.dto.AuthorDto;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper implements Mapper<AuthorEntity, AuthorDto>
{

    private ModelMapper modelMapper;

    //inject the modelMapper
    public AuthorMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthorDto mapTo(AuthorEntity authorEntity)
    {
        //just use method map from ModelMapper class
        //map creates a new object of AuthorDto type with data from AuthorEntity class
        return this.modelMapper.map(authorEntity, AuthorDto.class);
    }

    @Override
    public AuthorEntity mapFrom(AuthorDto authorDto)
    {
        return this.modelMapper.map(authorDto, AuthorEntity.class);
    }
}
