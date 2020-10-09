package per.lvjc.concurrent.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicReferenceTest {

    private static int x = 0;
    private static int y = 0;

    private static AtomicInteger atomicX = new AtomicInteger(0);
    private static AtomicInteger atomicY = new AtomicInteger(0);

    private static class Foo {
        int fooX = 0;
        int fooY = 0;
    }

    private static Foo foo = new Foo();

    public static void main(String[] args) throws InterruptedException {
        foo1();
        //foo2();
    }

    private static void foo1() throws InterruptedException {
        for (int i = 1; i <= 20; i++) {
            final int count = i;
            new Thread(() -> {
                x = x + count;
                y = y + count;
            }).start();
        }
        System.out.println("x = " + x);
        System.out.println("y = " + y);
    }

    private static void foo2() throws InterruptedException {
        for (int i = 1; i <= 20; i++) {
            final int count = i;
            new Thread(() -> {
                atomicX.getAndAdd(count);
                atomicY.getAndAdd(count);
            }).start();
        }
        System.out.println("x = " + atomicY);
        System.out.println("y = " + atomicY);
    }

    private static void foo3() {
        for (int i = 1; i <= 20; i++) {
            final int count = i;
            new Thread(() -> {
                atomicX.getAndAdd(count);
                atomicY.getAndAdd(count);
            }).start();
        }
        System.out.println("x = " + atomicY);
        System.out.println("y = " + atomicY);
    }


}
