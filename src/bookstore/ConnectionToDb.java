/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstore;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Tony NDOSS
 */
public class ConnectionToDb {

    ResultSet result = null;
    public static String globaUserName = "";

    public Statement Connection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:Database/bookStoreDatabase.db").createStatement();
    }

    public Boolean LoginBtnToDataBase(TextField userNameTxt, PasswordField userPwdTxt) {
        Boolean validation = false;
        globaUserName = "";
        result = null;
        try {
            result = Connection().executeQuery("Select UserName From User");
            while (result.next()) {
                if (userNameTxt.getText().equals(result.getString(1))) {
                    globaUserName = result.getString(1);
                } else {
                }
            }

            if ("".equals(globaUserName)) {
//                System.out.println("\nInvalid Username");
            } else {
                result = Connection().executeQuery("Select Password From User Where UserName = '" + globaUserName + "'");
                while (result.next()) {
                    if (userPwdTxt.getText().equals(result.getString(1))) {
                    } else {
                        globaUserName = "";
                    }
                }
                if ("".equals(globaUserName)) {
//                    System.out.println("\nWrong Password!!");
                } else {
//                    System.out.println("\nValidation Succeded!!");
                    validation = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }

        return validation;
    }

    public String getUserFullName() {
        String firstName = "";
        String lastName = "";
        try {
            result = Connection().executeQuery("Select FirstName, LastName From User Where UserName = '" + globaUserName + "'");
            while (result.next()) {
                firstName = result.getString(1);
                lastName = result.getString(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }

        return firstName + " " + lastName;
    }

    public List<String> getListBookByOption3(ChoiceBox listBookByOption1) {
        List<String> ls = new ArrayList<>();
        String listBookByOption1Value = listBookByOption1.getSelectionModel().getSelectedItem().toString();
        if ("Genre".equals(listBookByOption1Value)) {
            listBookByOption1Value = "BookGenre";
        } else {
        }
        try {
            result = Connection().executeQuery("Select Distinct " + listBookByOption1Value + " From Book where UserName = '" + globaUserName + "'");
            while (result.next()) {
                ls.add(result.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }

    public ObservableList<Book> getListOfBook(String selectionOfOptionOneAndThree, String priceAndStock, String searchSelection, String selectionSorting) {
        ObservableList<Book> observableList = FXCollections.observableArrayList();
        try {
            result = Connection().executeQuery("Select BookTitle, BookGenre, Author, Price, Stock From Book Where UserName = '"
                    + globaUserName + "'"
                    + selectionOfOptionOneAndThree
                    + priceAndStock
                    + searchSelection
                    + selectionSorting
            );
            while (result.next()) {
                observableList.addAll(new Book(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4), result.getInt(5)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return observableList;
    }

    //get id of book to update
    int getBookIdToUpdtStock(Book book) {
        int id = 0;
        try {
            result = Connection().executeQuery("Select BookId From Book Where UserName = '"
                    + globaUserName + "' And BookTitle = '"
                    + book.bookTitle + "' And BookGenre = '"
                    + book.bookGenre + "' And Author = '"
                    + book.author + "' And Price = "
                    + String.valueOf(book.price)
            );
            while (result.next()) {
                id = result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("\nThe id is :" + id);

        return id;
    }

    //update stock of book
    public void upDateBookStock(Book book) {
        try {
            Connection().executeUpdate("Update Book Set Stock = " + String.valueOf(book.stock) + " where BookId = " + String.valueOf(getBookIdToUpdtStock(book)));
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<String> getListOfCmbBoxLabelName(String labelName) {
        List<String> ls = new ArrayList<>();
        try {
            result = Connection().executeQuery("Select Distinct " + labelName + " From Book Where UserName = '" + globaUserName + "'");
            while (result.next()) {
                ls.add(result.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }

    //add new book to database
    public void addNewBook(String bookTitle, String bookGenre, String author, String price, String stock) {
        try {
            result = Connection().executeQuery("Select BookTitle, BookGenre, Author From Book Where UserName = '"
                    + globaUserName + "' And BookTitle like '"
                    + bookTitle + "' And BookGenre Like '"
                    + bookGenre + "' And Author Like '"
                    + author + "'"
            );
            
            if (result.next() != false) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Book exists Alread");
                alert.show();
            } else {
                Connection().executeUpdate("Insert Into Book Values ("
                        + String.valueOf(generateBookId()) + ", '"
                        + globaUserName + "', '"
                        + bookTitle + "', '"
                        + bookGenre + "', '"
                        + author + "', "
                        + price + ", "
                        + stock + ")"
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //delete a Book From Database
    public void deleteBook(String bookTitle, String bookGenre, String author){
        try {
            Connection().executeUpdate("Delete From Book Where UserName = '"
                    + globaUserName + "' And BookTitle like '"
                    + bookTitle + "' And BookGenre Like '"
                    + bookGenre + "' And Author Like '"
                    + author + "'"
            );
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int generateBookId() {
        int highestId = 0;
        try {
            result = Connection().executeQuery("Select BookId From Book");
            while (result.next()) {
                if (highestId < result.getInt(1)) {
                    highestId = result.getInt(1);
                } else {
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return highestId + 1;
    }
    
    //check if sign up username exist already
    public boolean isUserNameExist(String userName){
        boolean b = false;
        try {
            result = Connection().executeQuery("Select UserName From User Where UserName = '" + userName + "'");
            if(result.next()){
                b = true;
            }else{
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }
    
    //Inser New User Data in Database
    public void addNewUser(String firstName, String lastName, String userName, String email, String password){
        try {
            Connection().executeUpdate("Insert Into User Values ('"
                    + userName + "', '"
                    + firstName + "', '"
                    + lastName + "', '"
                    + email + "', '"
                    + password + "')"
            );
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
}
