package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    //private final List<Book> repo = new ArrayList<>();
    private List<Book> repoBySearch = new ArrayList<>();
    private ApplicationContext context;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });

        return new ArrayList<>(books);
    }

    @Override
    public HashSet<Book> retreiveAllBySearch() {
        return new HashSet<>(repoBySearch);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)", parameterSource);

        logger.info("store new book: " + book);
//        if (book.getSize() != null && !book.getAuthor().isBlank() && !book.getTitle().isBlank()) {
//            // repo.add(book);
//        } else {
//            return;
//        }
    }

    @Override
    public boolean removeItemByAuthor(String bookAuthorToRemove) {
        int count = 0;
        for (Book book : retreiveAll()) {
            if (book.getAuthor().equals(bookAuthorToRemove) || book.getTitle().equals(bookAuthorToRemove)) {
                logger.info("remove book completed: " + book);
                //repo.remove(book);
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
            if (repoBySearch.size() < book.getSize()) {
                repoBySearch.add(book);
            }
        }
        int count = 1;
        logger.info("add book in temp repo completed: " + repoBySearch.size());
        for (Book book : retreiveAllBySearch()) {
            if (!book.getAuthor().equals(bookAuthorToList) && !book.getTitle().equals(bookAuthorToList)) {
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
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
//        int count = 0;
//        for (Book book : retreiveAll()) {
//            if (book.getId().equals(bookIdToRemove) || book.getSize().equals(bookIdToRemove)) {
        logger.info("remove book completed");
        // repo.remove(book);
//                count++;
//            }
//        }
//        if (count == 0) {
//            return false;
//        } else {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void defaultInit() {
        logger.info("default INIT in book repo bean");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in book repo bean");
    }
}