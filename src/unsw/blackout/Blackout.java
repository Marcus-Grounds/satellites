 package unsw.blackout;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Blackout {
    
    private LocalTime currentTime;
    private ArrayList<Device> devices;
    private ArrayList<Satellite> satellites;

    public Blackout(LocalTime currentTime) {
        
        this.currentTime = currentTime;
        devices = new ArrayList<Device>();
        satellites = new ArrayList<Satellite>();
    }
    
    public void createDevice(String id, String type, double position) {
            
        devices.add (new Device (id, type, position));        
    
        for (Satellite s : satellites) {
            s.updatePossibleConnections(id, position, type);    
        }

        devices.sort(Comparator.comparing(Device::getId));
    }

    public void createSatellite(String id, String type, double height, double position) {
        
        Satellite s = null;

        switch(type) {
            case "SpaceXSatellite":
                s = new SpaceXSatellite(id, height, position);
                satellites.add(s);
                break;
            
            case "BlueOriginSatellite":
                s = new BlueOriginSatellite(id, height, position);
                satellites.add (s);
                break;

            case "NasaSatellite":
                s = new NasaSatellite(id, height, position);
                satellites.add (s);
                break;
            
            case "SovietSatellite":
                s = new SovietSatellite(id, height, position);
                satellites.add (s);
                break;
        }

        for (Device d : devices) {
            s.updatePossibleConnections(d.getId(), d.getPosition(),d.getType());
        }

        satellites.sort(Comparator.comparing(Satellite::getId));
    }

    public void scheduleDeviceActivation(String deviceId, LocalTime start, int durationInMinutes) {
        
        //create a conversion from duration in minutes to endTime
        LocalTime actEndTime = start.plusMinutes(durationInMinutes); 

        for (Device d : devices) {
            
            if (d.getId().equals(deviceId)) {                
                d.addActivationPeriod(start, actEndTime);
                break;
            }
        }
    }

    public void removeSatellite(String id) {
        
        Satellite del = null;
        
        for (Satellite s : satellites) {
            if (s.getId().equals(id)) {
                del = s;
                break;
            }
        }
    
        satellites.remove(del);
    }

    public void removeDevice(String id) {
        
        Device del = null;
        
        for (Device d : devices) {
            if (d.getId().equals(id)) {
                del = d;
                break;
            }
        }
    
        devices.remove(del);

        for (Satellite s : satellites) {
            s.remPossibleConnection(id);
        }
    }

    public void moveDevice(String id, double newPos) {
        
        String dType = null;
        for (Device d : devices) {
            if (d.getId().equals(id)) {
                dType = d.getType();
                d.setPosition(newPos);
                break;
            }
        }

        for (Satellite s : satellites) {
            s.updatePossibleConnections(id, newPos, dType);
        }
    }

    //list.sort comparator to sort id names //sort the list first
    public JSONObject showWorldState() {
        
        JSONObject result = new JSONObject();
        JSONArray  jsdevices = new JSONArray();
        JSONArray  jssatellites = new JSONArray();

        // add all device objects
        for (Device d : devices) {
            
            JSONObject jsobj = new JSONObject();
            JSONArray  jsarr = new JSONArray();

            for (ActivationPeriod a : d.getActivationPeriods()) {
                
                JSONObject jsact = new JSONObject();
                jsact.put ("endTime", a.getEndTime());
                jsact.put ("startTime", a.getStartTime());
                
                jsarr.put (jsact);
            }

            jsobj.put ("activationPeriods", jsarr);
            jsobj.put ("id", d.getId());
            jsobj.put ("isConnected", d.getIsConnected());
            jsobj.put ("position", d.getPosition());
            jsobj.put ("type", d.getType());
       
            jsdevices.put(jsobj);
        }

        // add all satellites
        for (Satellite s : satellites) {

            JSONObject jsobj = new JSONObject();
            JSONArray  jsarr1 = new JSONArray();

            for (Connection c : s.getConnections()) {
                
                JSONObject jsobj1 = new JSONObject();
                
                if ((this.currentTime.compareTo(LocalTime.of(0,0)) == 0) ||
                    (this.currentTime.compareTo(c.getEndTime()) >= 0)) {
                   
                    jsobj1.put ("endTime", c.getEndTime());
                }
                
                jsobj1.put ("deviceId", c.getDeviceId());
                jsobj1.put ("minutesActive", c.getMinutesActiveId());
                jsobj1.put ("satelliteId", c.getSatelliteId());
                jsobj1.put ("startTime", c.getStartTime());

                jsarr1.put (jsobj1);
            }

            JSONArray jsarr2 = new JSONArray(s.getPossibleConnections());

            jsobj.put ("connections", jsarr1);
            jsobj.put ("height", s.getHeight());
            jsobj.put ("id", s.getId()); 
            jsobj.put ("position", s.getPosition());
            jsobj.put ("possibleConnections", jsarr2);
            jsobj.put ("type", s.getType());
            jsobj.put ("velocity", s.getVelocity());
       
            jssatellites.put(jsobj); 
        }

        result.put("devices", jsdevices);
        result.put("satellites", jssatellites);
        result.put("currentTime", this.currentTime);

        return result;
    }

    public void simulate(int tickDurationInMinutes) {
        
        for (int i = 0; i < tickDurationInMinutes; i++) {
            
            //update current time
            this.currentTime = this.currentTime.plusMinutes(1);

            for (Satellite s : satellites) {

                //update position of each satellite
                s.updatePosition();
                
                for (Device d : devices) {
                    
                    //update connections
                    s.updatePossibleConnections(d.getId(), d.getPosition(), d.getType());
                    s.updateConnections(d, this.currentTime); //does not change isConnected
                }
            }

            for (Device d : devices) {
                
                //update connection status for each device
                d.updateIsConnected(satellites, this.currentTime);
            }
        }
    }
}