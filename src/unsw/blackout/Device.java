package unsw.blackout;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

public class Device {
    
    private String id;
    private String type;
    private double position;
    private long connectTime;
    private boolean isConnected;
    private ArrayList<ActivationPeriod> activationPeriods;

    public Device (String id, String type, double position) {
        
        this.id = id;
        this.type = type;
        this.position = position;
        this.isConnected = false;
        this.activationPeriods = new ArrayList<ActivationPeriod>();

        switch(type) {
            case "HandheldDevice":
                this.connectTime = 1;
            case "LaptopDevice":
                this.connectTime = 2;
            case "DesktopDevice ":
                this.connectTime = 5;
        }
    }

    //ACTIVATION PERIODS
    public void addActivationPeriod(LocalTime startTime, LocalTime endTime) {
        
       ActivationPeriod a = new ActivationPeriod(startTime, endTime);
        
       //Assume you can't add the same activation period
        if (!activationPeriods.contains(a)) {
            activationPeriods.add(a);
        }

        activationPeriods.sort(Comparator.comparing(ActivationPeriod::getStartTime));
    }
    
    //UPDATE IS CONNECTED
    public void updateIsConnected(ArrayList<Satellite> satellites, LocalTime currentTime) {
        
        int count = 0;
        for (Satellite s : satellites) {
            
            ArrayList<Connection> connections = s.getConnections();
            for (Connection c : connections) {
                
                if (this.id.equals(c.getDeviceId()) && s.checkRange(getId(), getPosition())) {
                    
                    if ((currentTime.compareTo(c.getStartTime()) >= 0 && currentTime.compareTo(c.getEndTime()) <= 0) &&
                         currentTime.compareTo(LocalTime.of(0,0)) != 0) {
                        count ++;
                    }
                }
            }
        }

        if (count != 0) {
            setIsConnected(true);
        
        } else {
            setIsConnected(false);
        }
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

    public long getConnectTime() {
        return connectTime;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public ArrayList<ActivationPeriod> getActivationPeriods() {
        return activationPeriods;
    }

    //SETTERS
    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void setPosition(double position) {
        this.position = position;
    }
}
