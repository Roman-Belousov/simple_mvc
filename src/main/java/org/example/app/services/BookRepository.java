package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private  List<Book> repoBySearch = new ArrayList<>();
    private ApplicationContext context;

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
        book.setId(context.getBean(IdProvider.class).provideId(book));
         logger.info("store new book: " + book);
        if (book.getSize() != null && !book.getAuthor().isBlank() && !book.getTitle().isBlank()) {
            repo.add(book);
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
        logger.info("add book in temp repo completed: "  + repoBySearch.size());
        for(Book book : retreiveAllBySearch()){
                    if(!book.getAuthor().equals(bookAuthorToList) && !book.getTitle().equals(bookAuthorToList)){
                        repoBySearch.remove(book);
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
    public boolean removeItemById(String bookIdToRemove) {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
    private void defaultInit(){
        logger.info("default INIT in book repo bean");
    }

    private void defaultDestroy(){
        logger.info("default DESTROY in book repo bean");
    }
}