package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private  List<Book> repo = new ArrayList<>();
    private  List<Book> repoBySearch = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public HashSet<Book> retreiveAllBySearch() {
        return new HashSet<>(repoBySearch);
    }

    @Override
    public void store(Book book) {
        book.setId(book.hashCode());
        // logger.info("store new book: " + book);
        if (book.getSize() != null && !book.getAuthor().isBlank() && !book.getTitle().isBlank()) {
            repo.add(book);
            logger.info("store new book: " + book);

        } else {
            return;
        }
    }

    @Override
    public boolean removeItemByAuthor(String bookAuthorToRemove) {
        int count = 0;
        for (Book book : retreiveAll()) {
            if (book.getAuthor().equals(bookAuthorToRemove) || book.getTitle().equals(bookAuthorToRemove)) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
                count++;
            }
        }
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean listItemByAuthor(String bookAuthorToList) {
      repoBySearch.clear();
        for (Book book : retreiveAll()) {
            if (repoBySearch.size() < repo.size()) {
                repoBySearch.add(book);
            }
        }
    int count = 1;
        logger.info("add book completed: "  + repoBySearch.size());
        for(Book book : retreiveAllBySearch()){
                    if(!book.getAuthor().equals(bookAuthorToList)){
                        repoBySearch.remove(book);
                    logger.info("add book completed: " + book + repoBySearch.size());
                    count++;
                }
        }
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        int count = 0;
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove) || book.getSize().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
                count++;
            }
        }
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }
}