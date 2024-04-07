import javax.swing.*;
import java.awt.*;

public class Login3 extends JFrame{
    JTextArea textShow;
    Login3(){
        init();
        setTitle("余额界面");
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    void init(){
        setLayout(new FlowLayout());
        textShow = new JTextArea(1,20);
        textShow.setEditable(false);
        add (new JLabel("余额："));
        add(new JScrollPane(textShow));
        this.setSize(300, 150);
    }
    void showBalance(String imformation){
        textShow.append(imformation);
        
    }
}