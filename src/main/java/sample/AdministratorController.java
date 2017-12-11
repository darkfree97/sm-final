package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;

public class AdministratorController {
    String login;
    Integer user_id;
    Connection conn = null;

    @FXML
    private Tab informationTab;

    @FXML
    private Tab receptionTab;

    @FXML
    private Tab answerTab;

    @FXML
    private Tab commentTab;

    @FXML
    private TextArea informationAreaAdm;

    @FXML
    private Button informationSave;

    @FXML
    private Button informationChange;

    @FXML
    private ListView<String> receptionList;

    @FXML
    private TextField receptionIdField;

    @FXML
    private Button receptionApply;

    @FXML
    private ListView<String> answerList;

    @FXML
    private TextField answerIdField;

    @FXML
    private TextArea answerText;

    @FXML
    private Button answerApply;

    @FXML
    private TextField commentsIdField;

    @FXML
    private ListView<String> commentsList;

    @FXML
    private Button commentsApply;

    @FXML
    private MenuItem logOut;

    @FXML
    private Label informationMessage;

    @FXML
    private Label receptionMessage;

    @FXML
    private Label answerMessage;

    @FXML
    private Label commentMessage;


    private void updateInformation(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT information_text FROM information WHERE id=1";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                informationAreaAdm.setText(rs.getString("information_text"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean insertInformation(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "UPDATE information SET information_text='"+
                    informationAreaAdm.getText()+"' WHERE id="+1+";";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateReception(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT pryjom_do_likara.id, users.first_name, users.last_name, " +
                    "doctors.doctor_full_name " +
                    "FROM pryjom_do_likara " +
                    "JOIN users ON pryjom_do_likara.patient_id = users.id " +
                    "JOIN doctors ON pryjom_do_likara.doctor_id = doctors.id;";
            ResultSet rs = stmt.executeQuery(sql);
            receptionList.getItems().clear();
            while(rs.next()){
                String reception_temp = rs.getInt("id")+" | ";
                reception_temp += rs.getString("first_name")+" "+rs.getString("last_name")+" | ";
                reception_temp += rs.getString("doctor_full_name");
                receptionList.getItems().add(reception_temp);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean deleteReception(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM pryjom_do_likara WHERE id="+receptionIdField.getText()+";";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateAnswers(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT id, question_text,answer_text FROM question;";
            ResultSet rs = stmt.executeQuery(sql);
            answerList.getItems().clear();
            while(rs.next()){
                String temp_quest = rs.getInt("id")+" | ";
                temp_quest+=rs.getString("question_text")+"\n";
                temp_quest+=rs.getString("answer_text")+  "\n__________________________________";
                answerList.getItems().add(temp_quest);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean insertAnswer(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "UPDATE question SET answer_text='"+
                    answerText.getText()+"' WHERE id="+answerIdField.getText()+";";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateComment(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT comments.id, comments.response,users.last_name, users.first_name " +
                    "FROM comments " +
                    "JOIN users ON comments.patient_id = users.id;";
            ResultSet rs = stmt.executeQuery(sql);
            commentsList.getItems().clear();
            while(rs.next()){
                String reception_temp = rs.getInt("id")+" | ";
                reception_temp += rs.getString("first_name")+" "+rs.getString("last_name")+"\n";
                reception_temp += rs.getString("response");
                commentsList.getItems().add(reception_temp);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean deleteComment(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM comments WHERE id="+commentsIdField.getText()+";";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void initialize(){
        String url = "jdbc:sqlite:/home/darkfree/Projects/IdeaProjects/ModelLab10/data.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateInformation();
        informationTab.setOnSelectionChanged(p->{
            if(informationTab.isSelected()){
                updateInformation();
            }
        });

        informationChange.setOnAction(p->{
            informationAreaAdm.setEditable(true);
            informationSave.setDisable(false);
            informationChange.setDisable(true);
            informationMessage.setText("");
        });

        informationSave.setOnAction(p->{
            if(insertInformation()){
                informationMessage.setStyle("-fx-text-fill: darkgreen");
                informationMessage.setText("Дані успішно змінені!!!");
            }
            else {
                informationMessage.setStyle("-fx-text-fill: red");
                informationMessage.setText("Невдалося змінити дані!!!");
            }
            informationAreaAdm.setEditable(false);
            informationChange.setDisable(false);
            informationSave.setDisable(true);
        });

        receptionTab.setOnSelectionChanged(p->{
            if(receptionTab.isSelected()){
                updateReception();
            }
        });

        receptionApply.setOnAction(p->{
            if(receptionIdField.getText().equals("")){
                receptionMessage.setStyle("-fx-text-fill: red");
                receptionMessage.setText("Не вказано ідентифікатор!!!");
            }
            else if(deleteReception()){
                receptionMessage.setStyle("-fx-text-fill: darkgreen");
                receptionMessage.setText("Прийом успішно відмінено!!!");
                updateReception();
            }
            else {
                receptionMessage.setStyle("-fx-text-fill: red");
                receptionMessage.setText("Прийом не відмінено!!!!!!");
            }
        });

        answerTab.setOnSelectionChanged(p->{
            if(answerTab.isSelected()){
                updateAnswers();
            }
        });

        answerApply.setOnAction(p->{
            if(answerIdField.getText().equals("")||answerText.getText().equals("")){
                answerMessage.setStyle("-fx-text-fill: red");
                answerMessage.setText("Не вказано ідентифікатор або \nполе відповіді порожнє!!!");
            }
            else if(insertAnswer()){
                answerMessage.setStyle("-fx-text-fill: darkgreen");
                answerMessage.setText("Відповідь успішно змінено!!!");
                updateAnswers();
            }
            else {
                answerMessage.setStyle("-fx-text-fill: red");
                answerMessage.setText("Відповідь не змінено!!!");
            }
        });

        commentTab.setOnSelectionChanged(p->{
            if(commentTab.isSelected()){
                updateComment();
            }
        });

        commentsApply.setOnAction(p->{
            if(commentsIdField.getText().equals("")){
                commentMessage.setStyle("-fx-text-fill: red");
                commentMessage.setText("Не вказано ідентифікатор!!!");
            }
            else if(deleteComment()){
                commentMessage.setStyle("-fx-text-fill: darkgreen");
                commentMessage.setText("Відгук успішно видалено!!!");
                updateComment();
            }
            else {
                commentMessage.setStyle("-fx-text-fill: red");
                commentMessage.setText("Відгук не видалено!!!!!!");
            }
        });

        logOut.setOnAction(p->{
            ((Stage)logOut.getParentPopup().getOwnerWindow().getScene().getWindow()).close();
            try {
                Stage stage = new Stage();
                FXMLLoader autoriseLoader = new FXMLLoader(getClass().getResource("/fxml/authorise.fxml"));
                Parent root = autoriseLoader.load();
                stage.setTitle("Приватна поліклініка");
                stage.setScene(new Scene(root, 300, 275));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
