import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Component {
    private Graphics2D g2d;
    private BufferedImage image;
    private int width;
    private int height;
    private final int FPS = 60;
    private boolean run = true;
    private final int Target_Time = 1000000000 / FPS;
    private Player player;
    private Key key;
    private List<Bullet>bulletList;
    private List<Alien>alienList;
    private int shotTime;
    private int score = 0;

    public void start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d=image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        Thread thread = new Thread(() -> {
            while (run) {
                long startTime=System.nanoTime();
                drawBackground();
                drawGame();
                Render();
                long time=System.nanoTime()-startTime;
                if(time<Target_Time)
                {
                    long s=(Target_Time-time)/1000000;
                    sleep(s);
                }

            }
        });
        initGame();
        initKeyboard();
        initBullet();
        thread.start();
    }

    private void initGame()
    {
        player=new Player();
        player.changLocation(150,150);
        alienList = Collections.synchronizedList(new ArrayList<>());
        new Thread(() -> {
            while (run) {
                addAlien();
                sleep(3000);
            }
        }).start();
    }

    public void addAlien()
    {
        Random random=new Random();
        int y1=random.nextInt(height-50)+25;
        Alien alien=new Alien();
        alien.changLocation(0,y1);
        alien.changeAngle(0);
        alienList.add(alien);

        int y2=random.nextInt(height-50)+25;
        Alien alien2=new Alien();
        alien2.changLocation(width,y2);
        alien2.changeAngle(180);
        alienList.add(alien2);
    }

    private void drawBackground()
    {
        try {
            BufferedImage backgroundImage =ImageIO.read(new File("src\\space\\s.jpg"));
            g2d.drawImage(backgroundImage, 0, 0, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //g2d.setColor(Color.BLACK); // خلفية سوداء للتجربة
        //g2d.fillRect(0, 0, width, height);
        //g2d.dispose();
    }


    private void resetGame() {
        score = 0;
        alienList.clear();
        bulletList.clear();
        player.changLocation(150, 150);
        player.reset();
    }

    private void initKeyboard()
    {
        key=new Key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    key.setKey_left(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    key.setKey_right(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    key.setKey_space(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_J)
                {
                    key.setKey_j(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    key.setKey_left(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    key.setKey_right(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    key.setKey_space(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_J)
                {
                    key.setKey_j(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(false);
                }
            }
        });
        new Thread(() -> {
            float s=0.5f;
            while (run) {
                if(player.isAlive()) {
                    float angle = player.getAngle();
                    if (key.isKey_left()) {
                        angle -= s;
                    }
                    if (key.isKey_right()) {
                        angle += s;
                    }
                    if (key.isKey_j()) {
                        if (shotTime == 0) {
                            synchronized (bulletList) {
                                bulletList.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 15, 3f));
                            }
                        }
                        shotTime++;
                        if (shotTime == 15) {
                            shotTime = 0;
                        }
                    } else {
                        shotTime = 0;
                    }
                    if (key.isKey_space()) {
                        player.speedUp();
                    } else {
                        player.speedDown();
                    }
                    player.updata();
                    player.changeAngle(angle);
                }
                else {
                    if (key.isKey_enter()) {
                        resetGame();
                    }
                }
                synchronized (alienList) {
                    Iterator<Alien> iterator = alienList.iterator();
                    while (iterator.hasNext()) {
                        Alien alien = iterator.next();
                        if (alien != null) {
                            alien.updata();
                            if (!alien.check(width, height)) {
                                iterator.remove();
                            }
                            else {
                                if (player.isAlive()){
                                    checkPlayer(alien);
                                }
                            }
                        }
                    }
                }
                sleep(5);
            }
        }).start();

    }

    private void initBullet() {
        bulletList = new CopyOnWriteArrayList<>();
        new Thread(() -> {
            while (run) {
                for (Bullet bullet : bulletList) {
                    bullet.updata();
                    checkBullets(bullet);
                    if (!bullet.check(width, height)) {
                        bulletList.remove(bullet);
                    }
                }
                        sleep(1);
            }
        }).start();
    }

    private void checkBullets(Bullet bullet) {
        synchronized (alienList) {
            Iterator<Alien> alienIterator = alienList.iterator();
            while (alienIterator.hasNext()) {
                Alien alien = alienIterator.next();
                Rectangle2D bulletBounds = bullet.getBounds();
                Rectangle2D alienBounds = alien.getBounds();
                if (bulletBounds.intersects(alienBounds)) {
                    score++;
                    alienIterator.remove();
                    synchronized (bulletList) {
                        bulletList.remove(bullet);
                    }
                    break;
                }
            }
        }
    }

    private void checkPlayer(Alien alien)
    {
        if(alien !=null)
        {
            Rectangle2D PlayerBounds = player.getBounds();
            if (PlayerBounds.intersects(alien.getBounds()))
            {
                if (! player.updateHP(0.1))
                {
                    player.setAlive(false);
                }
            }

        }
    }

    private void drawGame()
    {
        if(player.isAlive()) {
            player.draw(g2d);
        }
        for (int i=0;i<bulletList.size();i++)
        {
            Bullet bullet=bulletList.get(i);
            if (bullet != null)
            {
                bullet.draw(g2d);
            }
        }

        for (int i=0;i<alienList.size();i++)
        {
            Alien alien=alienList.get(i);
            if (alien != null)
            {
                alien.draw(g2d);
            }
        }

        g2d.setFont(getFont().deriveFont(Font.BOLD, 15f));
        g2d.drawString("Score : " + score, 10, 20);
        if (!player.isAlive()) {
            String text = "GAME OVER";
            String textKey = "Press key enter to Continue ...";
            g2d.setFont(getFont().deriveFont(Font.BOLD, 50f));
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r2 = fm.getStringBounds(text, g2d);
            double textWidth = r2.getWidth();
            double textHeight = r2.getHeight();
            double x = (width - textWidth) / 2;
            double y = (height - textHeight) / 2;
            g2d.drawString(text, (int) x, (int) y + fm.getAscent());
            g2d.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2d.getFontMetrics();
            r2 = fm.getStringBounds(textKey, g2d);
            textWidth = r2.getWidth();
            textHeight = r2.getHeight();
            x = (width - textWidth) / 2;
            y = (height - textHeight) / 2;
            g2d.drawString(textKey, (int) x, (int) y + fm.getAscent() + 50);
        }
    }

    private void Render()
    {
        Graphics g=getGraphics();
        g.drawImage(image,0,0,null);
        g.dispose();
    }

    private void sleep(long t)
    {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
