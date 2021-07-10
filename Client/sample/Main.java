/*
Header
Name: Ahmed
Roll no: 18L-0950
email: l180950@lhr.nu.edu.pk
Section: 6A

This program allow a user to Register Login change password and change username
using javafx Gui.All the Validations/Insertions/Updations are performed on the server side
 */


package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main extends Application {

    //Encrypt Password Using bitwise operation
    private String encryptPass(String password)  {
        StringBuilder hashtext= new StringBuilder();
        char xorKey = password.charAt(password.length()/2);
        int len = password.length();
        for (int i = 0; i < len; i++)
        {
            hashtext.append((char) (password.charAt(i) ^ xorKey));
        }
        return hashtext.toString();
    }

    //Create Login Window adding labels Buttons and layouts etc
    private BorderPane createLoginWindow()
    {
        BorderPane Root=new BorderPane();

        AnchorPane Leftpane=new AnchorPane();

        AnchorPane Rightpane=new AnchorPane();

        Leftpane.setPrefHeight(400);

        Leftpane.setPrefWidth(257);

        Leftpane.setStyle("-fx-background-color:  #0096FF");

        Rightpane.setPrefHeight(400);

        Rightpane.setPrefWidth(344);

        Rightpane.setStyle("-fx-background-color: #ffffff");

        Label Registerlabel=new Label("Register");

        Registerlabel.setStyle("-fx-text-fill: #ffffff;-fx-font-size:24;");

        Registerlabel.setAlignment(Pos.CENTER);

        Registerlabel.setPrefHeight(58);

        Registerlabel.setPrefWidth(149);

        Registerlabel.setLayoutX(59);

        Registerlabel.setLayoutY(106);

        Label LoginLabel=new Label("Login");

        LoginLabel.setStyle("-fx-text-fill:  #0096FF;-fx-font-size:36;-fx-font-weight:Bold;");

        LoginLabel.setLayoutX(126);

        LoginLabel.setLayoutY(44);

        TextField UsernameField=new TextField();

        UsernameField.setPromptText("Username");

        UsernameField.setPrefHeight(26);

        UsernameField.setPrefWidth(230);

        UsernameField.setLayoutX(59);

        UsernameField.setLayoutY(155);

        PasswordField Passwordfield=new PasswordField();

        Passwordfield.setPromptText("Username");

        Passwordfield.setPrefHeight(26);

        Passwordfield.setPrefWidth(230);

        Passwordfield.setLayoutX(59);

        Passwordfield.setLayoutY(225);

        Label UsernameLabel=new Label("Username");

        UsernameLabel.setStyle("-fx-text-fill: #000000;-fx-font-weight:Bold;");

        UsernameLabel.setLayoutX(59);

        UsernameLabel.setLayoutY(131);

        Label Passwordlabel=new Label("Password");

        Passwordlabel.setStyle("-fx-text-fill: #000000;-fx-font-weight:Bold;");

        Passwordlabel.setLayoutX(59);

        Passwordlabel.setLayoutY(200);

        Label NotRegisteredlabel=new Label("Don't have an account?Register one!");

        NotRegisteredlabel.setStyle("-fx-text-fill: #ffffff;-fx-font-size:13;");

        NotRegisteredlabel.setAlignment(Pos.CENTER);

        NotRegisteredlabel.setLayoutX(32);

        NotRegisteredlabel.setLayoutY(164);

        Button RegisterButton=new Button("Register");

        RegisterButton.setStyle("-fx-text-fill: #0096ff;-fx-font-weight:Bold;-fx-background-radius:25;");

        RegisterButton.setPrefHeight(26);

        RegisterButton.setPrefWidth(200);

        RegisterButton.setLayoutX(32);

        RegisterButton.setLayoutY(210);

        //Onclicking Register Button Open Register Window and Close this window
        RegisterButton.setOnAction(event -> {

            Stage stage = (Stage) Root.getScene().getWindow();

            stage.close();

            Stage stage1 = new Stage();

            stage1.initStyle(StageStyle.UNDECORATED);

            stage1.setScene(new Scene(createRegisterWindow(), 600, 400));

            stage1.show();

        });

        Button LoginButton=new Button("Login");

        LoginButton.setStyle("-fx-text-fill:#ffffff;-fx-background-color: #0096ff;-fx-font-weight:Bold;-fx-background-radius:25;");

        LoginButton.setPrefHeight(26);

        LoginButton.setPrefWidth(200);

        LoginButton.setLayoutX(74);

        LoginButton.setLayoutY(298);

        //Onclicking Login Button
        LoginButton.setOnAction(event -> {
            //If Fields are Empty
           if(UsernameField.getText().isEmpty()||Passwordfield.getText().isEmpty())
           {
               Alert alert = new Alert(Alert.AlertType.WARNING);

               alert.setTitle("Warning");

               alert.setHeaderText("No Field can be Empty");

               alert.showAndWait();
           }
           //Encrypt password and send password and Username to Server and wait for Response if Failed invalid Username/password else Show Dashboard window
           else {
               String Encryptedpassword = encryptPass(Passwordfield.getText());
               try {
                   Socket s = new Socket("localhost", 6666);

                   ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());

                   //Creating String Object for Sending USERNAME etc
                   String[] Data = new String[3];

                   Data[0] = "Login";

                   Data[1] = UsernameField.getText();

                   Data[2] = Encryptedpassword;

                   //Write the Object
                   oout.writeObject(Data);

                   //Read the Response By Server
                   DataInputStream dis = new DataInputStream(s.getInputStream());

                   String str = dis.readUTF();

                   //if Success then close this window and open Dashboard
                   if (str.equals("Success")) {
                       Stage stage = (Stage) Root.getScene().getWindow();

                       stage.close();

                       Stage stage1 = new Stage();

                       stage1.initStyle(StageStyle.UNDECORATED);

                       stage1.setScene(new Scene(createLoggedinWindow(), 600, 400));

                       stage1.show();
                   }

                   //else show Invalid Error
                   else {
                       Alert alert = new Alert(Alert.AlertType.ERROR);

                       alert.setTitle("Error");

                       alert.setHeaderText("Invalid Username or Password");

                       alert.showAndWait();
                   }
                   oout.flush();

                   oout.close();

                   dis.close();

                   s.close();

               } catch (IOException e) {
                   JOptionPane.showMessageDialog(null, "Connection To Server failed");

                   System.out.println(e.toString());
               }
           }
        });

        Leftpane.getChildren().addAll(Registerlabel,NotRegisteredlabel,RegisterButton);

        Rightpane.getChildren().addAll(LoginLabel,UsernameLabel,UsernameField,Passwordlabel,Passwordfield,LoginButton);

        Root.setLeft(Leftpane);

        Root.setRight(Rightpane);

        return Root;

    }
    //Create Register Window adding labels Buttons and layouts etc
    private BorderPane createRegisterWindow()
    {
        BorderPane Root=new BorderPane();

        AnchorPane Leftpane=new AnchorPane();

        AnchorPane Rightpane=new AnchorPane();

        Leftpane.setPrefHeight(400);

        Leftpane.setPrefWidth(257);

        Leftpane.setStyle("-fx-background-color:  #0096FF");

        Rightpane.setPrefHeight(400);

        Rightpane.setPrefWidth(344);

        Rightpane.setStyle("-fx-background-color: #ffffff");

        Label LoginLabel=new Label("Login");

        LoginLabel.setStyle("-fx-text-fill: #ffffff;-fx-font-size:24;");

        LoginLabel.setAlignment(Pos.CENTER);

        LoginLabel.setPrefHeight(58);

        LoginLabel.setPrefWidth(149);

        LoginLabel.setLayoutX(59);

        LoginLabel.setLayoutY(106);

        Label Registerlabel=new Label("Register");

        Registerlabel.setStyle("-fx-text-fill:  #0096FF;-fx-font-size:36;-fx-font-weight:Bold;");

        Registerlabel.setLayoutX(100);

        Registerlabel.setLayoutY(44);

        TextField UsernameField=new TextField();

        UsernameField.setPromptText("Username");

        UsernameField.setPrefHeight(26);

        UsernameField.setPrefWidth(230);

        UsernameField.setLayoutX(59);

        UsernameField.setLayoutY(155);

        PasswordField Passwordfield=new PasswordField();

        Passwordfield.setPromptText("Username");

        Passwordfield.setPrefHeight(26);

        Passwordfield.setPrefWidth(230);

        Passwordfield.setLayoutX(59);

        Passwordfield.setLayoutY(225);

        Label UsernameLabel=new Label("Username");

        UsernameLabel.setStyle("-fx-text-fill: #000000;-fx-font-weight:Bold;");

        UsernameLabel.setLayoutX(59);

        UsernameLabel.setLayoutY(131);

        Label Passwordlabel=new Label("Password");

        Passwordlabel.setStyle("-fx-text-fill: #000000;-fx-font-weight:Bold;");

        Passwordlabel.setLayoutX(59);

        Passwordlabel.setLayoutY(200);

        Label NotLoginlabel=new Label("Already have an account?Login here!");

        NotLoginlabel.setStyle("-fx-text-fill: #ffffff;-fx-font-size:13;");

        NotLoginlabel.setAlignment(Pos.CENTER);

        NotLoginlabel.setLayoutX(32);

        NotLoginlabel.setLayoutY(164);

        Button LoginButton=new Button("Login");

        LoginButton.setStyle("-fx-text-fill: #0096ff;-fx-font-weight:Bold;-fx-background-radius:25;");

        LoginButton.setPrefHeight(26);

        LoginButton.setPrefWidth(200);

        LoginButton.setLayoutX(32);

        LoginButton.setLayoutY(210);

        //Open login Screen
        LoginButton.setOnAction(event -> {
            Stage stage = (Stage) Root.getScene().getWindow();

            stage.close();

            Stage stage1 = new Stage();

            stage1.initStyle(StageStyle.UNDECORATED);

            stage1.setScene(new Scene(createLoginWindow(), 600, 420));

            stage1.show();
        });

        Button RegisterButton=new Button("Register");

        RegisterButton.setStyle("-fx-text-fill:#ffffff;-fx-background-color: #0096ff;-fx-font-weight:Bold;-fx-background-radius:25;");

        RegisterButton.setPrefHeight(26);

        RegisterButton.setPrefWidth(200);

        RegisterButton.setLayoutX(74);

        RegisterButton.setLayoutY(298);

        //On Register Button Click
        RegisterButton.setOnAction(event -> {
            //If anyfield is empty show error
            if(UsernameField.getText().isEmpty()||Passwordfield.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setTitle("Warning");

                alert.setHeaderText("No Field can be Empty");

                alert.showAndWait();
            }
            else
            {
                //if password is not between 4-10 show error
                if (Passwordfield.getText().length()<4||Passwordfield.getText().length()>10)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");

                    alert.setHeaderText("Password Should be between 4-10 Characters");

                    alert.showAndWait();
                }
                //if Username is greater than 20 show error
                else if (UsernameField.getText().length()>20)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Username Should be Less than 20 Characters");
                    alert.showAndWait();
                }
                //Validate the Username and Register by Server
                else {
                    String Encryptedpassword = encryptPass(Passwordfield.getText());
                    try {
                        Socket s = new Socket("localhost", 6666);

                        ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());

                        //Write String Object with Information  to read by server
                        String[] Data = new String[3];

                        Data[0] = "Register";

                        Data[1] = UsernameField.getText();

                        Data[2] = Encryptedpassword;

                        oout.writeObject(Data);

                        DataInputStream dis = new DataInputStream(s.getInputStream());

                        //Read the Response by Server
                        String str = dis.readUTF();

                        //if Success show Successful Registration and open login Window
                        if (str.equals("Success")) {

                            JOptionPane.showMessageDialog(null,"Registered Successfully Please Login to Continue");

                            Stage stage = (Stage) Root.getScene().getWindow();

                            stage.close();

                            Stage stage1 = new Stage();

                            stage1.initStyle(StageStyle.UNDECORATED);

                            stage1.setScene(new Scene(createLoginWindow(), 600, 420));

                            stage1.show();
                        }

                        //if Failed then Username is not unique show error
                        else if(str.equals("Failed")) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);

                            alert.setTitle("Error");

                            alert.setHeaderText("Username is Already Present");

                            alert.showAndWait();
                        }

                        oout.flush();

                        oout.close();

                        dis.close();

                        s.close();
                    }
                    catch (IOException e)
                    {
                        JOptionPane.showMessageDialog(null, "Connection To Server failed");

                        System.out.println(e.toString());

                    }

                }

            }
        });

        Leftpane.getChildren().addAll(LoginLabel,NotLoginlabel,LoginButton);

        Rightpane.getChildren().addAll(Registerlabel,UsernameLabel,UsernameField,Passwordlabel,Passwordfield,RegisterButton);

        Root.setLeft(Rightpane);

        Root.setRight(Leftpane);

        return Root;

    }
    //Create after login is Successful Window adding labels Buttons and layouts etc
    private AnchorPane createLoggedinWindow()
    {
        AnchorPane Root=new AnchorPane();

        Root.setStyle("-fx-background-color:  #0096FF;");

        Root.setPrefWidth(600);

        Root.setPrefWidth(400);

        Label Dashboard=new Label("Dashboard");

        Dashboard.setStyle("-fx-text-fill: #ffffff;-fx-font-size:48px;-fx-font-weight:Bold;");

        Dashboard.setLayoutX(177);

        Dashboard.setLayoutY(49);

        Button ChangeUsername=new Button("Change Username");

        ChangeUsername.setLayoutX(153);

        ChangeUsername.setLayoutY(187);

        ChangeUsername.setPrefWidth(290);

        ChangeUsername.setPrefHeight(26);

        ChangeUsername.setStyle("-fx-background-color:#ffffff;-fx-text-fill: #0096FF;-fx-background-radius:25px");

        //On Clicking Change Username Create  Change username Window
        ChangeUsername.setOnAction(event -> {

            Stage Parentstage=(Stage) Root.getScene().getWindow();

            Parentstage.hide();

            AnchorPane Changeuserpane=new AnchorPane();

            Changeuserpane.setPrefWidth(500);

            Changeuserpane.setPrefHeight(300);

            Changeuserpane.setStyle("-fx-background-color: #0096FF");

            Label Oldusername=new Label("Old Username");

            Oldusername.setLayoutY(23);

            Oldusername.setLayoutX(176);

            Oldusername.setStyle("-fx-text-fill: #ffffff;-fx-font-weight:Bold;");

            Label Newusername=new Label("New Username");

            Newusername.setLayoutY(80);

            Newusername.setLayoutX(176);

            Newusername.setStyle("-fx-text-fill: #ffffff;-fx-font-weight:Bold;");

            Label Passoword=new Label("Password");

            Passoword.setLayoutY(135);

            Passoword.setLayoutX(176);

            Passoword.setStyle("-fx-text-fill: #ffffff;-fx-font-weight:Bold;");

            TextField Olduserfield=new TextField();

            Olduserfield.setPromptText("Old Username");

            Olduserfield.setStyle("-fx-background-radius: 25px;");

            Olduserfield.setPrefWidth(176);

            Olduserfield.setPrefHeight(26);

            Olduserfield.setLayoutX(176);

            Olduserfield.setLayoutY(47);

            TextField Newuserfield=new TextField();

            Newuserfield.setPromptText("New Username");

            Newuserfield.setStyle("-fx-background-radius: 25px;");

            Newuserfield.setPrefWidth(176);

            Newuserfield.setPrefHeight(26);

            Newuserfield.setLayoutX(176);

            Newuserfield.setLayoutY(102);

            PasswordField Passwordfield=new PasswordField();

            Passwordfield.setPromptText("Password");

            Passwordfield.setStyle("-fx-background-radius: 25px;");

            Passwordfield.setPrefWidth(176);

            Passwordfield.setPrefHeight(26);

            Passwordfield.setLayoutX(176);

            Passwordfield.setLayoutY(157);

            Button ChangeUserName=new Button("Change Username");

            ChangeUserName.setStyle("-fx-text-fill: #0096ff;-fx-background-color: #ffffff;-fx-background-radius:25px;");

            ChangeUserName.setPrefHeight(26);

            ChangeUserName.setPrefWidth(294);

            ChangeUserName.setLayoutX(117);

            ChangeUserName.setLayoutY(234);

            //On clicking change username button
            ChangeUserName.setOnAction(event1 -> {

                //If empty show error
                if(Olduserfield.getText().isEmpty()||Newuserfield.getText().isEmpty()||Passwordfield.getText().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Warning");

                    alert.setHeaderText("No Field Should be Empty");

                    alert.showAndWait();
                }

                //if new username >20
                else if (Newuserfield.getText().length()>20)
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Warning");

                    alert.setHeaderText("Username Should not be More than 20 Characters");

                    alert.showAndWait();
                }
                //Validate Uniqueness and then change
                else {

                    String Encryptedpassword = encryptPass(Passwordfield.getText());

                    try {
                        Socket s = new Socket("localhost", 6666);

                        ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());

                        //Send Data to Server
                        String[] Data = new String[4];

                        Data[0] = "changeusername";

                        Data[1] = Olduserfield.getText();

                        Data[2] = Encryptedpassword;

                        Data[3]=Newuserfield.getText();

                        //Write String Object
                        oout.writeObject(Data);

                        DataInputStream dis = new DataInputStream(s.getInputStream());

                        //Read Response by Server
                        String str = dis.readUTF();

                        //Username Changes Successfully
                        switch (str) {
                            case "Success":

                                JOptionPane.showMessageDialog(null, "Username Changed Successfully");

                                Stage stage = (Stage) Changeuserpane.getScene().getWindow();

                                stage.close();

                                Parentstage.show();

                                break;

                            //Invalid Old username or Password show error
                            case "Failed": {
                                Alert alert = new Alert(Alert.AlertType.ERROR);

                                alert.setTitle("Error");

                                alert.setHeaderText("Invalid Username or Password");

                                alert.showAndWait();
                                break;
                            }

                            //Username Already Exists show error
                            case "NotUnique": {

                                Alert alert = new Alert(Alert.AlertType.ERROR);

                                alert.setTitle("Error");

                                alert.setHeaderText("New Username is not Unique");

                                alert.showAndWait();
                                break;
                            }
                        }

                        oout.flush();

                        oout.close();

                        dis.close();

                        s.close();
                    }
                    catch (IOException e)
                    {
                        JOptionPane.showMessageDialog(null, "Connection To Server failed");

                        System.out.println(e.toString());

                    }

                }
            });

            //Add the Fields To changeUserpane and Show it
            Changeuserpane.getChildren().addAll(Oldusername,Olduserfield,Newusername,Newuserfield,Passoword,Passwordfield,ChangeUserName);


            Stage stage1 = new Stage();

            stage1.initStyle(StageStyle.UNDECORATED);

            stage1.setScene(new Scene(Changeuserpane, 500, 300));

            stage1.show();

        });

        //Create change password window
        Button ChangePassword=new Button("Change Password");

        ChangePassword.setLayoutX(153);

        ChangePassword.setLayoutY(240);

        ChangePassword.setPrefWidth(290);

        ChangePassword.setPrefHeight(26);

        ChangePassword.setStyle("-fx-background-color:#ffffff;-fx-text-fill: #0096FF;-fx-background-radius:25px");

        //Onclicking Cahnge Password Create Change Password Window
        ChangePassword.setOnAction(event -> {

            Stage Parentstage=(Stage) Root.getScene().getWindow();

            Parentstage.hide();

            AnchorPane Changepasspane=new AnchorPane();

            Changepasspane.setPrefWidth(500);

            Changepasspane.setPrefHeight(300);

            Changepasspane.setStyle("-fx-background-color: #0096FF");

            Label Username=new Label("Username");

            Username.setLayoutY(23);

            Username.setLayoutX(176);

            Username.setStyle("-fx-text-fill: #ffffff;-fx-font-weight:Bold;");

            Label Oldpassword=new Label("Old Password");

            Oldpassword.setLayoutY(80);

            Oldpassword.setLayoutX(176);

            Oldpassword.setStyle("-fx-text-fill: #ffffff;-fx-font-weight:Bold;");

            Label NewPassoword=new Label("New Password");

            NewPassoword.setLayoutY(135);

            NewPassoword.setLayoutX(176);

            NewPassoword.setStyle("-fx-text-fill: #ffffff;-fx-font-weight:Bold;");

            TextField Userfield=new TextField();

            Userfield.setPromptText("Username");

            Userfield.setStyle("-fx-background-radius: 25px;");

            Userfield.setPrefWidth(176);

            Userfield.setPrefHeight(26);

            Userfield.setLayoutX(176);

            Userfield.setLayoutY(47);

            PasswordField Oldpasswordfield=new PasswordField();

            Oldpasswordfield.setPromptText("Old Password");

            Oldpasswordfield.setStyle("-fx-background-radius: 25px;");

            Oldpasswordfield.setPrefWidth(176);

            Oldpasswordfield.setPrefHeight(26);

            Oldpasswordfield.setLayoutX(176);

            Oldpasswordfield.setLayoutY(102);

            PasswordField Newpasswordfield=new PasswordField();

            Newpasswordfield.setPromptText("New Password");

            Newpasswordfield.setStyle("-fx-background-radius: 25px;");

            Newpasswordfield.setPrefWidth(176);

            Newpasswordfield.setPrefHeight(26);

            Newpasswordfield.setLayoutX(176);

            Newpasswordfield.setLayoutY(157);

            Button Changepassword=new Button("Change Password");

            Changepassword.setStyle("-fx-text-fill: #0096ff;-fx-background-color: #ffffff;-fx-background-radius:25px;");

            Changepassword.setPrefHeight(26);

            Changepassword.setPrefWidth(294);

            Changepassword.setLayoutX(117);

            Changepassword.setLayoutY(234);

            //On clicking Change Password Send the data to server and validate
            Changepassword.setOnAction(event12 -> {

                //iF any field is Empty
                if(Oldpasswordfield.getText().isEmpty()||Newpasswordfield.getText().isEmpty()||Userfield.getText().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Warning");

                    alert.setHeaderText("No Field Should be Empty");

                    alert.showAndWait();
                }

                //if Newpassword length is not between 4-10
                else if(Newpasswordfield.getText().length()<4||Newpasswordfield.getText().length()>10)
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Warning");

                    alert.setHeaderText("New Password Should be between 4-10 Characters");

                    alert.showAndWait();
                }
                //Validate and Change password
                else {

                    String Encryptedpassword = encryptPass(Oldpasswordfield.getText());

                    String NewEncryptedpassword = encryptPass(Newpasswordfield.getText());

                    try {
                        Socket s = new Socket("localhost", 6666);

                        ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());

                        String[] Data = new String[4];

                        Data[0] = "changepassword";

                        Data[1] = Userfield.getText();

                        Data[2] = Encryptedpassword;

                        Data[3]=NewEncryptedpassword;

                        //Write String Object data to Server
                        oout.writeObject(Data);

                        DataInputStream dis = new DataInputStream(s.getInputStream());

                        //Raed Response by Server
                        String str = dis.readUTF();

                        //Password changed
                        if (str.equals("Success")) {

                            JOptionPane.showMessageDialog(null,"Password Changed Successfully");

                            Stage stage = (Stage) Changepasspane.getScene().getWindow();

                            stage.close();

                            Parentstage.show();

                        }
                        //Invalid username or old Password
                        else if(str.equals("Failed")) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);

                            alert.setTitle("Error");

                            alert.setHeaderText("Invalid Username or Password");

                            alert.showAndWait();
                        }

                        oout.flush();

                        oout.close();

                        dis.close();

                        s.close();
                    }
                    catch (IOException e)
                    {
                        JOptionPane.showMessageDialog(null, "Connection To Server failed");

                        System.out.println(e.toString());

                    }

                }



            });

            //Add all the Nodes in Parent and Display it
            Changepasspane.getChildren().addAll(Username,Userfield,Oldpassword,Oldpasswordfield,NewPassoword,Newpasswordfield,Changepassword);

            Stage stage1 = new Stage();

            stage1.initStyle(StageStyle.UNDECORATED);

            stage1.setScene(new Scene(Changepasspane, 500, 300));

            stage1.show();

        });

        Root.getChildren().addAll(Dashboard,ChangeUsername,ChangePassword);

        return Root;
    }

    //Start the Application
    @Override
    public void start(Stage primaryStage) {

        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.setScene(new Scene(createLoginWindow(), 600, 420));

        primaryStage.show();
    }


    public static void main(String[] args) {

        System.out.println("This program allow a user to Register Login change password and change username using javafx Gui.All the Validations/Insertions/Updations are performed on the server side");

        launch(args);
    }
    /*
    Test Cases
    IF Username is unique and less than 20 length and password
    is between 4-10 then Register him else show error
    On login if invalid Credentials show Invalid error
    Change username if new username is unique and less than 20 length and old username and password is valid
    Change Password if new Password is Between 4-10 length and  username and old password is valid
     */
}
