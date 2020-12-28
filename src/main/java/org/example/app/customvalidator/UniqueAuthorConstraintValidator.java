package org.example.app.customvalidator;

import org.apache.log4j.Logger;
import org.example.app.services.BookRepository;
import org.example.web.dto.Book;
import org.example.web.dto.BookAuthorToRemove;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueAuthorConstraintValidator implements ConstraintValidator<UniqueAuthor, BookAuthorToRemove> {

    private Logger logger = Logger.getLogger(UniqueAuthorConstraintValidator.class);

    @Autowired
    private BookRepository bookRepository;

    public UniqueAuthorConstraintValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void initialize(UniqueAuthor uniqueAuthor) {

    }

    @Override
    public boolean isValid(BookAuthorToRemove bookAuthorToRemove, ConstraintValidatorContext ctx) {
        int count = 0;
        for (Book book : bookRepository.retreiveAll()) {
            if (book.getAuthor().equals(bookAuthorToRemove.getAuthor()) || book.getTitle().equals(bookAuthorToRemove.getAuthor())) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(
                        "{My custom validator}")
                        .addPropertyNode("author").addConstraintViolation();
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





