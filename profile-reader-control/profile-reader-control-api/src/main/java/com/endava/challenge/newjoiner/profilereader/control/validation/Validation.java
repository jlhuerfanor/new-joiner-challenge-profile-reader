package com.endava.challenge.newjoiner.profilereader.control.validation;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Validation<T> {

    T validate(T obj);

    static <T> MultiStepValidation<T> create() {
        return new MultiStepValidation<>();
    }

    class MultiStepValidation<T> implements Validation<T> {
        private Predicate<T> predicate;
        Function<T, ? extends ValidationException> exceptionProvider;

        public MultiStepValidation<T> when(Predicate<T> initialPredicate) {
            this.predicate = initialPredicate;
            return this;
        }

        public MultiStepValidation<T> and(Predicate<T> predicate) {
            this.predicate = this.predicate.and(predicate);
            return this;
        }

        public MultiStepValidation<T> or(Predicate<T> predicate) {
            this.predicate = this.predicate.or(predicate);
            return this;
        }

        public MultiStepValidation<T> then(Function<T, ? extends ValidationException> exceptionProvider) {
            this.exceptionProvider = exceptionProvider;
            return this;
        }

        @Override
        public T validate(T obj) {
            if(this.predicate.test(obj)) {
                throw this.exceptionProvider.apply(obj);
            }
            return obj;
        }
    }

    class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }

        public static <T> Function<T, ValidationException> withMessage(String message) {
            return obj -> new ValidationException(message);
        }

        public static <T> Function<T, ValidationException> withMessage(Function<T, String> formatter) {
            return obj -> new ValidationException(formatter.apply(obj));
        }
    }
}
