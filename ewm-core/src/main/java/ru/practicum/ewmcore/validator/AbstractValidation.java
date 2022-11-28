package ru.practicum.ewmcore.validator;

public abstract class AbstractValidation {
    protected void validationSpacesInStringField(String field) {
        field = field != null ? field.trim() : null;
    }
}
