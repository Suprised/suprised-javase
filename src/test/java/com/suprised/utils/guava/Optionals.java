package com.suprised.utils.guava;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import static com.google.common.base.Optional.absent;

/**
 * 可以使用FluentOptional代替
 */
public class Optionals {
    public static <T, U> Optional<U> bind(Optional<T> value,
                                          Function<T, Optional<U>> function) {
        if (value.isPresent()) {
            return function.apply(value.get());
        }
        return absent();
    }

    private Optionals() {}
}