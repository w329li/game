package wqp2;

import javax.swing.*;
import java.awt.*;

public class Wqp2Main{

    public static void main(String args[]) {
        int h = 800;
        int w = 1200;
        try{
            Wqp2 wq = new Wqp2(h,w);

            JFrame frame = new JFrame();
            frame.setLayout(new GridLayout(1,1));
            frame.setContentPane(wq);

            wq.setFocusable(true);
            wq.requestFocusInWindow();

            frame.setSize(w, h);
            frame.setVisible(true);

        } catch (Exception e){
            System.out.println("exception error");
        }
    }

}
