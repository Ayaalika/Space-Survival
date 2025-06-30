import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HpRender {

    private final HP hp;

    public HpRender(HP hp) {
        this.hp = hp;
    }

    protected void hpRender(Graphics2D g2d, Shape shape, double y) {
        if (hp.getCurrentHp() != hp.getMAX_HP()) {
            double hpY = shape.getBounds().getY() - y - 10;
            g2d.setColor(new Color(70, 70, 70));
            g2d.fill(new Rectangle2D.Double(0, hpY, Player.PlayerSize, 2));
            g2d.setColor(getHealthColor());
            double hpSize = hp.getCurrentHp() / hp.getMAX_HP() * Player.PlayerSize;
            g2d.fill(new Rectangle2D.Double(0, hpY, hpSize, 2));
        }
    }

    public boolean updateHP(double cutHP) {
        hp.setCurrentHp(hp.getCurrentHp() - cutHP);
        return hp.getCurrentHp() > 0;
    }

    public double getHP() {
        return hp.getCurrentHp();
    }

    public void resetHP() {
        hp.setCurrentHp(hp.getMAX_HP());
    }

    private Color getHealthColor() {
        double ratio = hp.getCurrentHp() / hp.getMAX_HP();
        if (ratio > 0.6) {
            return new Color(76, 175, 80);
        } else if (ratio > 0.3) {
            return new Color(255, 193, 7);
        } else {
            return new Color(253, 91, 91);
        }
    }
}
