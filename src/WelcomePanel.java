import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class WelcomePanel extends JPanel {
    private BufferedImage backgroundImage;

    public WelcomePanel(ActionListener startAction, ActionListener quitAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            backgroundImage = ImageIO.read(new File("src\\space\\B.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setOpaque(false);

        JLabel title = new JLabel("🚀 Space Survival");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Orbitron", Font.BOLD, 42));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = createStyledButton("Start Game");
        JButton quitButton = createStyledButton("Quit");

        startButton.addActionListener(startAction);
        quitButton.addActionListener(quitAction);

        add(Box.createVerticalStrut(100));
        add(title);
        add(Box.createVerticalStrut(80));
        add(startButton);
        add(Box.createVerticalStrut(20));
        add(quitButton);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.YELLOW);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
