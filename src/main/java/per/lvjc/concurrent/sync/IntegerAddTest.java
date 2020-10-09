package per.lvjc.concurrent.sync;

public class IntegerAddTest {

    private static Integer integer = 0;

    public static void main(String[] args) {
        System.out.println("hash code: " + System.identityHashCode(integer));
        System.out.println("hash code: " + System.identityHashCode(integer));
        for (int i = 0; i < 5; i++) {
            integer++;
            System.out.println("hash code: " + System.identityHashCode(integer));
        }
        Object o = null;
        synchronized (o) {
            System.out.println("xxx");
        }
    }
}
