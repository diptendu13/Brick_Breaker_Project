package DemoGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener{
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 300;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirX = -1;
    private int ballDirY = -2;
    private MapGenerator mapObj;

    public GamePlay(){
        mapObj = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g){
        // canvas
        g.setColor(Color.black);
        g.fillRect(1,1, 692, 592);

        // bricks
        mapObj.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592); // left border
        g.fillRect(0, 0, 692, 3); // top border
        g.fillRect(691, 0, 3, 592); // right border

        // score box
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // ledge
        g.setColor(Color.yellow);
        g.fillRect(playerX, 550, 100, 10);

        // ball
        g.setColor(Color.green);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (ballPosY > 570){
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER", 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Score : " + score, 190, 340);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter To Restart", 190, 380);
        }
        if (totalBricks == 0){
            play = false;
            ballDirX = -1;
            ballDirY = -2;

            g.setColor(Color.red);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER", 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Score : " + score, 190, 340);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter To Restart", 190, 380);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play){
            // logic if ball hits the ledge
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballDirY = -ballDirY;
            }
            // logic if the ball hits the bricks
            A:
            for (int i=0; i<mapObj.map.length; i++){
                for (int j=0; j<mapObj.map[0].length; j++){
                    if (mapObj.map[i][j] > 0){
                        int brickX = j * mapObj.brickWidth + 80;
                        int brickY = i * mapObj.brickHeight + 50;

                        int brickWidth = mapObj.brickWidth;
                        int brickHeight = mapObj.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)){
                            mapObj.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            // direction to move after reflection
                            if ((ballPosX + 19 <= brickRect.x) || (ballPosX + 1 >= brickRect.x + brickWidth)){
                                ballDirX = -ballDirX;
                            }
                            else{
                                ballDirY = -ballDirY;
                            }
                            break A;
                        }
                    }
                }
            }
            ballPosX += ballDirX;
            ballPosY += ballDirY;
            if (ballPosX < 0){
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0){
                ballDirY = -ballDirY;
            }
            if (ballPosX > 670){
                ballDirX = -ballDirX;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            if (playerX >= 600){
                playerX = 600;
            }
            else{
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            if (playerX < 10){
                playerX = 10;
            }
            else{
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            if (!play){
             ballPosX = 120;
             ballPosY = 350;
             ballDirX = -1;
             ballDirY = -2;
             score = 0;
             playerX = 300;
             totalBricks = 21;
             mapObj = new MapGenerator(3, 7);

             repaint();
            }
        }
    }
    public void moveRight(){
        play = true;
        playerX += 20;
    }
    public void moveLeft(){
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
}
