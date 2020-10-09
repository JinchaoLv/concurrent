package per.lvjc.concurrent.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {

    private static class Foo {
        public Foo(int i) {
            this.i = i;
        }
        private int i = 0;
    }

    private static AtomicStampedReference<Foo> atomicFoo = new AtomicStampedReference<>(new Foo(1), 1);

    public static void main(String[] args) {
        //线程 1 读取旧值
        int version = atomicFoo.getStamp();
        Foo old = atomicFoo.getReference();
        //模拟线程 2 将 A 改为 B
        atomicFoo.set(new Foo(2), version + 1);
        //模拟线程 1 的 CAS 更新
        boolean success = atomicFoo.compareAndSet(old, new Foo(5), version, version + 1);
        System.out.println(success);
        //模拟线程 2 将 B 再改回 A
        atomicFoo.set(old, version + 1);
        //atomicFoo.set(old, version);
        //模拟线程 1 的 CAS 更新
        success = atomicFoo.compareAndSet(old, new Foo(6), version, version + 1);
        System.out.println(success);
    }
}
