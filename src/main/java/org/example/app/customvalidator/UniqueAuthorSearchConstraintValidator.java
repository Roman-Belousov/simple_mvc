package org.example.app.customvalidator;

import org.apache.log4j.Logger;
import org.example.app.services.BookRepository;
import org.example.web.dto.Book;
import org.example.web.dto.BookAuthorToRemove;
import org.example.web.dto.BookAuthorToSearch;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueAuthorSearchConstraintValidator implements ConstraintValidator<SearchUniqueAuthor, BookAuthorToSearch> {

    private Logger logger = Logger.getLogger(UniqueAuthorSearchConstraintValidator.class);

    @Autowired
    private BookRepository bookRepository;

    public UniqueAuthorSearchConstraintValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void initialize(SearchUniqueAuthor searchUniqueAuthor) {

    }

    @Override
    public boolean isValid(BookAuthorToSearch bookAuthorToSearch, ConstraintValidatorContext ctx) {
        int count = 0;
        for (Book book : bookRepository.retreiveAll()) {
            if (book.getAuthor().equals(bookAuthorToSearch.getAuthorForSearch())
                    || book.getTitle().equals(bookAuthorToSearch.getAuthorForSearch())) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(
                        "{My custom validator}")
                        .addPropertyNode("authorForSearch").addConstraintViolation();
                count++;
            }
        }
        if (count == 0) {
            logger.info("Custom validator return false " + count);
            return false;
        } else {
            logger.info("Costom validator return true ");
            return true;
        }
    }
}






