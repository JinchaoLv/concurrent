package per.lvjc.concurrent.cas;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceTest {

    private static class Foo {
        public Foo(int i) {
            this.i = i;
        }
        private int i = 0;
    }

    private static AtomicMarkableReference<Foo> atomicFoo = new AtomicMarkableReference<>(new Foo(1), false);

    public static void main(String[] args) {
        //线程 1 读取旧值
        boolean marked = atomicFoo.isMarked();
        Foo old = atomicFoo.getReference();
        //模拟线程 2 将 A 改为 B
        atomicFoo.set(new Foo(2), !marked);
        //模拟线程 1 的 CAS 更新
        boolean success = atomicFoo.compareAndSet(old, new Foo(5), marked, !marked);
        System.out.println(success);
        //模拟线程 2 将 B 再改回 A
        atomicFoo.set(old, !marked);
        //atomicFoo.set(old, marked);
        //模拟线程 1 的 CAS 更新
        success = atomicFoo.compareAndSet(old, new Foo(6), marked, !marked);
        System.out.println(success);
    }
}
