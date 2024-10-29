package com.project.BooksAndAuthors.mappers;

//interface for mapping from dto to object and vice versa
public interface Mapper<A, B>
{
    B mapTo(A a);
    A mapFrom(B b);
}
