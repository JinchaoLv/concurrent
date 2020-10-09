package per.lvjc.concurrent.aqs.match;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Synchronizer extends AbstractQueuedSynchronizer {

    private static final int PLAYER_COUNT = 10;

    @Override
    protected int tryAcquireShared(int arg) {
return 0;
    }

    @Override
    protected boolean tryReleaseShared(int arg) {
        return super.tryReleaseShared(arg);
    }
}
