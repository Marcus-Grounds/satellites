package unsw.blackout;
import java.time.LocalTime;

public class ActivationPeriod {
    private LocalTime startTime;
    private LocalTime endTime;
    
    public ActivationPeriod(LocalTime startTime, LocalTime endTime) {

        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public LocalTime getStartTime () {
        return startTime;
    }

    public LocalTime getEndTime () {
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
        if (!(o instanceof ActivationPeriod)) {
            return false;
        }
          
        // typecast o to Complex so that we can compare data members 
        ActivationPeriod a = (ActivationPeriod) o;
          
        // Compare the data members and return accordingly 
        return startTime.compareTo(a.startTime) == 0
                && endTime.compareTo(a.endTime) == 0;
    }
}
