package com.project.BooksAndAuthors.controllers;

import com.project.BooksAndAuthors.domain.dto.AuthorDto;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.mappers.Mapper;
import com.project.BooksAndAuthors.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController
{
    private AuthorService authorService;

    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper)
    {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    //create endpoint(HTTP POST endpoint)
    //JSON author in the request body => Jackson will convert it to author(DTO) object(Java) - automatically
    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author)
    {
        AuthorEntity authorEntity = this.authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = this.authorService.saveAuthor(authorEntity);
        return new ResponseEntity<>(this.authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }

    //conversion form List<AuthorDto> -> JSON handled by Jackson
    //listing without pagination
//    @GetMapping(path = "/authors")
//    public List<AuthorDto> getAllAuthors()
//    {
//        List<AuthorEntity> authorEntities = this.authorService.findAllAuthors();
//
//        //list of authEntities-> stream of authEntities -> stream of authDto -> list of authDto
//        return authorEntities.stream()
//                .map(this.authorMapper::mapTo)
//                .collect(Collectors.toList());
//    }

    @GetMapping(path = "/authors")
    public Page<AuthorDto> getAllAuthors(Pageable pageable)
    {
        Page<AuthorEntity> authorEntityPage = this.authorService.findAllAuthors(pageable);
        return authorEntityPage.map(this.authorMapper::mapTo);
    }

    @GetMapping(path = "/authors/{id}")
    //store the value from id path variable into a long id
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id)
    {
        Optional<AuthorEntity> authorEntity = this.authorService.findOne(id);
        if(authorEntity.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AuthorDto authorDto = this.authorMapper.mapTo(authorEntity.get());
        return new ResponseEntity<>(authorDto, HttpStatus.OK);
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable Long id, @RequestBody AuthorDto authorDto)
    {
        Optional<AuthorEntity> authorEntity = this.authorService.findOne(id);

        if(authorEntity.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        authorDto.setId(id);
        AuthorEntity updatedAuthorEntity = this.authorMapper.mapFrom(authorDto);
        this.authorService.saveAuthor(updatedAuthorEntity);

        return new ResponseEntity<>(this.authorMapper.mapTo(updatedAuthorEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(@PathVariable Long id, @RequestBody AuthorDto authorDto)
    {
        Boolean authorFound = this.authorService.Exists(id);

        if(!authorFound)
        {
            //if author is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            //if found, update and return
            AuthorEntity updatedAuthor = this.authorService.partialUpdateAuthor(id, this.authorMapper.mapFrom(authorDto));
            return new ResponseEntity<>(this.authorMapper.mapTo(updatedAuthor), HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable Long id)
    {
        this.authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
