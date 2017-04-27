
package ballformer;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class BallFormer extends JFrame implements WindowListener {
    
    GameCanvas gc;
    
    BallFormer() {
        
        super("BallFormer");
        
        gc = new GameCanvas();
        gc.setSize(600, 600);
        add(gc, BorderLayout.CENTER);
        
        this.addKeyListener(gc);
        this.addWindowListener(this);
        this.pack();
        this.setLocationRelativeTo(gc);
                
	this.setVisible(true);
        
        gc.createBufferStrategy(2);
        Thread thread = new Thread(gc);
        thread.start();
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                        new BallFormer();
                }
        });
    }

    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {
		gc.threadStop = true;
		dispose();
    }
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    
}
