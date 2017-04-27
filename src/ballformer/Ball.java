
package ballformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Ball {
    
    private double x, y, w, h;
    private double dx = 0, dy = 0;
    boolean onGround;
    private static int collisionRange = 3;
    boolean aboveScreen;
    
    Ball(int x, int y) {
        this.x = x;
        this.y = y;
        this.w = 20;
        this.h = 20;
    }
    
    void MyRepaint(Graphics g) {
        g.setColor(new Color(200,0,0));
        g.fillOval((int)x, (int)y, (int)w, (int)h);
        g.setColor(new Color(255,100,100));
        g.fillOval((int)x+5, (int)y+5, (int)w/3, (int)h/3);
        g.setColor(new Color(200,0,0));
        g.fillOval((int)x+7, (int)y+7, (int)w/2, (int)h/2);
        g.setColor(new Color(0,0,0));
        g.drawOval((int)x, (int)y, (int)w, (int)h);
        
        if (aboveScreen) {
            g.setColor(new Color(200,0,0));  
            int[] xpoints = {10+(int)x,20+(int)x,13+(int)x,13+(int)x,7+(int)x,7+(int)x,0+(int)x};
            int[] ypoints = {5,15,15,35,35,15,15};
            Polygon p = new Polygon(xpoints,ypoints,7);
            g.fillPolygon(p);
            g.setColor(Color.black);
            g.drawPolygon(p);
        }
    }
    
    double getdx() {return dx;}
    double getdy() {return dy;}
    double getx() {return x;}
    double gety() {return y;}
    double getw() {return w;}
    double geth() {return h;}
    void setdx(double dx) {this.dx = dx;}
    void setdy(double dy) {this.dy = dy;}
    void setx(double x) {this.x = x;}
    void sety(double y) {this.y = y;}
    void setw(double w) {this.w = w;}
    void seth(double h) {this.h = h;}

    void checkOnGround(ArrayList<PlatformBlock> blocks) {
        for (PlatformBlock i : blocks) {
            Rectangle r3 = i.getBounds();
            Rectangle r2 = new Rectangle((int)x,(int)y+20,(int)w,collisionRange);
            if (r2.intersects(r3)&& dy>0 && i.collide == true) {
                onGround = true;
                return;
            }
        }
        onGround = false;
    }

    boolean checkAbove(ArrayList<PlatformBlock> blocks) {
        for (PlatformBlock i : blocks) {
            Rectangle r3 = i.getBounds();
            Rectangle r2 = new Rectangle((int)x,(int)y-collisionRange,(int)w,collisionRange);
            if (r2.intersects(r3) && dy<0 && i.collide == true) {
                return true;
            }
        }
        return false;
    }
    boolean checkLeft(ArrayList<PlatformBlock> blocks) {
        for (PlatformBlock i : blocks) {
            Rectangle r3 = i.getBounds();
            Rectangle r2 = new Rectangle((int)x-collisionRange,(int)y+3,collisionRange,(int)h-6);
            if (r2.intersects(r3) && i.collide == true) {
                return true;
            }
        }
        return false;
    }
    boolean checkRight(ArrayList<PlatformBlock> blocks) {
        for (PlatformBlock i : blocks) {
            Rectangle r3 = i.getBounds();
            Rectangle r2 = new Rectangle((int)x+20,(int)y+3,collisionRange,(int)h-6);
            if (r2.intersects(r3) && i.collide == true) {
                return true;
            }
        }
        return false;
    }
    
    
    int getTouchedBlock(ArrayList<PlatformBlock> blocks) {
        for (int i=0; i<blocks.size(); i++) {
            Rectangle r = blocks.get(i).getBounds();
            Rectangle r3 = new Rectangle((int) x, (int) y + 20, (int) w, collisionRange);
            if (i+1 < blocks.size()){
                Rectangle r2 = blocks.get(i + 1).getBounds();
                if (r.intersection(r3).getWidth() >= r2.intersection(r3).getWidth()
                        && r.intersection(r3).getHeight() >= r2.intersection(r3).getHeight()) {
                    if (r.intersection(r3).getWidth() > 0 && r.intersection(r3).getHeight() > 0) {
                        if (blocks.get(i).collide == true) {
                            return i;
                        }else{
                            return i+1;
                        }
                    }
                }else if(r.intersection(r3).getWidth() < r2.intersection(r3).getWidth()
                        && r.intersection(r3).getHeight() <= r2.intersection(r3).getHeight()) {
                    if (r.intersection(r3).getWidth() > 0 && r.intersection(r3).getHeight() > 0) {
                        if (blocks.get(i + 1).collide == true) {
                            return i+1;
                        }else{
                            return i;
                        }
                    }
                }
            }else{
                if (r.intersects(r3) && blocks.get(i).collide == true){
                    return i;
                }
            }
        }
        return -1;
    }
}
