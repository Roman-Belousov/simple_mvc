package org.example.web.dto;

import org.example.app.customvalidator.UniqueAuthor;

import javax.validation.constraints.NotEmpty;


@UniqueAuthor
public class BookAuthorToRemove {

    @NotEmpty
    
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
