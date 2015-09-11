/* This project was completed by Christian Wilson (wilso266) and Wei Zhang (zhan1613)*/






import java.util.Observable;
import java.util.Observer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.JFrame;
import javax.swing.SwingWorker;


public class Controller extends SwingWorker<Object, Object> implements Observer {
 Model model;
    View view;
    
    
  

 
     Controller(Model model, View view){
      
      Connector a = new Connector("pc.cs.purdue.edu", 1337, "connect monitor",this);
      this.model = model;
      this.view = view;
     
      execute();
     }

 @Override
 protected Object doInBackground() throws Exception {
  // TODO Auto-generated method stub
  while(true){
   Thread.sleep(100);
   view.repaint();
   
  }
  
  
 }
     
 @Override
 public void update(Observable o, Object arg) {
  // TODO Auto-generated method stub
  synchronized(model.lock){
   
   
   String message = (String) arg;
   
   String [] A = message.split(" ");
   
   
   if(A[0].equals("location")){
    locationHandler(A);
   }
   else if(A[0].equals("request")){
    requestHandler(A);
   }
   else if(A[0].equals("volunteer")){
    volunteerHandler(A);
   }
   else if(A[0].equals("moving")){
    movingHandler(A);
   }
   else if(A[0].equals("walking")){
    walkingHandler(A);
   }
   else if(A[0].equals("delete")){
    deleteHandler(A);
   }
   
  }
  
  
 }
   

  public void locationHandler(String [] A) {
   
   
       String name = A[1];
       Double x = Double.parseDouble(A[2]);
       Double y = Double.parseDouble(A[3]);
      
          if(model.getLocationByName(name) == null){
           Location newLoc = new Location(model, name, x, y);

           
           model.addLocation(newLoc);
           
          } 
   
  }
  
  
  public void requestHandler(String [] A) {
   // TODO Auto-generated method stub
   
       String name = A[1];
       
       Location fromLoc = model.getLocationByName(A[2]);
       Location toLoc = model.getLocationByName(A[3]);
       
       int value = Integer.parseInt(A[4]);
       Request newReq = new Request(model, name, fromLoc, toLoc, value);
       if(model.getRequestByName(name) == null){
       
       fromLoc.addRequest(newReq);
     
   }
              else{
     
     fromLoc.getRequests().remove(model.getRequestByName(name));
    }

       //view.drawwhaever
       }
     
       
  
  
  public void volunteerHandler(String[] A) {
   // TODO Auto-generated method stub
   String  name = A[1];
   Location curLoc = model.getLocationByName(A[2]);
   int value = Integer.parseInt(A[3]);
   
   if(model.getVolunteerByName(name) == null){
    Volunteer newVol = new Volunteer(model,name,value,curLoc);
    model.addVolunteer(newVol);
    curLoc.addVolunteer(newVol);
   }
   
  }
  
  public void movingHandler(String[] A) {
   // TODO Auto-generated method stub
   
   String name = A[1];
   
   Location fromLoc = model.getLocationByName(A[2]);
   Location destination = model.getLocationByName(A[3]);
  
   
   long timeNeed = Long.parseLong(A[4]);
   
   
      
   Volunteer vol = model.getVolunteerByName(name);
   
   fromLoc.removeVolunteer(vol);
   destination.addVolunteer(vol);
   //model.removeVolunteer(model.getVolunteerByName(name));
   
   
   } 
   
   
   
   
   
   
   
   //view.drwawhatever;
  
  
  public void walkingHandler(String[] A) {
   // TODO Auto-generated method stub
   
   String nameforVol = A[1];
   
   String nameforReqster = A[2];
   
   String buildFrom = A[3];
   
   String buildTo = A[4];
   
   Request req = model.getRequestByName(nameforReqster);
   Volunteer vol = model.getVolunteerByName(nameforVol);
   Location BF = model.getLocationByName(buildFrom);
   Location BT = model.getLocationByName(buildTo);
   
   long timeNeed = Long.parseLong(A[5]);
   
   model.getVolunteerByName(nameforVol).startWalking(req, timeNeed);
   
   BF.removeRequest(req);
   BF.removeVolunteer(vol);
   BT.addRequest(req);
   BT.addVolunteer(vol);
   //view.drwawhatever;
   
  }
  
  public void deleteHandler(String[] A) {
   // TODO Auto-generated method stub
   String name = A[1];
       
      model.removeVolunteer(model.getVolunteerByName(name));
      //view.drawwhatever;
  }         
   
   
  }

  
   
 


  

