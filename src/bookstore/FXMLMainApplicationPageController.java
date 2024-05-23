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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tony NDOSS
 */
public class FXMLMainApplicationPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    ChoiceBox listBookByOption1;

    @FXML
    ChoiceBox listBookByOption2;

    @FXML
    ChoiceBox listBookByOption3;

/////////////////////////////////
    @FXML
    ComboBox addNewBookGenreCombBox;

    @FXML
    ComboBox addNewBookAuthorCombBox;

    /////////////////////////////////
    @FXML
    ChoiceBox priceChBox;

    @FXML
    ChoiceBox stockChBox;

    /////////////////////////////////
    @FXML
    TextField priceTxt;

    @FXML
    TextField stockTxt;

    @FXML
    TextField searchBookTxt;

    @FXML
    TextField addNewBookTitleTxt;

    @FXML
    TextField amountOfBookToAdd;

    @FXML
    TextField amountOfBookToSub;

    @FXML
    TextField addNewBookPriceTxt;

    @FXML
    TextField addNewBookStockTxt;

    /////////////////////////////////
    @FXML
    Button applyBtn;

    @FXML
    Button logoutBtn;

    @FXML
    Button addBookBtn;

    @FXML
    Button substractBookBtn;

    @FXML
    Button deleteBookBtn;

    @FXML
    Button addNewBook;

    /////////////////////////////////
    @FXML
    TableView tableView;

    /////////////////////////////////
    @FXML
    TableColumn titleColumn;

    @FXML
    TableColumn genreColumn;

    @FXML
    TableColumn authorColumn;

    @FXML
    TableColumn priceColumn;

    @FXML
    TableColumn stockColumn;

//    FXMLLogingPageController logPControl = new FXMLLogingPageController();
    ConnectionToDb connectionToDb = new ConnectionToDb();

    /////////////////////////////////
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        listBookByOption1.getItems().addAll("Title (All Books)", "Genre", "Author");
        listBookByOption1.valueProperty().setValue("Title (All Books)");

        listBookByOption2.getItems().addAll("A - Z", "Z - A", "Last Added", "First Added");
        listBookByOption2.valueProperty().setValue("A - Z");

        listBookByOption3.setDisable(true);
        HandlerOfListBookByOption1(listBookByOption1, listBookByOption3);

        priceChBox.getItems().addAll("Up To", "From");
        priceChBox.valueProperty().setValue("Up To");

        stockChBox.getItems().addAll("Up To", "From");
        stockChBox.valueProperty().setValue("From");

        ValidateTxtInput(stockTxt, true);
        ValidateTxtInput(amountOfBookToAdd, true);
        ValidateTxtInput(amountOfBookToSub, true);
        ValidateTxtInput(addNewBookStockTxt, true);
        ValidateTxtInput(priceTxt, false);
        ValidateTxtInput(addNewBookPriceTxt, false);

        titleColumn.setCellValueFactory(new PropertyValueFactory("bookTitle"));
        genreColumn.setCellValueFactory(new PropertyValueFactory("bookGenre"));
        authorColumn.setCellValueFactory(new PropertyValueFactory("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory("stock"));

        applyBtnHandler();
        addBookBtnHandler(addBookBtn, amountOfBookToAdd, true);
        addBookBtnHandler(substractBookBtn, amountOfBookToSub, false);

        seTBoxesOfAddNewBook(addNewBookGenreCombBox, "BookGenre");
        seTBoxesOfAddNewBook(addNewBookAuthorCombBox, "Author");
        addNewBook();

        deleteBookBtnHandler();

        logoutBtnHandler();
    }

    //ValidateEntryMethod
    void ValidateTxtInput(TextField textField, boolean b) {
        textField.setOnKeyTyped(t -> {
            try {
                if (b) {
                    Integer.parseInt(textField.getText() + t.getCharacter());
                } else {
                    Double.parseDouble(textField.getText() + t.getCharacter());
                }
            } catch (NumberFormatException e) {
                t.consume();
            }
        });
    }

    //listBookByOption1 handler
    void HandlerOfListBookByOption1(ChoiceBox listBookByOption1, ChoiceBox listBookByOption3) {
        listBookByOption1.getSelectionModel().selectedItemProperty().addListener(ch -> {
            //System.out.println("\n" + listBookByOption1.getSelectionModel().getSelectedItem().toString());
            if ("Title (All Books)".equals(listBookByOption1.getSelectionModel().getSelectedItem().toString())) {
                listBookByOption3.setDisable(true);
            } else {
                listBookByOption3.getItems().clear();
                if (connectionToDb.getListBookByOption3(listBookByOption1).isEmpty()) {
                    listBookByOption3.getItems().add("");
                    listBookByOption3.valueProperty().setValue("");
                } else {
                    listBookByOption3.getItems().addAll(connectionToDb.getListBookByOption3(listBookByOption1));
                    listBookByOption3.valueProperty().setValue(connectionToDb.getListBookByOption3(listBookByOption1).get(0));
                }
                listBookByOption3.setDisable(false);
            }
        });
    }

    //set a handler on Apply Button
    void applyBtnHandler() {
        applyBtn.setOnMouseClicked(e -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(connectionToDb.getListOfBook(
                    getSelectionOfOptionOneAndThree(),
                    getPriceAndStock(),
                    getSearchSelection(),
                    getSelectionSorting()
            ));
            tableView.setPlaceholder(new Label("No Book Found"));
        });
    }

    /*
    From here let set methods that will help get the list of books according to specifications
     */
    //1st get selection string according to listBookByOption1 and listBookByOption3
    public String getSelectionOfOptionOneAndThree() {
        String str = "";
        if (listBookByOption3.isDisable() || "".equals(listBookByOption3.getSelectionModel().getSelectedItem().toString())) {
            return "";
        } else {
            if ("Genre".equals(listBookByOption1.getSelectionModel().getSelectedItem().toString())) {
                str = "BookGenre";
            } else {
                str = "Author";
            }
            return " And " + str + " = '" + listBookByOption3.getSelectionModel().getSelectedItem().toString() + "'";
        }
    }

    //2nd get selection sorting according to listBookByOption2
    public String getSelectionSorting() {
        if ("A - Z".equals(listBookByOption2.getSelectionModel().getSelectedItem().toString())) {
            return " Order By BookTitle ASC";
        } else if ("Z - A".equals(listBookByOption2.getSelectionModel().getSelectedItem().toString())) {
            return " Order By BookTitle DESC";
        } else if ("Last Added".equals(listBookByOption2.getSelectionModel().getSelectedItem().toString())) {
            return " Order By BookId DESC";
        } else {
            return " Order By BookId ASC";
        }
    }

    //3rd get Stock and Price selection condition
    public String getPriceAndStock() {
        String priceCondition = "";
        String stockCondition = "";

        if ("".equals(priceTxt.getText())) {
        } else {
            if ("Up To".equals(priceChBox.getSelectionModel().getSelectedItem().toString())) {
                priceCondition = " And Price <= " + priceTxt.getText();
            } else {
                priceCondition = " And Price >= " + priceTxt.getText();
            }
        }

        if ("".equals(stockTxt.getText())) {
        } else {
            if ("Up To".equals(stockChBox.getSelectionModel().getSelectedItem().toString())) {
                stockCondition = " And Stock <= " + stockTxt.getText();
            } else {
                stockCondition = " And Stock >= " + stockTxt.getText();
            }
        }
        return priceCondition + stockCondition;
    }

    //4th get search selection
    public String getSearchSelection() {
        if ("".equals(searchBookTxt.getText())) {
            return "";
        } else {
            return " And (BookTitle Like '%" + searchBookTxt.getText()
                    + "%' Or BookGenre Like '%" + searchBookTxt.getText()
                    + "%' Or Author Like '%" + searchBookTxt.getText() + "%')";
        }
    } // Now we can have a query to get books in the table according to user specifications 

    /*
    Add and Substract Book functionnality
     */
    //get details of selected Book
    Book getSelectedBook(TableView<Book> tableView) {
        return tableView.getSelectionModel().getSelectedItem();
    }

    //addBookBtn Handler
    void addBookBtnHandler(Button button, TextField textField, boolean bool) {
        button.setOnMouseClicked(e -> {
            if (getSelectedBook(tableView) == null) {
            } else {
                //System.out.println("\n" + getSelectedBook(tableView).stock);
                if (bool) {
                    if ("".equals(textField.getText()) || Integer.parseInt(textField.getText()) < 1) {
                    } else {
                        getSelectedBook(tableView).setStock(getSelectedBook(tableView).stock + Integer.parseInt(textField.getText()));
                        //System.out.println("\nthe book stoc is: " + getSelectedBook(tableView).bookTitle);
                        afterValidUpdateEntry();
                    }
                } else {
                    if ("".equals(textField.getText()) || Integer.parseInt(textField.getText()) < 1 || Integer.parseInt(textField.getText()) > getSelectedBook(tableView).stock) {
                    } else {
                        getSelectedBook(tableView).setStock(getSelectedBook(tableView).stock - Integer.parseInt(textField.getText()));
                        afterValidUpdateEntry();
                    }
                }
            }
        });
    }

    //void function to wrap code line after valid input in textfield
    void afterValidUpdateEntry() {
        if (confirmationDialog(getSelectedBook(tableView), true)) {
            connectionToDb.upDateBookStock(getSelectedBook(tableView));
        } else {
        }
        Book bookSelected = getSelectedBook(tableView);
        tableView.getItems().clear();
        tableView.getItems().addAll(connectionToDb.getListOfBook(
                getSelectionOfOptionOneAndThree(),
                getPriceAndStock(),
                getSearchSelection(),
                getSelectionSorting()
        ));
        tableView.getSelectionModel().select(bookSelected);
    }

    boolean confirmationDialog(Book book, boolean bool) {
        String str;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (bool) {
            str = "You are about to update '" + book.bookTitle + "' Stock to '" + book.stock + "' ";
            alert.setTitle("Confirmation");
        } else {
            alert.setTitle("Warning");
            str = "You are about to REMOVE '" + book.bookTitle + "' From System!!!";
        }
        alert.setHeaderText(str);
        alert.setContentText("Do you want to proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    //method to set Combox boxes of adding new box
    void seTBoxesOfAddNewBook(ComboBox comboBox, String labelName) {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(connectionToDb.getListOfCmbBoxLabelName(labelName));
        comboBox.setEditable(true);
    }

    //AddNewbookBtn handler
    void addNewBook() {
        addNewBook.setOnMouseClicked(e -> {
            if ("".equals(addNewBookTitleTxt.getText().trim())
                    || addNewBookAuthorCombBox.getValue() == null
                    || addNewBookGenreCombBox.getValue() == null
                    || "".equals(addNewBookAuthorCombBox.getValue().toString().trim())
                    || "".equals(addNewBookGenreCombBox.getValue().toString().trim())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Fill the Form Correctly");
                alert.show();
            } else {
                if ("".equals(addNewBookPriceTxt.getText())) {
                    addNewBookPriceTxt.setText("0");
                } else {
                }

                if ("".equals(addNewBookStockTxt.getText())) {
                    addNewBookStockTxt.setText("0");
                } else {
                }

                if (confirmationDialogToAddNewBook()) {
                    connectionToDb.addNewBook(addNewBookTitleTxt.getText(), addNewBookGenreCombBox.getValue().toString(), addNewBookAuthorCombBox.getValue().toString(), addNewBookPriceTxt.getText(), addNewBookStockTxt.getText());
                    tableView.getItems().clear();
                    tableView.getItems().addAll(connectionToDb.getListOfBook(
                            getSelectionOfOptionOneAndThree(),
                            getPriceAndStock(),
                            getSearchSelection(),
                            getSelectionSorting()
                    ));
                    tableView.setPlaceholder(new Label("No Book Found"));

                    seTBoxesOfAddNewBook(addNewBookGenreCombBox, "BookGenre");
                    seTBoxesOfAddNewBook(addNewBookAuthorCombBox, "Author");

                } else {
                }
            }
        });
    }

    //Delete selected book button handler
    void deleteBookBtnHandler() {
        deleteBookBtn.setOnMouseClicked(e -> {
            if (getSelectedBook(tableView) == null) {
            } else {
                if (confirmationDialog(getSelectedBook(tableView), false)) {
                    connectionToDb.deleteBook(getSelectedBook(tableView).bookTitle, getSelectedBook(tableView).bookGenre, getSelectedBook(tableView).author);
                    tableView.getItems().clear();
                    tableView.getItems().addAll(connectionToDb.getListOfBook(
                            getSelectionOfOptionOneAndThree(),
                            getPriceAndStock(),
                            getSearchSelection(),
                            getSelectionSorting()
                    ));
                    tableView.setPlaceholder(new Label("No Book Found"));

                    seTBoxesOfAddNewBook(addNewBookGenreCombBox, "BookGenre");
                    seTBoxesOfAddNewBook(addNewBookAuthorCombBox, "Author");
                }
            }
        });
    }

    //Confirmation Dialog To add New book To dataBase
    boolean confirmationDialogToAddNewBook() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You are about to Add New Book :"
                + "\nTile: " + addNewBookTitleTxt.getText()
                + "\nGenre: " + addNewBookGenreCombBox.getValue().toString()
                + "\nAuthor: " + addNewBookAuthorCombBox.getValue().toString()
                + "\nPrice: " + addNewBookPriceTxt.getText()
                + "\nStock: " + addNewBookStockTxt.getText());
        alert.setContentText("Do you want to proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    //Logout Button
    void logoutBtnHandler() {
        logoutBtn.setOnMouseClicked(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogingPage.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(FXMLLogingPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            connectionToDb.globaUserName = "";
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setTitle(connectionToDb.getUserFullName());
            Scene scene = new Scene(loader.getRoot());
            stage.setScene(scene);
            stage.show();
        });
    }

}
