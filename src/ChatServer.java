import java.net.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ChatServer {

//    private final static ChatServer server = new ChatServer();
    private final int PORT = 1234;
    private boolean running = true;
    private Hashtable connectedClients = new Hashtable();

    ChatServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                new ChatServerThread(clientSocket, this);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + PORT + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    ChatServer get(){
        return this;
    }

    void addConnectedClient(Socket socket, ObjectOutputStream outputStream){
        this.connectedClients.put(socket, outputStream);
    }

    Hashtable getConnectedClients(){
        return this.connectedClients;
    }

    Enumeration getOutputStreams(){
        return connectedClients.elements();
    }

    void sendToAll(Message msg){
        for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
            // ... get the output stream ...
            ObjectOutputStream dataOut = (ObjectOutputStream)e.nextElement();
            // ... and send the message
            try {
                dataOut.writeObject(msg);
            } catch( IOException ie ) { ie.printStackTrace(); }
        }

    }

    void removeConnection(Socket socket){
        try {
            socket.close();
            System.out.println("Removing connection: " + socket.getRemoteSocketAddress().toString());
            connectedClients.remove(socket);
            System.out.println("Connected clients: " + connectedClients.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}