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
@Table(name = "authors")
public class AuthorEntity
{
    //id is the primary key(it is generated automatically(growing sequence))
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    @SequenceGenerator(name = "author_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private Integer age;
}
