package data;

import network.ChatServer;
import network.SocketStreamHelper;

import java.util.concurrent.LinkedBlockingDeque;

public class DataHandler implements Runnable {

    private LinkedBlockingDeque dataQueue = new LinkedBlockingDeque();
    private User socketUser;

    public DataHandler(User socketUser) {
        this.socketUser = socketUser;

    }

    public void run() {

        //Checks if dataQueue has anything to handle else sleep
        while (true) {
            if (dataQueue.size() > 0) {

                Object data = dataQueue.poll();

                if (data instanceof Message) {
                    handleMessage(data);

                } else if (data instanceof User) {
                    System.err.println("User object received but method is deprecated");

                } else if (data instanceof NetworkMessage.ClientConnect) {
                    handleClientConnect((NetworkMessage.ClientConnect) data);

                } else if (data instanceof NetworkMessage.RoomCreate) {

                } else if (data instanceof NetworkMessage.RoomDelete) {

                } else if (data instanceof NetworkMessage.RoomJoin) {

                } else if (data instanceof NetworkMessage.RoomLeave) {

                } else if (data instanceof NetworkMessage.UserNameChange) {
                    handleUserNameChange((NetworkMessage.UserNameChange) data);

                } else if (((String) data).startsWith("update")) {
                    updateUsers();
                }
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleClientConnect(NetworkMessage.ClientConnect data) {

        System.out.println(data.userId);

        ChatServer.get().getRooms().forEach((roomID, room) -> room.updateUser(this.socketUser));

        System.out.println("UserName: " + socketUser.getUsername());

        //PLACEHOLDER: Get the rest of the users rooms:
        //ArrayList<String> joinedRooms = ((User) data).getJoinedRooms();

        SocketStreamHelper.sendData(ChatServer.get().getRooms().get("general"), socketUser.getDataOut());

        System.out.println(ChatServer.get().getRooms().get("general"));

        SocketStreamHelper.sendData(ChatServer.get().getRooms().get("other room"), socketUser.getDataOut());

        System.out.println("Updating");
        ChatServer.get().broadcastToAll(ChatServer.get().getRooms());
    }

    private void handleClientDisconnect() {
    }

    private void handleRoomCreate() {
    }

    private void handleRoomDelete() {
    }

    private void handleRoomJoin() {
    }

    private void handleRoomLeave() {
    }

    private void handleUserNameChange(NetworkMessage.UserNameChange data) {
        ChatServer.get().getUser(data.userId).setUsername(data.newName);

        // update users in all rooms
        ChatServer.get().getRooms().forEach((nameID, room) ->
                room.updateUser(socketUser));

        // send updated user to all clients
        ChatServer.get().broadcastToAll(data);
    }

    private void handleMessage(Object data) {
        Message msg = (Message) data;

        System.out.println(msg.getRoom() + ": " + msg.getTimestamp() + " | " + socketUser.getUsername() + ": " + msg.getMsg());

        ChatServer.get().broadcastToRoom(msg.getRoom(), msg);

        ChatServer.get().getRooms().forEach((roomID, room) -> {
            if (roomID.equals(msg.getRoom())) {
                room.addMessageToRoom(msg);
            }
        });
    }

    private void handleRoom() {
    }

    public void addToQueue(Object o) {
        dataQueue.addLast(o);
    }

    public void updateUsers() {
        System.out.println("Updating users...");
        ChatServer.get().broadcastToAll(ChatServer.get().getRooms());
    }


} //END OF CLASS