package org.example.app.services;

import org.example.web.dto.Book;

import java.util.List;

public interface ProjectRepository<T> {

    List<T> retreiveAll();

    void store(T book);

    List<T> searchItemByAuthor(String bookAuthorToSearch);

    boolean removeItemByAuthor(String bookAuthorToRemove);

    boolean removeItemById(Integer bookIdToRemove);

}
