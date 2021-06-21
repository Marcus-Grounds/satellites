package unsw.blackout;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalTime;
import java.util.ArrayList;

public class SovietSatellite extends Satellite {

    public SovietSatellite(String id, double height, double position) {
        
        setId(id);
        setType("SovietSatellite");
        setHeight(height);
        setPosition(position);
        setVelocity(6000.0/60);
        setConnections();
        setPossibleConnections();
    }

    //UNIQUE SOVIET POSITION METHOD
    public void updatePosition() {
        
        double curPos = getPosition();
        double curVel = getVelocity();
        double newAng = getVelocity()/getHeight();
        double newPos = 0;

        if (curPos >= 190 && curPos <= 345) {
            
            setVelocity(-6000.0/60);
            newAng = getVelocity()/getHeight();
            
            newPos = getPosition() + newAng;
            setPosition(newPos);
            

        } else if (curPos >= 345 || curPos <= 140) {
            
            setVelocity(6000.0/60);
            newAng = getVelocity()/getHeight();
            
            newPos = getPosition() + newAng;
            setPosition(newPos);
            
        
        } else if (curPos >= 140 && curPos <= 190){
            
            if (curVel > 0 && curPos < 190) {
                newPos = getPosition() + newAng;    
            
            } else if (curVel < 0 && curPos > 140) {
                newPos = getPosition() + newAng;
            }
        
            setPosition(newPos);
        }
    }
    
    public void updatePossibleConnections(String devId, double devPosition, String devType) {        
        
        if ((devType.equals("LaptopDevice") || devType.equals("DesktopDevice")) && checkRange(devId, devPosition)) {
            addPossibleConnection(devId);
        } else {
            remPossibleConnection(devId);
        }
    }

    public void updateConnections(Device d, LocalTime currTime) {
        
        if ((d.getType().equals("LaptopDevice") || d.getType().equals("DesktopDevice")) && checkRange(d.getId(), d.getPosition())) {

            ArrayList<ActivationPeriod> actPeriods = d.getActivationPeriods();
            
            for (ActivationPeriod a : actPeriods) {
                
                if (currTime.compareTo(a.getStartTime()) >= 0 && currTime.compareTo(a.getEndTime()) <= 0) {
                    
                    long minutesActive = a.getStartTime().until(a.getEndTime(), MINUTES) - (d.getConnectTime()*2);
                    Connection c = new Connection(d.getId(), getId(), minutesActive, a.getStartTime(), a.getEndTime().plusMinutes(1));
                    
                    addConnection(c);
                    break;
                } 
            }
        }
    }
}