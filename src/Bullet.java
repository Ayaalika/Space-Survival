import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Bullet {
    private double x;
    private double y;
    private final Shape shape;
    private final Color color = new Color(255, 255, 255);
    private final float angle;
    private float speed = 1f;
    private double size;

    Bullet(double x, double y, float angle, double size, float speed) {
        x += Player.PlayerSize / 2 - (size / 2);
        y += Player.PlayerSize / 2 - (size / 2);
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.size = size;
        this.speed = speed;
        shape = new Ellipse2D.Double(0, 0, size, size);
    }

    public void updata() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }

    public boolean check(int width, int height) {
        if (x <= -size || y < -size || x > width || y > height) {
            return false;
        } else {
            return true;
        }
    }

    public void draw(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();
        g2d.setColor(color);
        g2d.translate(x, y);
        g2d.fill(shape);
        g2d.setTransform(oldTransform);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, size, size);

    }
}
