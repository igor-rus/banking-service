package com.example.bankingservice.validation;

@FunctionalInterface
public interface Validator<T> {
    ValidationResult validate(T t);

    default Validator<T> and(Validator<? super T> other) {
        return obj -> {
            ValidationResult result = this.validate(obj);
            return !result.isValid() ? result : other.validate(obj);
        };
    }
}

