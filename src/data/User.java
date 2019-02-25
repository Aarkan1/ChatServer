package data;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 8119880995263638778L;

    private String ID;
    private String username;
    private transient ObjectOutputStream dataOut;
    private boolean onlineStatus;
    private ArrayList<String> joinedRooms = new ArrayList<>();
    String activeRoom = "";

    public User(ObjectOutputStream dataOut) {
        //this.activeRoom = "general";
        this.ID = UUID.randomUUID().toString();
        this.username = "anon"  + new Random().nextInt(1000);
        this.dataOut = dataOut;
        this.onlineStatus = true;
        joinedRooms.add("general");
        joinedRooms.add("other room");
        setActiveRoom("other room");

    }

    public User(String name) {
        //this.activeRoom = "general";
        this.ID = UUID.randomUUID().toString();
        this.username = name.length() > 0 ? name : "anon";
    }

    public User(User oldUser) {
        this.ID = oldUser.ID;
        this.username = oldUser.username;
    }

    public String getActiveRoom() {
        return activeRoom;
    }

    public void setActiveRoom(String activeRoom) {
        this.activeRoom = activeRoom;
    }

    public void setDataOut(ObjectOutputStream dataOut) {
        this.dataOut = dataOut;
    }

    public ObjectOutputStream getDataOut() {
        return dataOut;
    }

    public ArrayList<String> getJoinedRooms() {
        return joinedRooms;
    }

    public User getUser() {
        return this;
    }

    public String getID(){
        return this.ID;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOnlineStatus(boolean onlineStatus){
        this.onlineStatus = onlineStatus;
    }

    public boolean getOnlineStatus(){
        return this.onlineStatus;
    }
}//class end