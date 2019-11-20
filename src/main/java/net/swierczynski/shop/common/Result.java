package net.swierczynski.shop.common;

import io.vavr.control.Either;

import java.util.Objects;
import java.util.Optional;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

public class Result<T> {

    public static Result<Object> success() {
        return new Result<>(right(new Success<>()));
    }

    public static <T> Result<T> success(T result) {
        return new Result<>(right(new Success<>(result)));
    }

    public static <T> Result<T> failure(String reason) {
        return new Result<>(left(new Failure(reason)));
    }

    private final Either<Failure, Success<T>> result;

    private Result(Either<Failure, Success<T>> result) {
        this.result = result;
    }

    public boolean isFailure() {
        return result.isLeft();
    }

    public boolean isSuccessful() {
        return result.isRight();
    }

    public String reason() {
        if (result.isLeft()) {
            return result.getLeft().reason;
        }
        return "OK";
    }

    public Optional<T> result() {
        if (result.isRight()) {
            return Optional.of(result.get().getResult());
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result1 = (Result) o;
        return Objects.equals(result, result1.result);
    }

}

class Success<T> {

    private final T result;

    Success() {
        this.result = null;
    }

    Success(T result) {
        this.result = result;
    }

    T getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Success success = (Success) o;
        return Objects.equals(result, success.result);
    }

}

class Failure {

    final String reason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Failure failure = (Failure) o;
        return Objects.equals(reason, failure.reason);
    }

    Failure(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Failure{" +
                "reason='" + reason + '\'' +
                '}';
    }
}
