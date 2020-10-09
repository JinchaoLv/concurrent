package per.lvjc.concurrent.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class AQSTest {

    static class MyAQS extends AbstractQueuedSynchronizer {
        public MyAQS() {
            new ConditionObject();
        }
    }

    public class A {
        public class AA {}
    }

    public static void main(String[] args) {

    }
}
