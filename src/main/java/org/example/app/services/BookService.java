package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;
    private final ProjectRepository<Book> bookRepoBySearch;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo, ProjectRepository<Book> bookRepoBySearch) {
        this.bookRepo = bookRepo;
        this.bookRepoBySearch = bookRepoBySearch;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public HashSet<Book> getAllBooksBySearch() {
        return bookRepoBySearch.retreiveAllBySearch();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookByAuthor(String bookAuthorToRemove) {
        return bookRepo.removeItemByAuthor(bookAuthorToRemove);
    }

    public boolean listBookByAuthor(String bookAuthorToList) {
        return bookRepo.listItemByAuthor(bookAuthorToList);
    }

    public boolean removeBookById(String bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    private void defaultInit(){
        logger.info("default INIT in book service");
    }

    private void defaultDestroy(){
        logger.info("default DESTROY in book service");
    }



}