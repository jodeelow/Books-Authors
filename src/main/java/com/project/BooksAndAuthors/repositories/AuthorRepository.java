package com.project.BooksAndAuthors.repositories;

import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


//annotation that acts like @Component, this is now a bean(no need to implement)
@Repository
//specify the class/actually Entity and the type of the PK
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long>,
        PagingAndSortingRepository<AuthorEntity, Long>
{
    Iterable<AuthorEntity> findByAgeLessThan(int age);

    //working with java objects, ?1 - the first argument of the function
    @Query("SELECT a FROM AuthorEntity a WHERE a.age > ?1")
    Iterable<AuthorEntity> findAuthorsWithAgeGreaterThan(int age);
}
