import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField accountField;
    private String account;

    public Login() {
        setTitle("输入账号窗口");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel accountLabel = new JLabel("账号:");
        accountField = new JTextField(20);
        JButton submitButton = new JButton("确定");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                account = accountField.getText();
            }
        });

        panel.add(accountLabel);
        panel.add(accountField);
        panel.add(submitButton);
        add(panel);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getAccount() {
        return account;
    }
}