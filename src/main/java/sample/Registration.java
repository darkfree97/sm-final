package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class Registration {
    Connection conn = null;

    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;

    @FXML
    private DatePicker birth_date;

    @FXML
    private TextField address;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField pass_confirm;

    @FXML
    private Button okBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Label message;

    public void initialize(){
        String url = "jdbc:sqlite:/home/darkfree/Projects/IdeaProjects/ModelLab10/data.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeBtn.setOnAction(p->((Stage)closeBtn.getScene().getWindow()).close());
        okBtn.setOnAction(p->{
            if(!loginCheck()){
                message.setText("Користувач з ніком \""+login.getText()+"\" \nвже зареєстрований!!!");
            }
            else if(passwordCheck()){
                if(insertNewUser()){
                    message.setStyle("-fx-text-fill: darkgreen");
                    message.setText("Користувач успішно створений!!!");
                    okBtn.setDisable(true);
                }
                else {
                    message.setStyle("-fx-text-fill: red");
                    message.setText("Не вдалося створити користувача!!!");
                }
            }
        });
    }

    boolean passwordCheck(){
        if(password.getText().equals(pass_confirm.getText())){
            return true;
        }
        else {
            message.setText("Паролі не співпадають!!!");
            return false;
        }
    }

    boolean loginCheck(){
        boolean f = true;
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT login FROM users";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(login.getText().equals(rs.getString("login"))){
                    f = false;
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(login.getText().equals(""))
            f = false;
        return f;
    }

    boolean insertNewUser(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users(first_name, last_name, date_of_birth, address, phone, email, login, password)" +
                    " VALUES('"+
                    first_name.getText()+"','"+
                    last_name.getText()+"','"+
                    birth_date.getEditor().getText()+"','"+
                    address.getText()+"','"+
                    phone.getText()+"','"+
                    email.getText()+"','"+
                    login.getText()+"','"+
                    password.getText()+"');";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void finalize(){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
