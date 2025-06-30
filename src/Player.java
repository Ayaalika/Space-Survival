import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Player extends HpRender{
    public static final double PlayerSize=64;
    private double x;
    private double y;
    private final float MaxSpeed=1f;
    private float speed=0f;
    private float angle=0f;
    private boolean speedUp;
    private final Image image;
    private final Image image_on;
    private boolean alive = true;
    Player()
    {
        super(new HP(50,50));
        image=new ImageIcon("src/space/plane.png").getImage();
        image_on=new ImageIcon("src/space/plane_on.png").getImage();
    }

    public void draw(Graphics2D g2d)
    {
        AffineTransform oldTransform=g2d.getTransform();
        g2d.translate(x,y);
        AffineTransform tran=new AffineTransform();
        tran.rotate(Math.toRadians(angle+45),PlayerSize/2,PlayerSize/2);
        g2d.drawImage(speedUp? image_on : image,tran,null);
        hpRender(g2d, getBounds(), y);
        g2d.setTransform(oldTransform);
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public void speedUp()
    {
        speedUp=true;
        if(speed>MaxSpeed)
        {
            speed=MaxSpeed;
        }
        else
        {
            speed+=0.01f;
        }
    }

    public void speedDown()
    {
        speedUp=false;
        if(speed<=0)
        {
            speed=0;
        }
        else
        {
            speed-=0.003f;
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, PlayerSize, PlayerSize);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void reset() {
        alive = true;
        resetHP();
        angle = 0;
        speed = 0;
    }
}
