package unsw.blackout;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class Satellite {

    private String id;
    private String type;
    private double position;
    private double height;
    private double velocity;
    private ArrayList<Connection> connections;
    private ArrayList<String> possibleConnections;
    
    //ABSTRCACT METHODS
    //UPDATE POSITION //abstract as is differnt for soviet
    public abstract void updatePosition();

    //POSSIBLE CONNECTIONS //can be initiated before simulation
    public abstract void updatePossibleConnections(String devId, double devPosition, String devType);
    
    //CONNECTIONS //only be established during simulation - as per what was shown in the tests
    public abstract void updateConnections(Device d, LocalTime currTime);

    //RANGE CHECK
    public boolean checkRange(String devId, double devPosition) {
        return MathsHelper.satelliteIsVisibleFromDevice(position, height, devPosition);
    }
    
    public void addPossibleConnection(String p) {
        
        if (!possibleConnections.contains(p)) {
            possibleConnections.add(p);
        }

        Collections.sort(possibleConnections);
    }

    public void remPossibleConnection(String devId) {
        
        for (String p : possibleConnections) {
            
            if (p.equals(devId)) {
                possibleConnections.remove(p);
                break;
            }
        }
    }

    public void addConnection(Connection c) {
        
        boolean check = true;

        for (Connection o : connections) {
            
            if (o.getDeviceId().equals(c.getDeviceId())) { //or share the same angle i think but wont be given this
                check = false;
            } 
        }
        
        if (check) {
            connections.add(c);
        }

        connections.sort(Comparator.comparing(Connection::getStartTime));
        connections.sort(Comparator.comparing(Connection::getDeviceId));
    }

    // SETTERS
    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setConnections() {
        this.connections = new ArrayList<Connection>();
    }

    public void setPossibleConnections() {
        this.possibleConnections = new ArrayList<String>();
    }

    //GETTERS
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getPosition() {
        return position;
    }

    public double getHeight() {
        return height;
    }

    public double getVelocity() {
        return velocity;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public ArrayList<String> getPossibleConnections() {
        return possibleConnections;
    }
}