package ru.hse.java.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections {
    public static <X, Y> List<Y> map(Function1<? super X, ? extends Y> f, Collection<? extends X> a) {
        List<Y> buffer = new ArrayList<>(a.size());
        for (X el : a) {
            buffer.add(f.use(el));
        }
        return buffer;
    }

    public static <X> List<X> filter(Predicate<? super X> p, Collection<? extends X> a) {
        List<X> buffer = new ArrayList<>(a.size());
        for (X el : a) {
            if (p.use(el)) {
                buffer.add(el);
            }
        }
        return buffer;
    }

    public static <X> List<X> takeWhile(Predicate<? super X> p, Collection<? extends X> a) {
        List<X> buffer = new ArrayList<>(a.size());
        for (X el : a) {
            if (p.use(el)) {
                buffer.add(el);
            } else {
                break;
            }
        }
        return buffer;
    }

    public static <X> List<X> takeUnless(Predicate<? super X> p, Collection<? extends X> a) {
        return takeWhile(p.not(), a);
    }

    public static <X, Y, T extends Y> Y foldr(Function2<? super X, ? super Y, ? extends Y> f, T init, Collection<? extends X> a) {
        List<? extends X> list = new ArrayList<>(a);
        java.util.Collections.reverse(list);
        Y result = init;
        for (X el : list) {
            result = f.use(el, result);
        }
        return result;
    }

    public static <X, Y, T extends X> X foldl(Function2<? super X, ? super Y, ? extends X> f, T init, Collection<? extends Y> a) {
        X result = init;
        for (Y el : a) {
            result = f.use(result, el);
        }
        return result;
    }
}
