package unsw.blackout;

import java.time.LocalTime;

public class Connection {
    
    private String deviceId;
    private String satelliteId;
    private long minutesActive;
    private LocalTime startTime;
    private LocalTime endTime; 

    public Connection(String deviceId, String satelliteId, long minutesActive, LocalTime startTime, LocalTime endTime) {
        
        this.deviceId = deviceId;
        this.satelliteId = satelliteId;
        this.minutesActive = minutesActive;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public long getMinutesActiveId() {
        return minutesActive;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
  
        // If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
  
        // Check if o is an instance of Complex or not
        //  "null instanceof [type]" also returns false
        if (!(o instanceof Connection)) {
            return false;
        }
          
        // typecast o to Complex so that we can compare data members 
        Connection c = (Connection) o;
          
        // Compare the data members and return accordingly 
        return deviceId.equals(c.deviceId);
    }
}
