
import javax.swing.SwingUtilities;


public class SafeWalkView implements Runnable{
    public static void main(String[] args) {
        
        // Pass args to new SafeWalkView instance running on Event Dispatch Thread...
        SwingUtilities.invokeLater(new SafeWalkView());
    }
    
    
    
    /**
     * Run on the EDT, creating model, view, and controller.
     */
    public void run() {
        Model model = new Model();
        View view = new View(model);
        new Controller(model, view);
    }
    
    
}
