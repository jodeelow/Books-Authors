package com.project.BooksAndAuthors.repositories;

import com.project.BooksAndAuthors.TestDataUtil;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
//clears the context after every test method
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorEntityRepositoryIntegrationTests
{
    private AuthorRepository underTests;

    @Autowired
    private AuthorEntityRepositoryIntegrationTests(AuthorRepository underTests)
    {
        //constructor dependency injection
        this.underTests = underTests;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndFound()
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();

        this.underTests.save(authorEntity);
        Optional<AuthorEntity> foundAuthor = this.underTests.findById(authorEntity.getId());

        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndFound()
    {
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor1();
        this.underTests.save(authorEntity1);
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthor2();
        this.underTests.save(authorEntity2);
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthor3();
        this.underTests.save(authorEntity3);

        Iterable<AuthorEntity> results = this.underTests.findAll();
        assertThat(results).hasSize(3);
        assertThat(results).containsExactly(
                authorEntity1,
                authorEntity2,
                authorEntity3
        );
    }

    @Test
    public void testThatAuthorCanBeUpdated()
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        this.underTests.save(authorEntity);
        authorEntity.setName("Updated Author");

        //update the author name in DB
        this.underTests.save(authorEntity);

        //search for author in DB and retrieve him
        Optional<AuthorEntity> foundAuthor = this.underTests.findById(authorEntity.getId());

        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get()).isEqualTo(authorEntity);

    }

    @Test
    public void testThatAuthorCanBeDeleted()
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        this.underTests.save(authorEntity);

        this.underTests.deleteById(authorEntity.getId());
        Optional<AuthorEntity> foundAuthor = this.underTests.findById(authorEntity.getId());

        assertThat(foundAuthor).isNotPresent();
    }

    @Test
    public void testThatAuthorsWithAgeLessThanCanBeFound()
    {
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor1();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthor2();
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthor3();

        this.underTests.save(authorEntity1);
        this.underTests.save(authorEntity2);
        this.underTests.save(authorEntity3);

        Iterable<AuthorEntity> foundAuthors = this.underTests.findByAgeLessThan(20);

        assertThat(foundAuthors).hasSize(2);
        assertThat(foundAuthors).containsExactly(
                authorEntity1,
                authorEntity2
        );
    }

    @Test
    public void testThatAuthorsWithAgeGreaterThanCanBeFound()
    {
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor1();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthor2();
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthor3();

        this.underTests.save(authorEntity1);
        this.underTests.save(authorEntity2);
        this.underTests.save(authorEntity3);

        Iterable<AuthorEntity> foundAuthors = this.underTests.findAuthorsWithAgeGreaterThan(19);

        assertThat(foundAuthors).hasSize(1);
        assertThat(foundAuthors).containsExactly(
                authorEntity3
        );
     }

}
