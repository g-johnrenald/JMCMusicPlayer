/*
Copyright 2020 John Renald Garcines
Diploma of Software Development
South Metropolitan TAFE Murdoch Campus
*/
/*
    Created on : 2020/04/25, 18:25:16
    Author     : John
*/
package jmcmusicplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //getting loader and a pane for the login scene
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Parent loginPane = loginLoader.load();
        Scene loginScene = new Scene(loginPane);

        //getting a loader and a pane for the main scene
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("PlayerView.fxml"));
        Parent mainPane = mainLoader.load();
        Scene mainScene = new Scene(mainPane);

        //injecting main scene to the controller of login
        LoginController loginController = loginLoader.getController();
        loginController.setMainScene(mainScene);

        //injecting login scene to the controller of main
        PlayerController mainController = mainLoader.getController();
        mainController.setLoginScene(mainScene);

        primaryStage.initStyle(StageStyle.UNDECORATED); //exclude title bar
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
