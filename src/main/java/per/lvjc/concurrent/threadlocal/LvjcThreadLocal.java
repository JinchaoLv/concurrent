package per.lvjc.concurrent.threadlocal;

import java.util.HashMap;
import java.util.Map;

public class LvjcThreadLocal<T> {

    private Map<Thread, Map<LvjcThreadLocal, Object>> mapMap;
    private T initValue;

    public LvjcThreadLocal() {
        this.mapMap = new HashMap<>();
    }

    public LvjcThreadLocal(T initValue) {
        this.mapMap = new HashMap<>();
        this.initValue = initValue;
    }

    public void set(T value) {
        Thread thread = Thread.currentThread();
        Map<LvjcThreadLocal, Object> currentThreadMap = mapMap.get(thread);
        if (currentThreadMap == null) {
            currentThreadMap = new HashMap<>();
            mapMap.put(thread, currentThreadMap);
        }
        currentThreadMap.put(this, value);
    }

    public T get() {
        Thread thread = Thread.currentThread();
        Map<LvjcThreadLocal, Object> currentThreadMap = mapMap.get(thread);
        if (currentThreadMap == null) {
            return initValue;
        }
        return (T) currentThreadMap.get(this);
    }

    public void remove() {
        Thread thread = Thread.currentThread();
        Map<LvjcThreadLocal, Object> currentThreadMap = mapMap.get(thread);
        if (currentThreadMap != null) {
            currentThreadMap.remove(this);
        }
    }

    public static void main(String[] args) {
        LvjcThreadLocal<String> lvjcThreadLocal = new LvjcThreadLocal<>();
        LvjcThreadLocal<Integer> integerLvjcThreadLocal = new LvjcThreadLocal<>();
        integerLvjcThreadLocal.set(5);
        System.out.println(lvjcThreadLocal.get());
        lvjcThreadLocal.set("xxx");
        System.out.println(lvjcThreadLocal.get());
        lvjcThreadLocal.remove();
        System.out.println(lvjcThreadLocal.get());
        System.out.println(integerLvjcThreadLocal.get());
    }

}
