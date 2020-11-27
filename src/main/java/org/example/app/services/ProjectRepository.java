package org.example.app.services;

import java.util.HashSet;
import java.util.List;

public interface ProjectRepository<T> {

    List<T> retreiveAll();

    HashSet<T> retreiveAllBySearch();

    void store(T book);

    boolean removeItemByAuthor(String bookAuthorToRemove);

    boolean listItemByAuthor(String bookAuthorToList);

    boolean removeItemById(String bookIdToRemove);

}
