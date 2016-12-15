/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author fazeem
 */
public class TestApp extends Application {

    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        URL location = getClass().getResource("FXMLDocument.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) fxmlLoader.load(location.openStream());
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

    }

    @Override
    public void stop() {
        System.out.println("Application Closed by click to Close Button(X)");
        ((FXMLDocumentController) fxmlLoader.getController()).serialCommunicator.disconnect();
        System.exit(0);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //  new TestApp().startTimerTask();

        //dao.create(deviceInput);
        // System.out.println(""+DAO.readLasthourData());
        launch(args);

    }

}
