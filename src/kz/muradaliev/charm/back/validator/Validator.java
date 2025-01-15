package kz.muradaliev.charm.back.validator;

public interface Validator<T> {

    ValidationResult validate(T object);
}
