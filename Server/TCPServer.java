import java.io.*;
import java.net.*;
import java.sql.*;

public class TCPServer {
    DBConnection dbConnection; // 连接数据库类
    Connection conn; // 数据库连接

    String messageToClient; // 来自Client的信息
    String messageFromClient; // 发送给Client的信息
    ServerSocket welcomeSocket; // 连接网络
    Socket connectionSocket; // 连接网络
    BufferedReader inFromClient; // 读来自Client信息
    DataOutputStream outToClient; // 向Client发送信息

    Log log;

    String userId;
    String password;

    public TCPServer() {
        log = new Log(this);
        conn = dbConnection.ConnectDB("clients", "root", "!XX0404DORAapi?"); // 初始化Connection
        try {
            // 连接网络
            welcomeSocket = new ServerSocket(2525);

            connectionSocket = welcomeSocket.accept();
            // 监视器,while(true)使之一直监视
            while (true) {
                monitor();
            }
        } catch (Exception e) { // 输出错误信息
            e.printStackTrace();
        }
    }

    void monitor() {
        try {
            // 读取来自Client的信息
            inFromClient = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream()));
            messageFromClient = inFromClient.readLine();
            log.addlog(messageFromClient,userId);
            // 识别信息,跳入不同函数
            if (messageFromClient.startsWith("HELO ")) // 收到了UserId，检查该ID是否在数据库
                HELO(messageFromClient);
            else if (messageFromClient.startsWith("BYE")) // 结束
                BYE();
            else if (messageFromClient.startsWith("PASS ")) { // 收到了password，查看是否与userid匹配
                PASS(messageFromClient);
            }
            else if (messageFromClient.startsWith("BALA"))
                BALA();
            else if (messageFromClient.startsWith("WDRA "))
                WDRA(messageFromClient);
            else {
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                System.out.println("指令错误");
                outToClient.writeBytes("401 ERROR!" + '\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // HELO
    void HELO(String messageFromClient) {
        userId = messageFromClient.substring(5); // 后半段是userId
        String sql = "select * from users where UserId = '" + userId + "'"; // 查询语句
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql); // 查询结果
            if (resultSet.next()) { // 查询结果不为空，则请求密码
                System.out.println("ServerHelo，成功匹配");
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                outToClient.writeBytes("500 AUTH REQUIRE" + '\n');
                log.addlog("500 AUTH REQUIRE",userId);
            } else { // 查询结果为空，则返回401
                System.out.println("ServerHelo，匹配失败");
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                outToClient.writeBytes("401 ERROR!" + '\n');
                log.addlog("401 ERROR!",userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PASS
void PASS(String messageFromClient) {
    password = messageFromClient.substring(5); // 后半段是password
    String sql = "select * from users where UserId = '" + userId + "' and PassWord = '" + password + "'"; // 查询语句

    try {
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery(); // 执行查询
        if (resultSet.next()) { // 查询结果不为空，则请求密码
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            outToClient.writeBytes("525 OK!" + '\n');
            log.addlog("525 OK!",userId);
        } else { // 查询结果为空，则返回401
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            System.out.println("PASSWORD错误");
            outToClient.writeBytes("401 ERROR!" + '\n');
            log.addlog("401 ERROR!",userId);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // BALA
    void BALA() {
        System.out.println("进入BALA");
        String sql = "select * from users where UserId = '" + userId + "'"; // 查询语句
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql); // 查询结果
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            resultSet.next();
            String balance = resultSet.getString("Balance");
            outToClient.writeBytes("AMNT:" + balance + '\n'); // Balance
            log.addlog("AMNT:" + balance,userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WDRA
    void WDRA(String messageFromClient) {
        System.out.println("进入WDAR");
        String withdrawal = messageFromClient.substring(5); // 后半段是取款金额
        String sql = "select * from users where UserId = '" + userId + "'"; // 查询语句
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql); // 查询结果

            resultSet.next();
            String balance = resultSet.getString("Balance");
            double doubleWithdrawal = Double.parseDouble(withdrawal);
            double doubleBalance = Double.parseDouble(balance);
            if (doubleWithdrawal <= doubleBalance) // 余额充足
            {
                System.out.println("余额足");
                sql = "update users set Balance = " + (doubleBalance - doubleWithdrawal) + "where UserId = " + userId;
                try {
                    PreparedStatement preparedStatementUpdate = conn.prepareStatement(sql);
                    
                    preparedStatementUpdate.executeUpdate(sql);
                    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    outToClient.writeBytes("525 OK!" + '\n');
                    log.addlog("525 OK!",userId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { // 余额不足
                System.out.println("余额不足");
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                outToClient.writeBytes("401 ERROR!" + '\n');
                log.addlog("401 ERROR!",userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // BYE
    void BYE() {
        try {
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            outToClient.writeBytes("BYE" + '\n');
            log.addlog("BYE",userId);
            conn.close();
            connectionSocket.close();
            welcomeSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
