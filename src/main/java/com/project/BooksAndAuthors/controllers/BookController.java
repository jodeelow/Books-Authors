package com.project.BooksAndAuthors.controllers;

import com.project.BooksAndAuthors.domain.dto.BookDto;
import com.project.BooksAndAuthors.domain.entities.BookEntity;
import com.project.BooksAndAuthors.mappers.Mapper;
import com.project.BooksAndAuthors.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController
{
    private BookService bookService;
    private Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper)
    {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    //these requests are end-points
    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto)
    {
        //System.out.println(bookDto.toString());

        Boolean bookExists = this.bookService.Exists(isbn);
        BookEntity bookEntity = this.bookMapper.mapFrom(bookDto);
        //System.out.println(bookEntity.toString());

        BookEntity savedBookEntity = this.bookService.saveBook(isbn, bookEntity);
        //System.out.println(savedBookEntity.toString());

        BookDto updatedBookDto = this.bookMapper.mapTo(savedBookEntity);
        //System.out.println(updatedBookDto.toString());

        //if exists is an update, else it is created
        if(!bookExists)
        {
            //doesn't exist
            return new ResponseEntity<>(updatedBookDto, HttpStatus.CREATED);
        }

        else
        {
            //already exists
            return new ResponseEntity<>(updatedBookDto, HttpStatus.OK);
        }
    }

    // implementation without pagination
//    @GetMapping(path = "/books")
//    public List<BookDto> getAllBooks()
//    {
//        List<BookEntity> bookEntities = this.bookService.findAllBooks();
//
//        return bookEntities.stream()
//                .map(bookMapper::mapTo)
//                .collect(Collectors.toList());
//    }

    @GetMapping(path = "/books")
    public Page<BookDto> getAllBooks(Pageable pageable)
    {
        Page<BookEntity> bookEntityPage = this.bookService.findAllBooks(pageable);
        //maps all bookEntities from the page to bookDtos
        Page<BookDto> bookDtoPage = bookEntityPage.map(this.bookMapper::mapTo);

        return bookDtoPage;
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn)
    {
        Optional<BookEntity> bookEntity = this.bookService.findOne(isbn);

        if(bookEntity.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        BookDto bookDto = this.bookMapper.mapTo(bookEntity.get());
        return new ResponseEntity<>(bookDto, HttpStatus.OK);
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable String isbn, @RequestBody BookDto bookDto)
    {
        Boolean bookExists = this.bookService.Exists(isbn);

        if(!bookExists)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        BookEntity updatingBook = this.bookMapper.mapFrom(bookDto);
        BookEntity updatedBook = this.bookService.partialUpdateBook(isbn, updatingBook);

        return new ResponseEntity<>(this.bookMapper.mapTo(updatedBook), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable String isbn)
    {
        this.bookService.deleteBook(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
