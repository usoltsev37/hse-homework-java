package ru.hse.java.functional;

public interface Function1<X, Y> {
    Y use(X x);

    default <Z> Function1<X, Z> compose(Function1<? super Y, ? extends Z> g) {
        return (X x) -> g.use(use(x));
    }
}
