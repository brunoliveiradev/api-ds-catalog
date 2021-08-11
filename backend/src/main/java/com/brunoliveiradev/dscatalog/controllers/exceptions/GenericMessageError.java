package com.brunoliveiradev.dscatalog.controllers.exceptions;

import java.io.Serializable;

public class GenericMessageError implements Serializable {
    private static final long serialVersionUID = 1L;

    private String field;
    private String message;

    public GenericMessageError(String campo, String mensagemErro) {
        this.field = campo;
        this.message = mensagemErro;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
