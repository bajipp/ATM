import java.io.*;
import java.net.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;


class TCPClient { 
    String username;
    String messageFromServer;
    String password;
    String money;
    String deposit;
    static Socket clientSocket;
    String lastOperation;
    Login login;
    Login2 login2;
    Login3 login3;
    Login4 login4;
    private JFrame frame;

    public TCPClient(){
        login = new Login();
        login2 = new Login2();
        
        login3 = new Login3();
        
        try {
            //连接网络
            clientSocket = new Socket("10.234.129.87", 2525);
            lastOperation = "UserId";
            UserId();
            //监视器,while(true)使之一直监视
            while (true) {
                monitor();
            }
        } catch (Exception e) {  //输出错误信息
            e.printStackTrace();
        }

    }

    void monitor() {
        try {
            //读取来自Server的信息
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            messageFromServer = inFromServer.readLine();
            //识别信息,跳入不同函数
            if (messageFromServer.startsWith("500 AUTH REQUIRE")) {  //收到了UserId，检查该ID是否在数据库
                lastOperation = "PassWord";
                PassWord();
            
            } else if (messageFromServer.startsWith("525 OK!")) {
                
                Window(); 
            } else if (messageFromServer.startsWith("AMNT:"))
            {
                ABLA();
            }

            else if (messageFromServer.startsWith("BYE"))
            {
                Close();
            }
            
             else if (messageFromServer.startsWith("401 ERROR!")) {
                System.out.println("进入handle401检测");
                handle401();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handle401() {
        try {
            System.out.println("进入handle401");
            switch (lastOperation) {
                case "UserId":
                    JOptionPane errorWindow = new JOptionPane();
                    errorWindow.showMessageDialog(login, "账号错误，请重新输入！","401 ERROR!",JOptionPane.WARNING_MESSAGE);
                    System.out.println("401 UNAUTHORIZED: Please enter your username again.");
                    UserId();
                    break;
                case "PassWord":
                    JOptionPane errorWindow2 = new JOptionPane();
                    errorWindow2.showMessageDialog(login2, "密码错误，请重新输入！","401 ERROR!",JOptionPane.WARNING_MESSAGE);
                    System.out.println("401 UNAUTHORIZED: Please enter your password again.");
                    PassWord();
                    break;
                case "BALA":
                    System.out.println("401 UNAUTHORIZED: Please try checking balance again.");
                    BALA();
                    break;
                case "WDARA":
                    JOptionPane errorWindow3 = new JOptionPane();
                    errorWindow3.showMessageDialog(login4, "余额不足，请重新输入！","401 ERROR!",JOptionPane.WARNING_MESSAGE);
                    System.out.println("401 UNAUTHORIZED: Please try withdrawing money again.");
                    WDARA();
                    break;
                default:
                    System.out.println("401 UNAUTHORIZED: Please try again.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Window() {
        login2.setVisible(false);
        frame = new JFrame("功能选择");
        frame.setSize(300, 200);
        
        JButton button1 = new JButton("查余额");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                BALA();
                
            }
        });

        JButton button2 = new JButton("取款");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                WDARA();
                
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                BYE();
             
            }
        });


        JPanel panel = new JPanel();
        panel.add(button1);
        panel.add(button2);
        panel.add(closeButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    void ABLA(){
        login3.setVisible(true);
        try {
            deposit = messageFromServer.substring(5);
                    System.out.println("余额：" );
                    System.out.println(deposit +'\n');
                    login3.showBalance(deposit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void UserId(){
        try {
            while(login.getAccount() == null || login.getAccount() == username){
                System.out.println(login.getAccount());
            };
            username = login.getAccount();
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("HELO " + username + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void PassWord(){
        login2.setVisible(true);
        login.setVisible(false);
        try {
            while(login2.getPassword() == null || login2.getPassword() == password){
                System.out.println(login2.getPassword());
            };
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            password = login2.getPassword();
            outToServer.writeBytes("PASS "+password + '\n');;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void BALA(){
        login2.setVisible(false);
        try {

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("取款"); 
                outToServer.writeBytes( "BALA"+ '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    
    void WDARA() {
        System.out.println("WDRA");
        Login4 login4 = new Login4();
        login4.setVisible(true);
        lastOperation = "WDARA";
    }

    class Login4 extends JFrame {
        private JTextField accountField;
        private String award;
    
        public Login4() {
            setTitle("输入取款金额窗口");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
            JPanel panel = new JPanel();
            JLabel accountLabel = new JLabel("金额:");
            accountField = new JTextField(20);
            JButton submitButton = new JButton("确定");
    
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    award = accountField.getText();
                    sendDataToServer(award);
                }
            });

    
            panel.add(accountLabel);
            panel.add(accountField);
            panel.add(submitButton);
    
            add(panel);
        }
    
        private void sendDataToServer(String money) {
            try {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes("WDRA " + money + '\n');
                outToServer.flush(); // Flush the output stream
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    void BYE(){
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("BYE" + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void Close(){
        try{
        clientSocket.close();
        frame.dispose();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String argv[]) throws Exception 
    {  
        TCPClient client = new TCPClient();  
    } 
} 