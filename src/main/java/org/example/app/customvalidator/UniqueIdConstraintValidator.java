package org.example.app.customvalidator;

import org.apache.log4j.Logger;
import org.example.app.services.BookRepository;
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
        logger.info("Custom validator isValid " + bookIdToRemove.getId());
        int count = 0;
        for (Book book : bookRepository.retreiveAll()) {
            if (book.getId().equals(bookIdToRemove.getId()) || book.getPagesize().equals(bookIdToRemove.getId())) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(
                        "{My custom validator}")
                        .addPropertyNode("id").addConstraintViolation();
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













