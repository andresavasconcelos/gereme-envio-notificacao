package com.br.project.gereme.enums;

import com.br.project.gereme.entity.Status;

public enum StatusEnum {
    PENDING(1L, "pending"),
    SUCESSES(2L, "success"),
    ERROR(3L, "error"),
    CANCELED(4L, "canceled");

    private Long id;
    private String description;


    StatusEnum(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Status toStatus(){
        return new Status(id, description);
    }
}
