/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bookstore;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tony NDOSS
 */
public class FXMLSignUpPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button goToLoginBtn;
    @FXML
    Button signUpBtn;

    @FXML
    TextField firstNameTxt;
    @FXML
    TextField lastNameTxt;
    @FXML
    TextField userNameTxt;
    @FXML
    TextField emailTxt;
    @FXML
    TextField pwdTxt;
    @FXML
    PasswordField confirmPwdTxt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        ConnectionToDb connectionToDb = new ConnectionToDb();

        goToLoginBtn.setOnMouseClicked((MouseEvent e) -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogingPage.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(FXMLLogingPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            goToLoginBtn.getScene().setRoot(loader.getRoot());
        });

        signUpBtn.setOnMouseClicked(e -> {
            if ("".equals(firstNameTxt.getText().trim()) || "".equals(lastNameTxt.getText().trim())
                    || "".equals(userNameTxt.getText().trim())
                    || "".equals(emailTxt.getText().trim())
                    || "".equals(pwdTxt.getText().trim())
                    || "".equals(confirmPwdTxt.getText())) {
            } else {
                if (!pwdTxt.getText().trim().equals(confirmPwdTxt.getText().trim())) {
                    errorAlertDialog("Password Does not Match!");
                } else {
                    if (connectionToDb.isUserNameExist(userNameTxt.getText().trim())) {
                        errorAlertDialog("This UserName is Used Already!!");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Your are about Sign Up");
                        alert.setTitle("Sign Up Confirmation");
                        Optional<ButtonType> reOptional = alert.showAndWait();
                        if (reOptional.get() == ButtonType.OK) {
                            connectionToDb.addNewUser(firstNameTxt.getText().trim(), lastNameTxt.getText().trim(), userNameTxt.getText().trim(), emailTxt.getText().trim(), pwdTxt.getText().trim());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMainApplicationPage.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLLogingPageController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Stage stage = (Stage) signUpBtn.getScene().getWindow();
                            connectionToDb.globaUserName = userNameTxt.getText().trim();
                            stage.setTitle(connectionToDb.getUserFullName());
                            Scene scene = new Scene(loader.getRoot());
                            stage.setScene(scene);
                            stage.show();
                        } else {
                        }
                    }
                }
            }
        });
    }

    void errorAlertDialog(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(error);
        alert.show();
    }

}
