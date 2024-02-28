import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 650;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH* SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 180; //to slow the game- high value
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 4; // initial
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdaptor());
        startGame();

    }
    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(DELAY, this); 
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            for(int i=0; i<SCREEN_WIDTH/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE,  SCREEN_WIDTH, i*UNIT_SIZE);
            }
            g.setColor(Color.ORANGE);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i=0; i<bodyParts; i++){
                if(i==0){    ///head
                    g.setColor(new Color(45,180,100));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{           ///body
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        g.setColor(Color.gray);
        g.setFont(new Font("", Font.BOLD, 40));
        
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score "+applesEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move(){
        for(int i= bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        
        switch(direction){
            case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
            case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
            case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
        }

    }
    public void checkApple(){
        if((x[0] == appleX)&&(y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
        
    }
    public void checkCollisions(){
        // if head eats body
        for(int i=bodyParts; i>0; i--){
            if((x[0]==x[i])&&(y[0]==y[i]))
            {
                running = false;
            }
        }
        // if head hits left, right border
        if((x[0]<0 )||(x[0]>=SCREEN_WIDTH)){
            running = false;
        }
        // if head hits top, bottom border
        if((y[0]<0)|| y[0]>=SCREEN_HEIGHT){
            running = false;
        }
        if(running == false){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.MAGENTA);
        g.setFont(new Font("", Font.BOLD, 90));
        
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        g.setColor(Color.RED);
        g.setFont(new Font("", Font.BOLD, 60));
        
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score "+applesEaten))/2, g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdaptor extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction!= 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!= 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!= 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!= 'U'){
                        direction = 'D';
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
