package ru.hse.java.functional;

public interface Predicate<X> extends Function1<X, Boolean> {
    Predicate<?> ALWAYS_TRUE = x -> true;
    Predicate<?> ALWAYS_FALSE = x -> false;

    default Predicate<X> or(Predicate<? super X> p) {
        return (X x) -> p.use(x) || use(x);
    }

    default Predicate<X> and(Predicate<? super X> p) {
        return (X x) -> p.use(x) && use(x);
    }

    default Predicate<X> not() {
        return (X x) -> !use(x);
    }
}
