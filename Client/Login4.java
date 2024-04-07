
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login4 extends JFrame {
    private JTextField accountField;
    private String award;

    public Login4() {
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
                award = accountField.getText();
            }
        });

        panel.add(accountLabel);
        panel.add(accountField);
        panel.add(submitButton);
        
        System.out.println("Login4");
        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
    }

    public String getAward() {
        return award;
    }
}