package wqp2;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import java.awt.event.*;

import java.io.*;

public class Wqp2 extends JComponent{

    //0 start panel
    //10 choose M/F
    //1 game
    //2 wrong ans
    //3 loop
    //4 break
    //5 win
    //6 pass
    public int state = 0;
    public View v;
    public Model m;
    public String dir= "wqp2/data/";
    public int gender = 0;
    public int level = 1;
    public int maxLevel = 1;

    public int h = 800;
    public int w = 1200;

    public Wqp2(int h,int w){
        this.h = h;
        this.w = w;
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                handleKey(e.getKeyCode());
            }
        });

        this.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseClicked(MouseEvent e){
                handleMouse(e);
            }
        });
        v = new View(h, w, this);
    }

    public void handleMouse(MouseEvent e){
        if (state == 1){
            v.gb.check(e.getX(), e.getY());
            repaint();
        }

    }

    protected void paintComponent(Graphics g) {
        //int nw = this.getWidth();
        //int nh = this.getHeight();
        v.state = state;
        v.level = level;
        v.paint(g);

    }

    public void startGame(){
        m = new Model(v, gender, level);
        state = 1;
        repaint();
    }


    public void handleKey(int k){
        int ans;
        if(k == KeyEvent.VK_Q){
            System.exit(0);
        }
        switch(state){
            case 0:
                state = 10;
                break;
            case 10:
                switch(k){
                    case KeyEvent.VK_M:
                        gender = 0;
                        startGame();
                        break;
                    case KeyEvent.VK_F:
                        gender = 1;
                        startGame();
                        break;
                }
                break;
            case 1:
                switch(k){
                    case KeyEvent.VK_UP:
                        m.move(0,-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        m.move(0,1);
                        break;
                    case KeyEvent.VK_LEFT:
                        m.move(-1,0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        m.move(1,0);
                        break;
                    case KeyEvent.VK_K:
                        m.move(0,-1);
                        break;
                    case KeyEvent.VK_J:
                        m.move(0,1);
                        break;
                    case KeyEvent.VK_H:
                        m.move(-1,0);
                        break;
                    case KeyEvent.VK_L:
                        m.move(1,0);
                        break;
                    case KeyEvent.VK_R:
                        restart();
                        break;
                }
                break;
            case 2:
            case 3:
            case 4:
                restart();
                break;
            case 5:
                nextLevel();
                break;
            case 6:
                System.exit(0);

        }
        checkWin();
        repaint();
    }

    public void restart(){
        m = new Model(v,gender,level);
        state = 1;
    }

    public void nextLevel(){
        if(level == maxLevel){
            state = 6;
            m.gameState=-1;
            return;
        }
        level++;
        m = new Model(v, gender, level);
        state = 1;
    }

    public void checkWin(){
        //0 skip
        //1 wrong ans
        //2 loop
        //3 break
        //4 did choose a right ans
        //5 win
        if(m == null) return;
        int ans = m.gameState;
        switch(ans){
            case 0:
                state = 1;
                break;
            case 1:
                state = 2;
                v.waMsg = m.msg;
                break;
            case 2:
                state = 3;
                break;
            case 3:
                state = 4;
                break;
            case 4:
                state = 1;
                break;
            case 5:
                state = 5;
                break;
        }
    }
}


