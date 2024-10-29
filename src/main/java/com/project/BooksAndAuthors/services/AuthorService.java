package com.project.BooksAndAuthors.services;

import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AuthorService
{
    //creates the author and returns it
    AuthorEntity saveAuthor(AuthorEntity authorEntity);

    List<AuthorEntity> findAllAuthors();

    Page<AuthorEntity> findAllAuthors(Pageable pageable);

    Optional<AuthorEntity> findOne(Long id);

    Boolean Exists(Long id);

    AuthorEntity partialUpdateAuthor(Long id, AuthorEntity authorEntity);

    void deleteAuthor(Long id);
}
