package ru.hse.java.functional;

public interface Function2<X, Y, Z> {
    Z use(X x, Y y);

    default <R> Function2<X, Y, R> compose(Function1<? super Z, ? extends R> g) {
        return (X x, Y y) -> g.use(use(x, y));
    }

    default Function1<Y, Z> bind1(X x) {
        return (Y y) -> use(x, y);
    }

    default Function1<X, Z> bind2(Y y) {
        return (X x) -> use(x, y);
    }

    default Function1<X, Function1<Y, Z>> curry() {
        return (X x) -> ((Y y) -> use(x, y));
    }
}
