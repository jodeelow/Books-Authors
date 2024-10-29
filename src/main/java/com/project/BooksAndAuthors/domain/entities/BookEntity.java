package com.project.BooksAndAuthors.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//making this an entity
@Entity
@Table(name = "books")
public class BookEntity
{
    @Id
    private String isbn;

    private String title;

    //cascade - take book => retrieve the author too; every change is persisted in the db
    //cascade - inserting book with author that doesn't exist => author is CREATED IN DB!!!
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private AuthorEntity authorEntity;
}
