package per.lvjc.concurrent.aqs.my;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class LvjcNonfairSemaphore {

    private final Sync sync;

    public LvjcNonfairSemaphore(int available) {
        sync = new Sync(available);
    }

    public void acquire() {

    }

    public void release() {

    }

    private static final class Sync extends AbstractQueuedSynchronizer {

        Sync(int available) {
            setState(available);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return 0;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
return false;
        }
    }


}
