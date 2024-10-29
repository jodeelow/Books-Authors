package com.project.BooksAndAuthors.services.impl;

import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.domain.entities.BookEntity;
import com.project.BooksAndAuthors.repositories.BookRepository;
import com.project.BooksAndAuthors.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService
{

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository)
    {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity saveBook(String isbn, BookEntity book)
    {
        //make sure that the saved book has the same isbn as provided
        book.setIsbn(isbn);
        return this.bookRepository.save(book);
    }

    @Override
    public List<BookEntity> findAllBooks()
    {
        return StreamSupport.stream(this.bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookEntity> findAllBooks(Pageable pageable)
    {
        //using pagination
        return this.bookRepository.findAll(pageable);
    }


    @Override
    public Optional<BookEntity> findOne(String isbn)
    {
        return this.bookRepository.findById(isbn);
    }

    @Override
    public Boolean Exists(String isbn)
    {
        return this.bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdateBook(String isbn, BookEntity updatingBook)
    {
        Optional<BookEntity> foundBook1 = this.bookRepository.findById(isbn);

        if(foundBook1.isEmpty())
            throw new RuntimeException("Book not found");

        updatingBook.setIsbn(isbn);
        BookEntity foundBook = foundBook1.get();

        if(updatingBook.getTitle() == null)
        {
            updatingBook.setTitle(foundBook.getTitle());
        }

        if(updatingBook.getAuthorEntity() == null)
        {
            updatingBook.setAuthorEntity(foundBook.getAuthorEntity());
        }

        return this.bookRepository.save(updatingBook);
    }

    @Override
    public void deleteBook(String isbn)
    {
        this.bookRepository.deleteById(isbn);
    }
}
