package utils;

/**
 * Created by sacry on 16/05/14.
 */
public class Tuple2<T, R> {

    private T t;
    private R r;

    public static <T, R> Tuple2<T, R> valueOf(T t, R r) {
        return new Tuple2<T, R>(t, r);
    }

    private Tuple2(T t, R r) {
        this.t = t;
        this.r = r;
    }

    public T _1() {
        return this.t;
    }

    public R _2() {
        return this.r;
    }
}
