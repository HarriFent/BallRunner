
package ballformer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chrono implements ActionListener {

	GameCanvas gc;
        
	Chrono(GameCanvas gc) {
		this.gc = gc;
	}
        
	public void actionPerformed(ActionEvent e) {
		gc.myRepaint();
	}

}
