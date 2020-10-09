package per.lvjc.concurrent.aqs;

public class ParkTest {

    public static void main(String[] args) {
        int i = 3 << 16;
        int j = (1 << 16) - 1;
        System.out.println(j);
        System.out.println(Integer.toBinaryString(j));
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));
    }
}
