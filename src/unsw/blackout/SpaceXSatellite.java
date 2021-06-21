package unsw.blackout;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalTime;
import java.util.ArrayList;

public class SpaceXSatellite extends Satellite {

    public SpaceXSatellite(String id, double height, double position) {
        
        setId(id);
        setType("SpaceXSatellite");
        setHeight(height);
        setPosition(position);
        setVelocity(3330.0/60);
        setConnections();
        setPossibleConnections();
    }
    
    public void updatePosition() {
        double newAng = getVelocity()/getHeight();
        double newPos = getPosition() + newAng;
        
        setPosition(newPos);
    }

    public void updatePossibleConnections(String devId, double devPosition, String devType) {        
        
        if (devType.equals("HandheldDevice") && checkRange(devId, devPosition)) {
            addPossibleConnection(devId);
        } else {
            remPossibleConnection(devId);
        } 
    }

    public void updateConnections(Device d, LocalTime currTime) {
        
        if (d.getType().equals("HandheldDevice") && checkRange(d.getId(), d.getPosition())) {

            ArrayList<ActivationPeriod> actPeriods = d.getActivationPeriods();
            
            for (ActivationPeriod a : actPeriods) {
                
                if (currTime.compareTo(a.getStartTime()) >= 0 && currTime.compareTo(a.getEndTime()) <= 0) {
                    
                    long minutesActive = a.getStartTime().until(a.getEndTime(), MINUTES); //SpaceX connects instantly
                    Connection c = new Connection(d.getId(), getId(), minutesActive, a.getStartTime(), a.getEndTime().plusMinutes(1));
                    
                    addConnection(c);
                    break;
                } 
            }
        }
    }
}