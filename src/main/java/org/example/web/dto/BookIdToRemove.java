package org.example.web.dto;

import javax.validation.constraints.*;

public class BookIdToRemove {

    @NotNull
    
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
