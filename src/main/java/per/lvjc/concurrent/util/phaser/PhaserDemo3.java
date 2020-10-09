package per.lvjc.concurrent.util.phaser;

import java.util.concurrent.Phaser;

public class PhaserDemo3 {

    private static Phaser phaser = new Phaser(2);

    public static void main(String[] args) {
        // arrive 的是第 0 个屏障，因此 arrive = 0
        int arrive = phaser.arrive();
        // 当前是第 0 个阶段，因此 phase = 0
        int phase = phaser.getPhase();
        System.out.println("arrive = " + arrive);
        System.out.println("phase = " + phase);
        System.out.println("-----------");
        // 因为需要 arrive 两个才能越过屏障，所以上面第一次 arrive 没有越过屏障，当前仍处于第 0 阶段
        // 第 2 次 arrive，此时到达的仍是第 0 阶段的屏障，因此 arrive = 0
        arrive = phaser.arrive();
        // 第 2 次 arrive 之后，已经越过屏障，进入了第 1 阶段，因此 phase = 1
        phase = phaser.getPhase();
        System.out.println("arrive = " + arrive);
        System.out.println("phase = " + phase);
    }
}
