import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame{
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public  DashboardForm(){
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean  hasRegisteredUsers = connectToDatabase();
        if (hasRegisteredUsers){
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }else {
                dispose();
            }
        }
        else {
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }else {
                dispose();
            }
        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New userL "+ user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;
        final String DB_URL = "jdbc:mysql://localhost:3306/";
        final String DATABASE_NAME = "user_data";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);
            Statement statement = conn.createStatement();
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS user_data");
        statement.close();
        conn.close();

        conn = DriverManager.getConnection(DB_URL + DATABASE_NAME, USERNAME, PASSWORD);
        statement = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS users ("
            + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
            + "name VARCHAR( 200 )NOT NULL,"
            + "email VARCHAR( 200 )NOT NULL UNIQUE,"
            + "phone VARCHAR( 200 ),"
            + "address VARCHAR( 200 ),"
            + "password VARCHAR( 200 ) NOT NULL" +
                ")";
        statement.executeUpdate(sql);

        statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");

            if (resultSet.next()){
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0){
                    hasRegisteredUsers=true;
                }
            }
        }catch (Exception e ){
            e.printStackTrace();
        }


        return  hasRegisteredUsers;
    }

    public static void main(String[] args) {
        DashboardForm myForm = new DashboardForm();
    }
}
