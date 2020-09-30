package com.laffuste.inventorio.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Model Not Found")
public class ModelNotFoundException extends Exception {

    private static final String MESSAGE = "%s with id %s not found";

    public ModelNotFoundException(Class<?> model, Long id) {
        super(String.format(MESSAGE, getEntityFriendlyName(model), id));
    }

    private static String getEntityFriendlyName(Class<?> model) {
        return model.getSimpleName().replace("Entity", "");
    }

}
