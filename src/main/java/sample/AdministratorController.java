package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AdministratorController {
    @FXML
    private MenuItem logOut;

    public void initialize(){
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
}
