
import java.util.*;

public class Location {
    String name;
    double x;
    double y;
    private HashSet<Volunteer> v = new HashSet <Volunteer>();
    private HashSet<Request> r = new HashSet <Request>();
    Model model;
    
    
    
    public Location(Model model,
                    java.lang.String name,
                    double x,
                    double y){
        this.model = model;
        this.name = name;
        this.x = x;
        this.y = y;
        
        model.addLocation(this);
    }
    
    
    public void addRequest (Request request){ 
        r.add(request);
        model.addRequest(request);
    }
    
    public void addVolunteer(Volunteer volunteer){
        v.add(volunteer);
        model.addVolunteer(volunteer);
    }
    
    public String getName(){
        return name;
    }
    public HashSet<Request> getRequests(){
        return r;
    }
    public HashSet<Volunteer> getVolunteers(){
        return v;
    }
    public double[] getXY(){
    	double [] xy = {x,y};
        return xy;
    }
    public void removeRequest(Request request){
        r.remove(request);
        
        
    }
    public void removeVolunteer(Volunteer volunteer){
        v.remove(volunteer);
        
        
    }
    
    
    
}
