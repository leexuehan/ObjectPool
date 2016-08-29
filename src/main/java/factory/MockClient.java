package factory;

public class MockClient {
    private boolean status =  false;


    public void connect() throws InterruptedException {
        Thread.sleep(5000);
        status = true;
    }

    public void doSomething() {
        try {
            Thread.sleep(100);
            System.out.println("do something");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() throws InterruptedException {
        Thread.sleep(200);
        status = false;
    }


    public boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "MockClient{" +
                "status=" + status +
                '}';
    }
}
