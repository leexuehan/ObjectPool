package factory;

public class ClientFactory {


    public MockClient makeClient() throws Exception {
        MockClient myClient = new MockClient();
        myClient.connect();
        return myClient;
    }

    public void destroyClient(MockClient client) throws Exception {
        client.disconnect();
    }

    public boolean validateObject(MockClient client) {
        return client.getStatus();
    }

}
