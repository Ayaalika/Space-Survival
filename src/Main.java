import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
        setResizable(false);//*
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game game=new Game();
        add(game);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                game.start();
            }
        });
    }


    public static void main(String[] args) {
        Main main=new Main();
        main.setVisible(true);
    }
}
