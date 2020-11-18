package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {

    List<T> retreiveAll();

    void store(T book);

    boolean removeItemByAuthor(String bookAuthorToRemove);

    boolean removeItemById(Integer bookIdToRemove);

}
