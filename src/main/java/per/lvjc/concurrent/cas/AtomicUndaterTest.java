package per.lvjc.concurrent.cas;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicUndaterTest {

    private static class Foo {
        public Foo(int i) {
            this.i = i;
        }
        public volatile int i = 0;
    }

    public static void main(String[] args) {
        Foo foo = new Foo(1);
        AtomicIntegerFieldUpdater<Foo> atomicUndater = AtomicIntegerFieldUpdater.newUpdater(Foo.class, "i");
        boolean success = atomicUndater.compareAndSet(foo, 0, 2);
        System.out.println(success);
    }
}
