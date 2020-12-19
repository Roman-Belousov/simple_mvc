package org.example.app.customvalidator;

import org.apache.log4j.Logger;
import org.example.app.services.BookRepository;
import org.example.app.services.BookService;
import org.example.web.controllers.BookShelfController;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueIdConstraintValidator implements ConstraintValidator<UniqueId, BookIdToRemove> {

    private Logger logger = Logger.getLogger(UniqueIdConstraintValidator.class);

    @Autowired
    private BookRepository bookRepository;

    public UniqueIdConstraintValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void initialize(UniqueId uniqueId) {

    }

    @Override
    public boolean isValid(BookIdToRemove bookIdToRemove, ConstraintValidatorContext ctx) {
        for (Book book : bookRepository.retreiveAll()) {
            if (!book.getId().equals(bookIdToRemove.getId()) && !book.getPagesize().equals(bookIdToRemove.getId())) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(
                        "{My custom validator}")
                        .addPropertyNode("id").addConstraintViolation();
                logger.info("Costom validator return false " + book.getPagesize() + " " + bookIdToRemove.getId());
                return false;
            }
        }
        logger.info("Costom validator return true ");
        return true;
    }
}





