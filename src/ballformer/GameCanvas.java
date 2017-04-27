
package ballformer;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;
import javax.swing.Timer;

public class GameCanvas extends Canvas implements Runnable, KeyListener{

    Timer timer;
    boolean rightPressed, leftPressed, spacePressed;
    Ball Player = new Ball(300,300);
    boolean computationDone = false;
    boolean threadStop = false;
    boolean gameover = false;
    int score = 0;
    ArrayList<PlatformBlock> blocks = new ArrayList<PlatformBlock>();
    byte level[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,
                    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
                    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
                    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
                    1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    
    GameCanvas() {
        super();
        setIgnoreRepaint(true);
        
        for (int i=0; i<level.length; i++) {
            if (level[i] == 1) {
                blocks.add(new PlatformBlock((i%20)*30,(i/20)*30,30,30));
            }
        }
        
        Chrono chrono = new Chrono(this);
        timer = new Timer(15, chrono);
        timer.start();
    }
    
    public synchronized void myRepaint() {

        if(!computationDone) {
            return;
        }
        
        BufferStrategy strategy = getBufferStrategy();
        Graphics graphics = strategy.getDrawGraphics();
        graphics.setColor(new Color(250,250,250));
        graphics.fillRect(0, 0, 600, 600);
        
        
        for (int i = 0; i < 22; i++) {
            int[] xpoints = {-40+(30*i),-10+(30*i),30+(30*i),0+(30*i)};
            int[] ypoints = {560,560,600,600};
            Polygon p = new Polygon(xpoints,ypoints,4);
            if (i%2 == 0) {
                graphics.setColor(new Color(255,255,200));
            }else {
                graphics.setColor(new Color(150,150,150));
            }
            graphics.fillPolygon(p);
        }

        for (int b = 0; b < blocks.size(); b++){
            blocks.get(b).MyRepaint(graphics);
        }
        Player.MyRepaint(graphics);
        
        //Scoreboard Transparency
        Graphics2D graphics2d = (Graphics2D)graphics;
        Composite origComp = graphics2d.getComposite();
        graphics2d.setPaint(new Color(180,180,180));
        float alpha = 0.75f;
        AlphaComposite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        graphics2d.setComposite(alphaComp);
        graphics2d.fillRect(5, 5, 150, 20);
        graphics2d.setColor(Color.black);
        graphics2d.drawString("Score: " + Integer.toString(score), 6, 20);
        graphics2d.setComposite(origComp);
        
        if (gameover) {
            graphics2d.setFont(new Font(Font.MONOSPACED,1,20));
            graphics2d.drawString("Press [SPACE] To Player Again", 150, 400);
            graphics2d.setColor(Color.red);
            graphics2d.setFont(new Font(Font.MONOSPACED,1,60));
            graphics2d.drawString("Gameover", 170, 330);
        }
        
        strategy.show();
        Toolkit.getDefaultToolkit().sync();
        
        computationDone = false;
    }
    
    public void run() {
        for(;;) {
        
            if(threadStop) {
                timer.stop();
                return;
            }
            if(computationDone) {
                try {
                    Thread.sleep(1L);
                    continue;
                }
                catch(Exception e) {
                }
            }
            if (!gameover) {
                
                //Player Left and Right Acceleration
                if (leftPressed) {
                    if (Player.getdx()>-5) {
                        Player.setdx(Player.getdx()-0.2);
                    }
                }else if (rightPressed) {
                    if (Player.getdx()<5) {
                        Player.setdx(Player.getdx()+0.2);
                    }
                }else{
                    if ((int)Player.getdx()>0.1) {
                        Player.setdx(Player.getdx() - 0.2);
                    }else if (Player.getdx()<-0.1) {
                        Player.setdx(Player.getdx() + 0.2);
                    }else{
                        Player.setdx(0);
                    }
                }

                //Check if on ground
                Player.checkOnGround(blocks);

                //Jump Movement
                if (spacePressed && Player.onGround) {
                    Player.setdy(-8);
                    Player.onGround = false;
                }

                //Gravity and Landing
                if (!Player.onGround) {
                    Player.setdy(Player.getdy()+0.2);
                    Player.sety(Player.gety() + Player.getdy());
                }else if (Player.getdy()>0){
                    Player.setdy(0);
                    int i = Player.getTouchedBlock(blocks);
                    if (i != -1) {
                        Player.sety(blocks.get(i).y-20);
                        blocks.get(i).changeColor(10);
                        score++;
                    }
                }

                //Above Detection.
                if (Player.getdy()<0 && Player.checkAbove(blocks)) {
                    Player.setdy(0);
                }
                if (Player.gety()<0) {
                    Player.aboveScreen = true;
                }else{
                    Player.aboveScreen = false;
                }

                // Collision for left and right
                if (Player.getdx()<0 && Player.checkLeft(blocks)) {
                    Player.setdx(0);
                    Player.setx((((int)Player.getx()+5)/30)*30);
                }
                if (Player.getdx()>0 && Player.checkRight(blocks)) {
                    Player.setdx(0);
                    Player.setx(((int)Player.getx()/30)*30+10);
                }

                //Move Player Left or Right
                Player.setx(Player.getx() + Player.getdx());

                //Die
                if (Player.gety() > 600) {
                    gameover = true;
                    Player.sety(300);
                    Player.setx(300);
                    Player.setdy(0);
                }
            }
            computationDone = true;
            
            
            if (spacePressed && gameover) {
                gameover = false;
                score = 0;
                for (PlatformBlock b : blocks) {
                    b.cInt = 0;
                    b.cInt2 = 0;
                    b.cInt3 = 0;
                    b.collide = true;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT: leftPressed = true;
            break;
            case KeyEvent.VK_RIGHT: rightPressed = true;
            break;
            case KeyEvent.VK_SPACE: spacePressed = true;
            break;
            default: break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT: leftPressed = false;
            break;
            case KeyEvent.VK_RIGHT: rightPressed = false;
            break;
            case KeyEvent.VK_SPACE: spacePressed = false;
            break;
            default: break;
        }
    }
}
