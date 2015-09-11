import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.*;

import java.util.Iterator;

public class View extends JPanel{

    Model model;
    private Image mapImage;
    private Image volunteerImage;
    private Image requesterImage;


    final static int DIAMETER = 16; // diameter of circle at each location
    final static float dash1[] = { 10.0f };
    final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    Graphics2D g2;
    float scaleWidth;  //  Width Parameter for resizing the image
    float scaleHeight; //  Height Parameter for resizing the image

    public View(Model model) {


        this.model = model;

        mapImage = loadImage("CampusCropped-Faded.jpg");
        volunteerImage = loadImage("volunteer.jpg");
        requesterImage = loadImage("request.jpg");


        // The following code displays a blank JPanel with SafeWalkView in the title bar
        JFrame frame = new JFrame("SafeWalkView");
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Adding the canvas (this) to the main panel at the CENTER...
        mainPanel.add(this, BorderLayout.CENTER);

        // Adding the main panel to the frame...
        frame.add(mainPanel);
        JScrollPane pane = new JScrollPane(this);
        frame.add(pane);
        frame.getContentPane().add(pane);
        frame.setSize(500,600);
        // Sets visible and away we go...
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    /**
     * Called on the Event Dispatch Thread (EDT) in response to a call to
     * repaint(). Accesses the model to decide what to paint. Since the model is
     * also being accessed and updated on a different thread (by the Controller
     * when messages arrive), must get the lock before accessing the model.
     */
    public void paintComponent(Graphics gr) {
       
    	synchronized(model.lock){
    	g2 = (Graphics2D) gr; //Stores the graphic as a 2D graphic image

    





        scaleWidth = getWidth()/(float)(mapImage.getWidth(null));
        scaleHeight = getHeight()/(float)(mapImage.getHeight(null));






        // Draw the map first...
        //Syntax for drawImage method:  GraphicsObject.drawImage(Image img, int x, int y, int width, int height, null);
        g2.drawImage(mapImage, 0, 0, getWidth(), getHeight(), null);

       
        drawLocation();  //  Calls the drawLocation method
        drawIntransit(); //  Calls the drawIntransit method
        
    	}
    }


    /**
     * This method places indicators on each marked location and displays the volunteers
     * and the requesters present at a particular location. The method first Iterates through
     * each location HashSet<String> of locations stored in the model and converts the string
     * coordinates into integers. These values are used to draw two concentric circles at location.
     *
     * Next the method displays a list of volunteers and requesters at the location.
     *
     */

    private void drawLocation() {

         
    	//if(!model.getLocations().equals(null));
        //Refer Javadocs for using Iterator
        int x = 0; // Variable for X coordinate of the location
        int y = 0; // Variable for Y coordinate of the location
        Iterator<Location> it = (model.getLocations()).iterator();
       
        while (it.hasNext()) {
            //String Coordinate = (String) it.next(); //Iterates through each location
                Location place = it.next();
                String name = place.getName();
                //System.out.println(name);
                
                double[] position = place.getXY();
                int[] p = new int[2];
                p[0] = (int)position[0];
                p[1] = (int)position[1];
                //System.out.println(position);
            //System.out.println(Coordinate);
            String[] posString = new String[2];//Coordinate.split(" ");
            //System.out.println(posString);
            posString[0] = Integer.toString(p[0]);
            //System.out.println("POSSTRING " + posString[0]);
            //System.out.println("POSSTRING" + posString[0]);
            posString[1] = Integer.toString(p[1]);
            x = (int) position[0];//Integer.parseInt(CoordSplit[0]); //Fetches x coordinate
            y = (int) position[1];//Integer.parseInt(CoordSplit[1]); //Fetches y coordinate
            
            //System.out.println("POSSTRING" + posString);

            // Draw overlapping circle for the building location...
            g2.setColor(Color.BLACK); //Sets color to Black for outer circle

            g2.fillOval(Math.round(x * scaleWidth), Math.round(y * scaleHeight), DIAMETER, DIAMETER);


            g2.setColor(Color.YELLOW); //Sets color to Yellow for inner circle

            
            //Adjust the dimensions of the Yellow circle to make the circles appear like concentric circles
            g2.fillOval(Math.round(x * scaleWidth)+2, Math.round(y * scaleHeight)+2, DIAMETER-4, DIAMETER-4);


            g2.setColor(Color.BLACK); //Sets the color back to black

            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            // Sets font to "Arial" with type= Font.PLAIN and size = 16


            int dy = g2.getFontMetrics().getHeight() * 2; // Calculates line width dy

            y+=dy;   // Adds dy to the y coordinate to move to the next line in the image

            // This prints the volunteers and the requests in the next line
            Iterator<Request> it2 = (model.getRequests()).iterator();
            while (it2.hasNext()) {
                Request request = (Request)it2.next();
                String reqName = request.getName();
                //System.out.println(reqName);
                Location requested = request.getStart();
                //Location loaction = 
                double[] reqPosition = requested.getXY();
                int[] p2 = new int[2];
                p2[0] = (int)reqPosition[0];
                p2[1] = (int)reqPosition[1];
                //String key = it2.next(); //Iterates through each key
                
                String[] reqPos = new String[2];
                reqPos[0] = Integer.toString(p2[0]);
                //System.out.println("REQPOS" + reqPos[0]);
	            reqPos[1] = Integer.toString(p2[1]);
                //String loc = model.getRequester2Location().get(key);
                //String coord = model.getLocation2Coordinate().get(loc);
	            //System.out.println("REQPOS" + reqPos);

                //using drawName method
                //if coord(the coordinates of the requester) is equal to Coordinate (the coordinates
                //of the location)
                //then drawName
                //y+=dy
                if(reqPos[0].equals(posString[0]) && reqPos[1].equals(posString[1])){
                	//System.out.println("IT DOESNT GET HERE!!");
                        drawName(requesterImage, reqName, x, y);

                        y+=dy;   // Adds dy to the y coordinate to move to the next line in the image
                }
                }


            Iterator<Volunteer>it3 = (model.getVolunteers()).iterator();
            while (it3.hasNext()) {
                Volunteer volunteer = (Volunteer)it3.next();
                String volName = volunteer.getName();
                Location volLoc = volunteer.getCurrentLocation();
                
                if(volLoc != null){
                
                double[] volPosition = volLoc.getXY();// volunteer.getCurrentPosition();
                int[] vP = new int[2];
                vP[0] = (int)volPosition[0];
                vP[1] = (int)volPosition[1];
                String[] volPos = new String[2];
                volPos[0] = Integer.toString(vP[0]);
                //System.out.println("VOLPOS" + volPos[0]);
                volPos[1] = Integer.toString(vP[1]);
                //String key = it3.next(); //Iterates through each key
                
                //using drawName method
                //String loc2 = model.getVolunteer2Location().get(key);
                //String coord2 = model.getLocation2Coordinate().get(loc2);


                if(volPos[0].equals(posString[0])&& volPos[1].equals(posString[1])){
                        drawName(volunteerImage, volName, x, y);

                        y+=dy;
                }

               // Adds dy to the y coordinate to move to the next line in the image
            }
        }
        }
    }
    


    private void drawName(Image image, String name, int x, int y) {
        int gap = g2.getFontMetrics().getHeight();

        g2.drawImage(image, Math.round(x * scaleWidth) - gap, Math.round(y * scaleHeight), 12, 12, null);
        g2.drawString(name, Math.round(x * scaleWidth), Math.round(y * scaleHeight) + gap - 3);
    }
    /** Method drawIntransit
      *  This method draws movers from start location to end location and draws
      *  a moving green dot to depict the motion. Command for motion is given in
      *  Controller.java
      */
   private void drawIntransit() {

        Iterator<Volunteer>it = (model.getVolunteers()).iterator();
        
       
        
        while (it.hasNext()) {

            //String mover = (String) it.next();
            Volunteer vMover = (Volunteer)it.next();
            Location locMover = vMover.getCurrentLocation();
            
            if(locMover == null){
            
            
            String volunteerName = vMover.getName();
            // Uses the getVolunteerPosition() method in model to get location of mover.
            //You may uncomment the following code for implementing this:
            
            double[] xy = vMover.getCurrentPosition();//model.getVolunteerPosition(mover);
            int x = (int) Math.round(xy[0]);
            int y = (int) Math.round(xy[1]);
            
            
            // Draw mover name and anyone being walked
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            int lineW = g2.getFontMetrics().getHeight();
            g2.setColor(Color.BLACK);
            //String [] fields = mover.split(" ");
            //String mName = fields[0];
            g2.drawString(volunteerName,Math.round(x * scaleWidth) + 20, Math.round(y * scaleHeight) + lineW - 3);

            // Drawing the dashed line
            
           //String startLocation = model.getVolunteer2Location().get(mName);
            //String startLocation = locMover.getName();
            
            double[] coord3 = vMover.getCurrentPosition();//model.getLocation2Coordinate().get(startLocation);
            String [] locCoords = new String[2];
            locCoords[0] = Double.toString(coord3[0]);
            locCoords[1] = Double.toString(coord3[1]);
            //String [] scoord = locCoords.split(" ");
            int x1 = (int)Double.parseDouble(locCoords[0]);
            int y1 = (int)Double.parseDouble(locCoords[1]);
            //String destination = fields[1];
            double [] dest = vMover.getDestination();
            String [] destCoords = new String[2];
            destCoords[0] = Double.toString(dest[0]);
            destCoords[1] = Double.toString(dest[1]);
            //String coordLoc = model.getLocation2Coordinate().get(destination);
            //String [] dcoord = coordLoc.split(" ");
            int x2 = (int)Double.parseDouble(destCoords[0]);
	        int y2 = (int)Double.parseDouble(destCoords[1]);


          
            g2.setStroke(dashed); //set drawing style to shaded line

            g2.drawLine((Math.round(x1 * scaleWidth) + DIAMETER / 2), (Math.round(y1 * scaleHeight) + DIAMETER / 2), (Math.round(x2 * scaleWidth) + DIAMETER / 2), (Math.round(y2 * scaleHeight) + DIAMETER / 2));
            //Syntax: drawLine(int x1, int y1, int x2, int y2)
            //for coordinate use the scaled and rounded coordinates with offset given in the example
            //For example for x coordinate of starting point use (Math.round(xStart * scaleWidth) + DIAMETER / 2)


            // Draw filled circle at mover location on the dashed line
            g2.setColor(Color.GREEN);
            g2.fillOval(Math.round(x * scaleWidth),Math.round(y * scaleHeight), DIAMETER, DIAMETER);
            // Use setColor() and fillOval() method for drawing the moving green dot.
        }}
   }


    /** Method loadImage
      * Loads images from resources, allowing use in .jar files.
      *
      * @param name
      *            file name where the icon is located.
      * @return the Image found in the file.
      */
    private Image loadImage(String name) {
        URL url = getClass().getResource(name); //Gets the image URL
        if (url == null)
            throw new RuntimeException("Could not find " + name);
        return new ImageIcon(url).getImage(); //Loads the image and returns the Image
    }

}







