package per.lvjc.concurrent.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>(4, 1, 1);

        for(int i = 0; i < 100; i++) {
            int key = i * 8 + 1;
            String value = "v" + key;
            map.put(key, value);
        }
        map.put(3, "v3");
//        for (int i = 8; i >= 0; i--) {
//            int key = i * 64 + 1;
//            map.remove(key);
//        }
        Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
