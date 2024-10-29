package com.project.BooksAndAuthors.repositories;

import com.project.BooksAndAuthors.TestDataUtil;
import com.project.BooksAndAuthors.domain.entities.AuthorEntity;
import com.project.BooksAndAuthors.domain.entities.BookEntity;
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
//clears the context after each test method
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests
{

    private BookRepository underTests;

    @Autowired
    private BookEntityRepositoryIntegrationTests(BookRepository underTests)
    {
        this.underTests = underTests;
    }

    @Test
    public void testThatBookCanBeCreatedAndFound()
    {
        //inserted automatically in the DB because of the cascade turned on
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();

        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        this.underTests.save(bookEntity);

        Optional<BookEntity> foundBook = this.underTests.findById(bookEntity.getIsbn());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndFound()
    {
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor1();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthor2();
        AuthorEntity authorEntity3 = TestDataUtil.createTestAuthor3();

        BookEntity bookEntity1 = TestDataUtil.createTestBook(authorEntity1);
        BookEntity bookEntity2 = TestDataUtil.createTestBook1(authorEntity2);
        BookEntity bookEntity3 = TestDataUtil.createTestBook2(authorEntity3);

        this.underTests.save(bookEntity1);
        this.underTests.save(bookEntity2);
        this.underTests.save(bookEntity3);

        Iterable<BookEntity> results = this.underTests.findAll();
        assertThat(results).hasSize(3);
        assertThat(results).containsExactly(
                bookEntity1,
                bookEntity2,
                bookEntity3
        );


    }

    @Test
    public void testThatBookCanBeUpdated()
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);

        //insert book in database
        this.underTests.save(bookEntity);

        bookEntity.setTitle("Updated Title");

        //update book's name
        this.underTests.save(bookEntity);
        //get updated book
        Optional<BookEntity> foundBook = this.underTests.findById(bookEntity.getIsbn());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatBookCanBeDeleted()
    {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor1();
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);

        //insert book in h2 database
        this.underTests.save(bookEntity);

        this.underTests.deleteById(bookEntity.getIsbn());

        Optional<BookEntity> foundBook = this.underTests.findById(bookEntity.getIsbn());
        assertThat(foundBook).isNotPresent();
    }
}
