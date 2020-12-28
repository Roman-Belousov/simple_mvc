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
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private ApplicationContext context;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        logger.info("RetreiveAll start");
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setPagesize(rs.getInt("pagesize"));
            return book;
        });

        return new ArrayList<>(books);
    }

    @Override
    public List<Book> searchItemByAuthor(String bookAuthorToSearch) {
        logger.info("searchItemByAuthor start");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("author", bookAuthorToSearch);
        namedParameters.addValue("title", bookAuthorToSearch);
        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE author = :author OR title = :title", namedParameters,
                (ResultSet rs, int rowNum) -> {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setAuthor(rs.getString("author"));
                    book.setTitle(rs.getString("title"));
                    book.setPagesize(rs.getInt("pagesize"));
                    return book;
                });

        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        logger.info("store start");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("pagesize", book.getPagesize());
        jdbcTemplate.update("INSERT INTO books(author,title,pagesize) VALUES(:author, :title, :pagesize)", parameterSource);

    }


    @Override
    public boolean removeItemByAuthor(String bookAuthorToRemove) {
        logger.info("removeItemByAuthor start");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", bookAuthorToRemove);
        parameterSource.addValue("title", bookAuthorToRemove);
        for (Book book : retreiveAll()) {
            if (book.getAuthor().equals(bookAuthorToRemove)) {
                jdbcTemplate.update("DELETE FROM books WHERE author = :author", parameterSource);
                logger.info("remove bookByAuthor completed");
            } else {
                if (book.getTitle().equals(bookAuthorToRemove))
                    jdbcTemplate.update("DELETE FROM books WHERE title = :title ", parameterSource);
                logger.info("remove bookByTitle completed");
            }
        }
        return true;
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        logger.info("removeItemById start");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        parameterSource.addValue("pagesize", bookIdToRemove);
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                jdbcTemplate.update("DELETE FROM books WHERE id = :id ", parameterSource);
                logger.info("remove bookByID completed");
            } else {
                if (book.getPagesize().equals(bookIdToRemove))
                    jdbcTemplate.update("DELETE FROM books WHERE pagesize = :pagesize ", parameterSource);
                logger.info("remove bookByPageSize completed");
            }
        }
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