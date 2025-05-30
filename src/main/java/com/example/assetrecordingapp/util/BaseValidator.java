package com.example.assetrecordingapp.util;

import java.util.Objects;

public interface BaseValidator {

    default void equals(Object a, Object b) {
        if (!Objects.equals(a, b)) {
            throw buildException();
        }
    }

    default void equals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw buildException(message);
        }
    }

    default void isTrue(boolean expression) {
        if (!expression) {
            throw buildException();
        }
    }

    default void isTrue(boolean expression, String message) {
        if (!expression) {
            throw buildException(message);
        }
    }

    default void isNull(Object object) {
        if (object != null) {
            throw buildException();
        }
    }

    default void isNull(Object object, String message) {
        if (object != null) {
            throw buildException(message);
        }
    }

    default void isNotNull(Object object) {
        if (object == null) {
            throw buildException();
        }
    }

    default void isNotNull(Object object, String message) {
        if (object == null) {
            throw buildException(message);
        }
    }

    RuntimeException buildException();

    RuntimeException buildException(String message);
}
