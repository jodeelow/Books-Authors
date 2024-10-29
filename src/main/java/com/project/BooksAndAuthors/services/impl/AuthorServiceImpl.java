package com.project.BooksAndAuthors.services.impl;

import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.repositories.AuthorRepository;
import com.project.BooksAndAuthors.services.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService
{

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository)
    {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity saveAuthor(AuthorEntity authorEntity) {
        return this.authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAllAuthors()
    {
        //Iterable -> Spliterator -> Stream -> List
        //parameter false - indicates that the stream is sequential
        return StreamSupport.stream(this.authorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AuthorEntity> findAllAuthors(Pageable pageable)
    {
        return this.authorRepository.findAll(pageable);
    }


    @Override
    public Optional<AuthorEntity> findOne(Long id)
    {
        return this.authorRepository.findById(id);
    }

    @Override
    public Boolean Exists(Long id)
    {
        return this.authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdateAuthor(Long id, AuthorEntity updatingAuthor)
    {
        //only invoked when author is present
        Optional<AuthorEntity> foundAuthor1 = this.authorRepository.findById(id);

        if(foundAuthor1.isEmpty())
            throw new RuntimeException("Author not found");

        AuthorEntity foundAuthor = foundAuthor1.get();
        updatingAuthor.setId(foundAuthor.getId());

        //check which attributes are not present and keep the ones which were already there
        if(updatingAuthor.getName() == null)
        {
            updatingAuthor.setName(foundAuthor.getName());
        }

        if(updatingAuthor.getAge() == null)
        {
            updatingAuthor.setAge(foundAuthor.getAge());
        }

        return this.authorRepository.save(updatingAuthor);
    }

    @Override
    public void deleteAuthor(Long id)
    {
        this.authorRepository.deleteById(id);
    }
}
