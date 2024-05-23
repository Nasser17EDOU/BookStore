/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bookstore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tony NDOSS
 */
public class FXMLLogingPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button signUpBtn;
    @FXML
    Button loginBtn;
    @FXML
    TextField userNameTxt;
    @FXML
    PasswordField userPasswordTxt;
    
    public ConnectionToDb connectionToDb = new ConnectionToDb();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        LoginBtnHandler();
        
        signUpBtn.setOnMouseClicked((MouseEvent e) -> {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSignUpPage.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(FXMLLogingPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            signUpBtn.getScene().setRoot(loader.getRoot());
        });
    }
    
    void LoginBtnHandler() {
        loginBtn.setOnMouseClicked((MouseEvent e) -> {
            if (connectionToDb.LoginBtnToDataBase(userNameTxt, userPasswordTxt)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMainApplicationPage.fxml"));
                try {
                    loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLLogingPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                stage.setTitle(connectionToDb.getUserFullName());
                Scene scene = new Scene(loader.getRoot());
                stage.setScene(scene);
                stage.show();
            }
        });
    }
    
}
