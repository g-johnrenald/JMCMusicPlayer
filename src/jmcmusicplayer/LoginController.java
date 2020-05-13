package jmcmusicplayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static jmcmusicplayer.Authentication.Authenticate;

public class LoginController implements Initializable {
    private Scene mainScene;

    //getting main scene from main method
    public void setMainScene(Scene scene) {
        this.mainScene = scene;
    }

    //method to open main scene
    public void openMainScene(ActionEvent event) {
        //getting current stage and set scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainScene);
    }

    //stores window's position
    Stage stage;
    double xOffset = 0;
    double yOffset = 0;

    @FXML
    private AnchorPane mainForm;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    User admin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create admin account as default login
        admin = new User("admin", HashUtil.getHashedPassword("admin", "admin"));
    }

    @FXML
    private void mainFormMousePressedHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void mainFormMouseDraggedHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    private void loginBtnHandler(ActionEvent actionEvent) {
        if (Authenticate(usernameTextField.getText(), passwordTextField.getText(), this.admin)) {
            this.openMainScene(actionEvent);
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Your username or password are incorrect. Please try again.");
            errorAlert.show();
        }
    }

    @FXML
    private void closeBtnHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        this.stage.close();
    }

    @FXML
    private void miniBtnHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        this.stage.setIconified(true);
    }

    @FXML
    private void helpBtnHandler(MouseEvent event) {
        String url = "src/help/help.html";
        File htmlFile = new File(url);

        try {
            //open the help file in a default browser
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "unable to open help file. " + ex);
            errorAlert.show();
        }
    }
}
