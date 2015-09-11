

public class Request {
    String name;
    Location start;
    Location destination;
    int value;
    Model model;
    
    public Request(Model model, java.lang.String name, Location start, Location destination, int value){
        this.model = model;
        this.name = name;
        this.start = start;
        this.destination = destination;
        this.value = value;
        start.addRequest(this);
        model.addRequest(this);
        
    }
    
    public Location getDestination(){
        return destination; 
    }
    public String getName(){
        return name;
    }
    public Location getStart(){
        return start;
    }
    public int getValue(){
        return value;
        
    }
}
