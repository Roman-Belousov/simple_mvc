package org.example.web.dto;

import org.example.app.customvalidator.SearchUniqueAuthor;
import org.example.app.customvalidator.UniqueAuthor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@SearchUniqueAuthor
public class BookAuthorToSearch {


    @NotEmpty
    private String authorForSearch;

    public String getAuthorForSearch() {
        return authorForSearch;
    }

    public void setAuthorForSearch(String authorForSearch) {
        this.authorForSearch = authorForSearch;
    }

    @Override
    public String toString() {
        return "BookAuthorToSearch{" +
                "authorForSearch='" + authorForSearch + '\'' +
                '}';
    }
}
