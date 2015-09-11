import java.util.HashMap;
import java.util.HashSet;

/**
 * The Model class (along with Location, Request, and Volunteer classes) defines the data used by the SafeWalk Monitor
 * application.
 * 
 * @author dhelia
 * @author jtk
 * @version 2014
 */
public class Model {
    /**
     * The lock object is used to synchronize access to this model instance. Because the model is shared between the
     * controller thread that reads from the server and the view thread that handles event dispatching, a lock object is
     * need to synchronize these threads.
     */
    public Object lock;

    public HashSet<Location> locations = new HashSet<Location>();
    public HashMap<String, Location> building2location = new HashMap<String, Location>();

    public HashSet<Volunteer> volunteers = new HashSet<Volunteer>();
    public HashMap<String, Volunteer> name2volunteer = new HashMap<String, Volunteer>();

    public HashSet<Request> requests = new HashSet<Request>();
    public HashMap<String, Request> name2request = new HashMap<String, Request>();

    /**
     * Constructor. One instance of the Model class is created when the SafeWalk application begins. All locations,
     * requests, and volunteers are maintained in that model. Allocates a new Object and assigns it to the lock field.
     */
    public Model() {
	lock = new Object();
    }

    /**
     * Adds a location to this model. Also, updates a mapping from location name to location, so that a subsequent call
     * to getLocationByName(name) returns this location.
     * 
     * Limitations: Location is not null. There can be only one location with a given name.
     * 
     * @param location
     *            the Location to be added
     */
    public void addLocation(Location location) {
	// Check for an existing location with this name; remove from locations set
	Location existing = building2location.get(location.getName());
	if (existing != null)
	    locations.remove(existing);
	
	locations.add(location);
	building2location.put(location.getName(), location);
    }

    /**
     * Adds a request to this model. Also, updates a mapping from requester name to request, so that a subsequent call to
     * getRequestByName(name) returns this request.
     * 
     * Limitations: Request is not null. There can be only one Request with a given requester name.
     * 
     * @param request
     *            the Request to be added
     */
    public void addRequest(Request request) {
	requests.add(request);
	name2request.put(request.getName(), request);
    }

    /**
     * Adds a volunteer to this model. Also, updates a mapping from volunteer name to volunteer so that a subsequent call
     * to getVolunteerByName(name) returns this volunteer.
     * 
     * Limitations: Volunteer is not null. There can be only one Volunteer with a given name.
     * 
     * @param volunteer
     *            the Volunteer to be added
     */
    public void addVolunteer(Volunteer volunteer) {
	volunteers.add(volunteer);
	name2volunteer.put(volunteer.getName(), volunteer);
    }

    /**
     * Returns the location with the given name. Returns null if no location with the given name exists.
     * 
     * @param name
     *            the String name of the location; null if there is no such location
     * @return the Location with the given name
     */
    public Location getLocationByName(String name) {
	return building2location.get(name);
    }

    /**
     * Returns the set of Locations in this model.
     * 
     * @return the HashSet<Location> of locations; can iterate through the locations in this model
     */
    public HashSet<Location> getLocations() {
	return locations;
    }

    /**
     * Returns the request made by the given requester. Returns null if no request exists for the given requester name.
     * 
     * @param name
     *            the String name of the person making the request
     * @return the Request corresponding to the given requester name; null if the requester does not exist.
     */
    public Request getRequestByName(String name) {
	return name2request.get(name);
    }

    /**
     * Returns the set of requests in this model.
     * 
     * @return the HashSet<Request> of requests; can iterate through the requests with this value
     */
    public HashSet<Request> getRequests() {
	return requests;
    }

    /**
     * Returns the volunteer with the given name. Returns null if there is no volunteer with that name.
     * 
     * @param name
     *            the String name of volunteer
     * @return the Volunteer with the given name; null if there is no volunteer with that name
     */
    public Volunteer getVolunteerByName(String name) {
	Volunteer volunteer = name2volunteer.get(name);
	return volunteer;
    }

    /**
     * Returns the set of volunteers in this model.
     * 
     * @return the HashSet<Volunteer> of volunteers; can iterate through the volunteers with this value
     */
    public HashSet<Volunteer> getVolunteers() {
	return volunteers;
    }

    /**
     * Removes a request from this model, including removing it from the start location to which it is attached.
     * 
     * @param request
     *            the Request to be removed
     */
    public void removeRequest(Request request) {
	Location location = request.getStart();
	if (location != null)
	    location.removeRequest(request);
	requests.remove(request);
	name2request.remove(request.getName());
    }

    /**
     * Removes a volunteer from this model. If the volunteer is currently at a Location, it is removed from that
     * location.
     * 
     * @param volunteer
     *            the Volunteer to be removed
     */
    public void removeVolunteer(Volunteer volunteer) {
    	if (volunteer == null) 
    	System.out.println(">>>>>> VOLUNTEER WAS NULL");
    	
	Location location = volunteer.getCurrentLocation();
	if (location != null)
	    location.removeVolunteer(volunteer);
	volunteers.remove(volunteer);
	name2volunteer.remove(volunteer.getName());
    }
}
