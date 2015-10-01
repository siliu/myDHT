package dhtp2p.peer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import dhtp2p.utils.Address;
import dhtp2p.utils.Configuration;
import dhtp2p.utils.Worker;

public class PeerServer extends Thread {

	private int port;
	
    private boolean running;
    private ServerSocket ss;
    private static Map<String, Object> localTable = new ConcurrentHashMap<String, Object>();

    public PeerServer(int port) throws FileNotFoundException {
        this.port = port;
		
    }

    @Override
    public void run() {
        this.running = true;
        try {
            ss = new ServerSocket(port);
			System.out.println("Peer Server is running...");
			System.out.println("Listening on port " + port +".");
            while (running) {
                try {
                    Socket socket = ss.accept();
                    Worker worker = new Worker(socket,localTable);
                    worker.start();
                } catch (SocketException socketException) {
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PeerServer.class.getName()).log(Level.SEVERE, null, ex);
            this.running = false;
        }

    }

    public void close() {
        this.running = false;
        if (this.ss != null) {
            try {
                this.ss.close();
            } catch (IOException ex) {
                Logger.getLogger(PeerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
