import factory.ClientFactory;
import factory.MockClient;
import pool.ClientPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestObjectPool {
    public static void main(String[] args) throws Exception {
        ClientFactory factory = new ClientFactory();
        ClientPool pool = new ClientPool(5, factory);
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            doSomethingUseClientPool(pool);
        }

        long time2 = System.currentTimeMillis();

        for (int i = 0; i < 1000 ; i++) {
            doSomethingNotUsePool();
        }

        long time3 = System.currentTimeMillis();

        System.out.println("use object pool take time: " + (time2 - time1));
        System.out.println("not use take time: " + (time3 - time2));
    }

    private static void doSomethingNotUsePool() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.submit(new Runnable() {
            public void run() {
                MockClient client = new MockClient();
                try {
                    client.connect();
                    client.doSomething();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        client.disconnect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        service.shutdown();
    }

    public static void doSomethingUseClientPool(final ClientPool pool) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.submit(new Runnable() {
            public void run() {
                MockClient client = null;
                try {
                    client = pool.borrowObject();
                    System.out.println("Thread name: " + Thread.currentThread().getName() + "," +
                            "client info:" + client.toString());
                    client.doSomething();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        pool.returnObject(client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        service.shutdown();
    }

}
