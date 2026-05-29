package motorph.gui;

import javax.swing.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("MotorPH Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Login Screen", SwingConstants.CENTER);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            new MainFrame();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }
}