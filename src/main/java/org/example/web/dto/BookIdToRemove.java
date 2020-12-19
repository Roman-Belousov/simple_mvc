package org.example.web.dto;

import org.example.app.customvalidator.UniqueId;

import javax.validation.constraints.*;

@UniqueId
public class BookIdToRemove {

    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
