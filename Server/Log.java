import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

public class Log extends JFrame {
    TCPServer server;
    public JTextArea textShow;

    Log(TCPServer server) {
        this.server = server;
        init();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("窗口关闭事件被触发");
                server.BYE();
                dispose();
            }
        });

        setTitle("日志");
        this.setVisible(true);
    }

    void init() {
        setLayout(new FlowLayout());
        textShow = new JTextArea(20, 55);
        textShow.setEditable(false);
        add(new JScrollPane(textShow));
        this.setBounds(500, 250, 600, 400);
    }

    void addlog(String information, String userId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (userId == null)
            textShow.append(localDateTime + "    " + "No id               " + information + "\n");
        else
            textShow.append(localDateTime + "    " + userId + "    " + information + "\n");
    }
}