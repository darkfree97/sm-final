package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by darkfree on 10.12.17.
 */
public class SimpleUserController {
    String login;
    Integer user_id;
    Connection conn = null;
    List<Pair<Integer,String>> doctors = new ArrayList<>();

    @FXML
    private Tab showInfoTab;

    @FXML
    private Tab receptionLogerTab;

    @FXML
    private ChoiceBox<String> doctor;

    @FXML
    private ChoiceBox<String> meta_of_recaption;

    @FXML
    private TextArea contact_info;

    @FXML
    private DatePicker date;

    @FXML
    private CheckBox c1;

    @FXML
    private CheckBox c2;

    @FXML
    private CheckBox c3;

    @FXML
    private CheckBox c4;

    @FXML
    private CheckBox c5;

    @FXML
    private CheckBox c6;

    @FXML
    private Label recInfo;

    @FXML
    private Button recAplyBtn;

    @FXML
    private Tab askQuestionTab;

    @FXML
    private Tab leaveCommentTab;

    @FXML
    private MenuItem logOut;

    @FXML
    private ListView<String> questList;

    @FXML
    private TextArea questQuestion;

    @FXML
    private Button questApply;

    @FXML
    private Button questNew;

    @FXML
    private Label quest_mess;

    @FXML
    private ListView<String> commentList;

    @FXML
    private TextArea commentText;

    @FXML
    private Label commentMessage;

    @FXML
    private Button commentApply;

    @FXML
    private TextArea informationArea;


    private void getDoctors(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT id, doctor_full_name, profession FROM doctors";
            ResultSet rs = stmt.executeQuery(sql);
            doctors.clear();
            doctor.getItems().clear();
            while(rs.next()){
                doctors.add(new Pair<>(rs.getInt("id"),rs.getString("doctor_full_name")+" ("+rs.getString("profession")+")"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getUser(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE login='"+login+"';";
            ResultSet rs = stmt.executeQuery(sql);
            contact_info.clear();
            while(rs.next()){
                user_id = rs.getInt("id");
                contact_info.appendText("Прізвище: "+rs.getString("last_name")+"\n");
                contact_info.appendText("Ім'я: "+rs.getString("first_name")+"\n");
                contact_info.appendText("Дата народження: "+rs.getString("date_of_birth")+"\n");
                contact_info.appendText("Адреса: "+rs.getString("address")+"\n");
                contact_info.appendText("Телефон: "+rs.getString("phone")+"\n");
                contact_info.appendText("Email: "+rs.getString("email")+"\n");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getUserId(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE login='"+login+"';";
            ResultSet rs = stmt.executeQuery(sql);
            contact_info.clear();
            while(rs.next()){
                user_id = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateInformation(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT information_text FROM information WHERE id=1";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                informationArea.setText(rs.getString("information_text"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean insertReception(){
        Integer doctor_id = doctors.stream().filter(integerStringPair -> integerStringPair.getValue().contains(doctor.getValue())).findFirst().get().getKey();
        String simptoms = "";
        simptoms += c1.isSelected()?c1.getText()+" ":"";
        simptoms += c2.isSelected()?c2.getText()+" ":"";
        simptoms += c3.isSelected()?c3.getText()+" ":"";
        simptoms += c4.isSelected()?c4.getText()+" ":"";
        simptoms += c5.isSelected()?c5.getText()+" ":"";
        simptoms += c6.isSelected()?c6.getText()+" ":"";

        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO pryjom_do_likara(patient_id, destination, simptoms, doctor_id, date_of_pryjom)" +
                    " VALUES("+
                    user_id+",'"+
                    meta_of_recaption.getValue()+"','"+
                    simptoms+"',"+
                    doctor_id+",'"+
                    date.getEditor().getText()+"');";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateQuestionList(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT question_text,answer_text FROM question;";
            ResultSet rs = stmt.executeQuery(sql);
            questList.getItems().clear();
            while(rs.next()){
                String temp_quest = "Запитання: \n";
                temp_quest+=rs.getString("question_text")+"\n\nВідповідь:\n";
                temp_quest+=rs.getString("answer_text");
                questList.getItems().add(temp_quest);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean insertQuestion(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO question(question_text, asked_id)" +
                    " VALUES('"+
                    questQuestion.getText()+"',"+
                    user_id+");";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateCommentsList(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM comments JOIN users ON comments.patient_id=users.id";
            ResultSet rs = stmt.executeQuery(sql);
            commentList.getItems().clear();
            while(rs.next()){
                String temp_comment = rs.getString("first_name");
                temp_comment+=" "+rs.getString("last_name")+":\n";
                temp_comment+=rs.getString("response");
                commentList.getItems().add(temp_comment);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean insertComment(){
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO comments(response, patient_id)" +
                    " VALUES('"+
                    commentText.getText()+"',"+
                    user_id+");";
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
        showInfoTab.setOnSelectionChanged(p->{
            if(showInfoTab.isSelected()){
                updateInformation();
            }
        });

        receptionLogerTab.setOnSelectionChanged(__->{
            if(receptionLogerTab.isSelected()){
                login = (String)receptionLogerTab.getContent().getScene().getWindow().getUserData();
                getUser();
                getDoctors();
                doctors.forEach(p->doctor.getItems().add(p.getValue()));
                meta_of_recaption.getItems().clear();
                meta_of_recaption.getItems().addAll(Arrays.asList(
                        "Планова діагностика",
                        "Позапланова діагностика"
                ));
            }
        });

        recAplyBtn.setOnAction(p->{
            if(insertReception()){
                recInfo.setStyle("-fx-text-fill: darkgreen");
                recInfo.setText("Запис до лікаря успішно створено!!!");
                recAplyBtn.setDisable(true);
            }
            else {
                recInfo.setStyle("-fx-text-fill: red");
                recInfo.setText("Не вдалося створити запис!!!");
            }
        });

        askQuestionTab.setOnSelectionChanged(p->{
            if(askQuestionTab.isSelected()){
                login = (String)receptionLogerTab.getContent().getScene().getWindow().getUserData();
                getUserId();
                updateQuestionList();
            }
        });

        questApply.setOnAction(p->{
            if(questQuestion.getText().equals("")) return;
            if(insertQuestion()){
                quest_mess.setStyle("-fx-text-fill: darkgreen");
                quest_mess.setText("Додано нове запитання!!!");
                questApply.setDisable(true);
                questNew.setDisable(false);
            }
            else {
                quest_mess.setStyle("-fx-text-fill: red");
                quest_mess.setText("Запитання не було збережено!!!");
            }
            updateQuestionList();
        });

        questNew.setOnAction(p->{
            quest_mess.setText("");
            questApply.setDisable(false);
            questQuestion.clear();
            questNew.setDisable(true);
        });

        leaveCommentTab.setOnSelectionChanged(p->{
            if(leaveCommentTab.isSelected()){
                login = (String)receptionLogerTab.getContent().getScene().getWindow().getUserData();
                getUserId();
                updateCommentsList();
            }
        });

        commentApply.setOnAction(p->{
            if(commentText.getText().equals(""))return;
            if(insertComment()){
                commentMessage.setStyle("-fx-text-fill: darkgreen");
                commentMessage.setText("Відгук додано!!!");
                commentText.clear();
                updateCommentsList();
            }
            else {
                commentMessage.setStyle("-fx-text-fill: red");
                commentMessage.setText("Невдалося додати відгук!!!");
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
