package client;

/**
 * Created by sacry on 20/05/14.
 */
public class NameHandler {

    public Name name;
    public Response response;

    public NameHandler() {
        this.name = new Name();
        this.response = new Response();
    }

    class Name {
        private String name;

        public Name() {
            name = null;
        }

        public synchronized void updateName(String name) {
            this.name = name;
            notify();
        }

        public synchronized String getName()
                throws InterruptedException {
            while (name == null)
                wait();
            String tmp = name;
            name = null;
            return tmp;
        }
    }

    class Response {
        private String response;

        public Response() {
            response = null;
        }

        public synchronized void updateResponse(String response) {
            this.response = response;
            notify();
        }

        public synchronized String getResponse()
                throws InterruptedException {
            while (response == null)
                wait();
            String tmp = response;
            response = null;
            return tmp;
        }
    }
}