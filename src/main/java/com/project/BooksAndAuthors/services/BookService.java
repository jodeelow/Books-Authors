package com.project.BooksAndAuthors.services;

import com.project.BooksAndAuthors.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService
{
    BookEntity saveBook(String isbn, BookEntity book);

    List<BookEntity> findAllBooks();

    //overload findAllBooks to enable Pagination
    Page<BookEntity> findAllBooks(Pageable pageable);

    Optional<BookEntity> findOne(String isbn);

    Boolean Exists(String isbn);

    BookEntity partialUpdateBook(String isbn, BookEntity updatingBook);

    void deleteBook(String isbn);
}
