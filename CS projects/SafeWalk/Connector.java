
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jtk on 3/14/14.
 */
public class Connector extends Observable implements Runnable {
    private String host;
    private int port;
    private String connectString;

    private boolean closing = false;
    private Socket socket = null;
    private OutputStreamWriter outputStreamWriter;
    private BufferedReader bufferedReader;

    public Connector(String host, int port, String connectString, Observer observer) {
        this.host = host;
        this.port = port;
        this.connectString = connectString;

        addObserver(observer);

        new Thread(this).start();
    }

    private synchronized void open() {
        if (socket != null || closing)
            return;
        try {
            socket = new Socket(host, port);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            if (connectString != null) {
                outputStreamWriter.write(connectString);
                outputStreamWriter.write("\n");
                outputStreamWriter.flush();
            }
        } catch (IOException e) {
            socket = null;
        }
    }

    public void writeLine(String s) {
        open();
        try {
            outputStreamWriter.write(s);
            outputStreamWriter.write("\n");
            outputStreamWriter.flush();
        } catch (Exception e) {
            socket = null;
        }
    }

    public void run() {
        while (!closing) {
            open();
            try {
                String line = bufferedReader.readLine();
                setChanged();
                notifyObservers(line);
            } catch (IOException e) {
                socket = null;
                if (!closing) {
                    System.err.printf("READ FAILED: sleeping for 5 seconds (closing = %b)\n", closing);
                    sleep(5000);
                }
            }
        }
    }

    synchronized public void close() {
        closing = true;
        try {
            if (outputStreamWriter != null)
                outputStreamWriter.close();
            if (bufferedReader != null)
                bufferedReader.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            // ignore
        }
    }

    private void sleep(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
