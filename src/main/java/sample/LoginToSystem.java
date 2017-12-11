package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginToSystem {
    @FXML
    private Button signUpBtn;

    @FXML
    private Button signInBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private Label message;


    public void initialize(){
        closeBtn.setOnAction(p-> {
            ((Stage)closeBtn.getScene().getWindow()).close();
        });

        signInBtn.setOnAction(p->{
            List<Boolean> checkRes = userCheck();
            if(checkRes.get(1)){
                try {
                    ((Stage)signInBtn.getScene().getWindow()).close();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.DECORATED);
                    FXMLLoader autoriseLoader;
                    if (checkRes.get(2)){
                        stage.setTitle("Приватна поліклініка | Режим модератора");
                        autoriseLoader = new FXMLLoader(getClass().getResource("/fxml/moderator.fxml"));
                    }
                    else {
                        stage.setTitle("Приватна поліклініка | Користувацький режим");
                        autoriseLoader = new FXMLLoader(getClass().getResource("/fxml/simple_user.fxml"));
                    }
                    Parent root = autoriseLoader.load();
                    stage.setUserData(login.getText());
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(checkRes.get(0)){
                message.setText("Невірний пароль");
            }
            else {
                message.setText("Користувача під таким\nіменем не зареєстровано!!!");
            }
        });

        signUpBtn.setOnAction(p->{
            try {
                FXMLLoader autoriseLoader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
                Parent root = autoriseLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("Реєстрація");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Stage)signUpBtn.getScene().getWindow()));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Boolean> userCheck() {
        List<Boolean> res = new ArrayList<>(Arrays.asList(false,false,false));
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:/home/darkfree/Projects/IdeaProjects/ModelLab10/data.db";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String sql = "SELECT login, password, admin FROM users";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(login.getText().equals(rs.getString("login"))){
                    if(password.getText().equals(rs.getString("password"))){
                        res = new ArrayList<>(Arrays.asList(true,true,rs.getBoolean("admin")));
                        break;
                    }
                    res = new ArrayList<>(Arrays.asList(true,false,false));
                }
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return res;
    }
}
