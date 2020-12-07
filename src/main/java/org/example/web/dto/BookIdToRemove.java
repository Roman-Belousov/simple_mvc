package org.example.web.dto;

import javax.validation.constraints.*;

public class BookIdToRemove {

    @NotEmpty
    @Size(min = 18, max = 21)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
