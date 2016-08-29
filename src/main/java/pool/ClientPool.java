package pool;

import factory.ClientFactory;
import factory.MockClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientPool {
    private static final int DEFAULT_POOL_SIZE = 10;
    private final BlockingQueue<MockClient> pool;
    private final ClientFactory factory;

    public ClientPool(ClientFactory factory) throws Exception {
        this(DEFAULT_POOL_SIZE, factory);
    }

    public ClientPool(int poolSize, ClientFactory factory) throws Exception {
        this.factory = factory;
        pool = new ArrayBlockingQueue<MockClient>(poolSize * 2);
        initPool(poolSize);
    }

    private void initPool(int poolSize) throws Exception {
        for (int i = 0; i < poolSize; i++) {
            addObject();
        }
    }


    public MockClient borrowObject() throws Exception {
        MockClient client = pool.take();
        if (client == null) {
            client = factory.makeClient();
            addObject();
        } else if (!factory.validateObject(client)) {
            invalidateObject(client);
            client = factory.makeClient();
            addObject();
        }
        return client;
    }

    public void returnObject(MockClient client) throws Exception {
        if ((client != null) && !pool.offer(client, 3, TimeUnit.SECONDS)) {
            factory.destroyClient(client);
        }
    }

    public void invalidateObject(MockClient client) throws Exception {
        pool.remove(client);
    }

    public void addObject() throws Exception {
        pool.offer(factory.makeClient(), 3, TimeUnit.SECONDS);
    }


    public void clear() throws Exception {

    }

    public void close() throws Exception {
        while (pool.iterator().hasNext()) {
            MockClient client = pool.take();
            factory.destroyClient(client);
        }
    }
}
