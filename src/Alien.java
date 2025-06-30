import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Alien {
    public static final double AlienSize =48;
    private double x;
    private double y;
    private final float speed=0.3f;
    private float angle=0;
    private final Image image;


    Alien()
    {
        image=new ImageIcon("src/space/alien.png").getImage();
    }

    public void updata()
    {
        x+=Math.cos(Math.toRadians(angle))*speed;
        y+=Math.sin(Math.toRadians(angle))*speed;
    }

    public void changLocation(double x,double y)
    {
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public void changeAngle(float angle)
    {
        if (angle<0)
        {
            angle=359;
        }
        else if(angle>359)
        {
            angle=0;
        }
        this.angle=angle;
    }

    public void draw(Graphics2D g2d) {
        AffineTransform oldTransform=g2d.getTransform();
        g2d.translate(x,y);
        g2d.drawImage(image,null,null);
        g2d.setTransform(oldTransform);

    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, AlienSize, AlienSize);
    }

    public boolean check(int width, int height) {
        Rectangle size = getBounds().getBounds();
        if (x <= -size.getWidth() || y < -size.getHeight() || x > width || y > height) {
            return false;
        } else {
            return true;
        }
    }
}
