import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class Log extends JFrame{
    public JTextArea textShow;
    Log(){
        init();
        setTitle("日志");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    void init(){
        setLayout(new FlowLayout());
        textShow = new JTextArea(20,55);
        textShow.setEditable(false);
        add(new JScrollPane(textShow));
        this.setBounds(500,250,600,400);
    }
    void addlog(String imformation){
        LocalDateTime localDateTime = LocalDateTime.now();;
        textShow.append(localDateTime + "    " + imformation + "\n");
    }
}