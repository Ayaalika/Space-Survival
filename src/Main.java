import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    Main ()
    {
        init();
    }

    public void init()
    {
        setTitle("Space Survival");
        setSize(1366,768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        Game gamePanel = new Game();
        gamePanel.setFocusable(true);

        WelcomePanel welcomePanel = new WelcomePanel(
                e -> {
                    cardLayout.show(container, "game");
                    gamePanel.start();
                    gamePanel.requestFocus();
                },
                e -> System.exit(0)
        );

        container.add(welcomePanel, "welcome");
        container.add(gamePanel, "game");

        cardLayout.show(container, "welcome");

        setContentPane(container);
    }

    public static void main(String[] args) {
        Main main=new Main();
        main.setVisible(true);
    }
}
