
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login2 extends JFrame {
    private JTextField accountField;
    private String password;

    public Login2() {
        setTitle("输入密码窗口");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel accountLabel = new JLabel("密码:");
        accountField = new JTextField(20);
        JButton submitButton = new JButton("确定");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password = accountField.getText();
            }
        });

        panel.add(accountLabel);
        panel.add(accountField);
        panel.add(submitButton);
        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
    }

    public String getPassword() {
        return password;
    }
}