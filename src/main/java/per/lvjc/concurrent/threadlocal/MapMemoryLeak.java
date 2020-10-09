package per.lvjc.concurrent.threadlocal;

import java.util.HashMap;
import java.util.Map;

public class MapMemoryLeak {

    private static Map<Long, byte[]> map = new HashMap<>();

    private static void put(byte[] value) {
        map.put(System.currentTimeMillis(), value);
    }

    public static void main(String[] args) {
        int i = 0;
        while (true) {
            put(new byte[1024 * 1024]);
            System.out.println("i = " + i++);
        }
    }
}
