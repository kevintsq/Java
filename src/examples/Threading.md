```Java
/**
 * Semaphore
 */
class ZeroEvenOdd {
    private int n;
    private int count = 0;

    private final Semaphore sigZero = new Semaphore(1);
    private final Semaphore sigOdd = new Semaphore(0);
    private final Semaphore sigEven = new Semaphore(0);

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (count <= n) {
            sigZero.acquire();
            count = count + 1;
            if (count > n) {
                sigEven.release();
                sigOdd.release();
                return;
            }
            printNumber.accept(0);

            if (count % 2 == 0) {
                sigEven.release();
            } else {
                sigOdd.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (count <= n) {
            sigEven.acquire();
            if (count > n) {
                sigZero.release();
                return;
            }
            printNumber.accept(count);
            sigZero.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (count <= n) {
            sigOdd.acquire();
            if (count > n) {
                sigZero.release();
                return;
            }
            printNumber.accept(count);
            sigZero.release();
        }
    }
}


/**
 * Wait-notify
 */
class ZeroEvenOdd {
    private int n;
    private boolean zero;
    private int count;

    private final Object lock;

    public ZeroEvenOdd(int n) {
        this.n = n;
        zero = true;
        count = 1;
        lock = new Object();
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (count <= n) {
            synchronized (lock) {
                while (count <= n && !zero)
                    lock.wait();
                if (zero && count <= n) {
                    printNumber.accept(0);
                    zero = false;
                }
                lock.notifyAll();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (count <= n) {
            synchronized (lock) {
                while (count <= n && (zero || count % 2 != 0))
                    lock.wait();
                if (count <= n) {
                    printNumber.accept(count);
                    count = count + 1;
                    zero = true;
                }
                lock.notifyAll();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (count <= n) {
            synchronized (lock) {
                while (count <= n && (zero || count % 2 == 0))
                    lock.wait();
                if (count <= n) {
                    printNumber.accept(count);
                    count = count + 1;
                    zero = true;
                }
                lock.notifyAll();
            }
        }
    }
}
```