package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.BookAuthorToSearch;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
   // private List<Book> repoBySearch = new ArrayList<>();
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
            book.setPagesize(rs.getInt("pagesize"));
            return book;
        });

        return new ArrayList<>(books);
    }

    @Override
    public List<Book> searchItemByAuthor(String bookAuthorToSearch) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("author", bookAuthorToSearch);
        logger.info("has parametr" + " " + namedParameters.getValue("author"));
        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE author = :author", namedParameters,
                (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            logger.info("create new book: " + book);
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setPagesize(rs.getInt("pagesize"));
            logger.info("search new book: " + book);
            return book;
        });

        return new ArrayList<>(books);
    }
    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("pagesize", book.getPagesize());


        jdbcTemplate.update("INSERT INTO books(author,title,pagesize) VALUES(:author, :title, :pagesize)", parameterSource);

        logger.info("store new book: " + book);

    }
//    public class BookRowMapper implements RowMapper
//    {
//        public ArrayList<Book> mapRow(ResultSet rs, int rowNum) throws SQLException {
//            Book book= new Book();
//            logger.info("create new book");
//            book.setId(rs.getInt("id"));
//            book.setAuthor(rs.getString("author"));
//            book.setTitle(rs.getString("title"));
//            book.setPagesize(rs.getInt("pagesize"));
//            logger.info("create new book" + " " + book);
//            return new ArrayList<>();
//        }
//
//    }
//    @Override
//    public List<Book> searchItemByAuthor(String bookAuthorToSearch) {
//        String sql = "SELECT *  FROM books WHERE author = ?";
//        logger.info("presearch book" + " " + bookAuthorToSearch);
//        Book book = (Book) jdbcTemplate.getJdbcTemplate().queryForObject(
//                sql, new Object[]{ bookAuthorToSearch }, new BookRowMapper());
//            return new ArrayList<>();
 //       }


    @Override
    public boolean removeItemByAuthor(String bookAuthorToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", bookAuthorToRemove);
        parameterSource.addValue("title", bookAuthorToRemove);
        for (Book book : retreiveAll()) {
            if (book.getAuthor().equals(bookAuthorToRemove)) {
                jdbcTemplate.update("DELETE FROM books WHERE author = :author", parameterSource);
            } else {
                if (book.getTitle().equals(bookAuthorToRemove))
                    jdbcTemplate.update("DELETE FROM books WHERE title = :title ", parameterSource);
                logger.info("remove book completed");
            }
        }
            return true;
        }


    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        parameterSource.addValue("pagesize", bookIdToRemove);
        logger.info("remove book precompleted");
        int count = 0;
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                jdbcTemplate.update("DELETE FROM books WHERE id = :id ", parameterSource);
                logger.info("remove bookbyID precompleted");
            }else{
              if (book.getPagesize().equals(bookIdToRemove))
                    jdbcTemplate.update("DELETE FROM books WHERE pagesize = :pagesize ", parameterSource);
                logger.info("remove bookbySIZE precompleted");
                count++;
                logger.info("remove book completed");
            }
        }
            if (count == 0) {
                logger.info("remove book notcompleted");
                return false;
            } else {
                return true;
            }
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