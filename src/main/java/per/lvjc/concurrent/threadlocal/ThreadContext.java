package per.lvjc.concurrent.threadlocal;

public class ThreadContext {

    private static final LvjcThreadLocal<String> threadLocal = new LvjcThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set("some values");
        print();
    }

    private static void print() {
        printA();
    }

    private static void printA() {
        printB();
    }

    private static void printB() {
        printC();
    }

    private static void printC() {
        printX();
    }

    private static void printX() {
        //旧业务逻辑
        System.out.println("old");
        //添加新的业务逻辑
        String value = threadLocal.get();
        System.out.println("new:" + value);
    }
}
