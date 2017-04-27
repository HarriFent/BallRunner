package ballformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PlatformBlock {
    int x, y, w, h;
    int cInt, cInt2, cInt3;
    boolean collide = true;
            Color c;
            Color c2;
    
    void changeColor(int i){
        if (cInt<255) {
            if (cInt+i>255){
                cInt = 255;
            }else{
                cInt += i;
            }
        } else if(cInt2<255) {
            if (cInt2+i>255){
                cInt2 = 255;
            }else{
                cInt2 += i;
            }
        } else if(cInt3<255) {
            if (cInt3+i>255){
                cInt3 = 255;
            }else{
                cInt3 += i;
            }
        }
        
        if (cInt3 == 255) {
            collide = false;
            c = new Color(250,250,250);
            c2 = new Color(250,250,250);
        }
    }
    
    PlatformBlock(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    void MyRepaint(Graphics g) {
        if (cInt3 < 255) {
            c = new Color(0+cInt-cInt2+cInt3,255,0+cInt2);
            c2 = new Color(0+cInt-cInt2+cInt3,220,0+cInt2);
        }
        g.setColor(c);
        g.fillRect(x, y, w, h);
        g.setColor(c2);
        g.fillRect(x, y, w, h/2);
        if (cInt3 > 50){
            g.setColor(new Color(cInt3-50,cInt3-50,cInt3-50));
        }else {g.setColor(Color.black);}
        g.drawRect(x, y, w, h);
    }
    
    public Rectangle getBounds(){
        return new Rectangle(x,y,w,h);
    }
    
}
