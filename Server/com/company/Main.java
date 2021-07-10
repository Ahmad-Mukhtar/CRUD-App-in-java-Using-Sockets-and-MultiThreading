/*
Header
Name: Ahmed
Roll no: 18L-0950
email: l180950@lhr.nu.edu.pk
Section: 6A

This program accepts Requests From Clients to validate login change username/Password
and Registering a user it checks data send by user and send Response accordingly
 */
package com.company;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("This program accepts Requests From Clients to validate login change username/Password and Registering a user it checks data send by user and send Response accordingly");

        //      Create Server Socket
        try{
            ServerSocket ss = new ServerSocket(6666);

            System.out.println("Server Started");

            //infinite Loop Server will Continue to Run
            while(true) {

                Socket s = ss.accept();//establishes connection

                //Client has Connected Create Class Object and pass the socket in its Constructor
                HandleClient handleClient=new HandleClient(s);

                //Run this Client Thread
                Thread thread=new Thread(handleClient);

                //Start the Thread
                thread.start();
            }
        }
        catch(Exception e){System.out.println(e);}
    }

}

//Class for Threading
class HandleClient implements Runnable
{
    private final Socket SOCKET;

    //Initilize Socket
    public HandleClient(Socket socket)
    {
        this.SOCKET=socket;
    }

    //Run the Thread
    @Override
    public void run() {

        try {
            //Connect with database
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Clients;integratedSecurity=true");

            //Read data from Client
            ObjectInputStream Ois = new ObjectInputStream(SOCKET.getInputStream());

            //Initilize string object


            //Read Data from String
            String  [] str = (String[])Ois.readObject();

            //Check What the User Requested
            switch (str[0])
            {

                //if Login
                case "Login":

                    CallableStatement callableStatement;

                    //Calling procedure signin with first input as userName second as password and third as an output value
                    callableStatement = connection.prepareCall("{call signin(?,?,?)}");

                    //Set the vale at respective Indexes
                    callableStatement.setString(1, str[1]);

                    callableStatement.setString(2, str[2]);

                    callableStatement.registerOutParameter(3, Types.INTEGER);

                    callableStatement.execute();

                    //get the result returned by procedure
                    int Result = callableStatement.getInt(3);

                    DataOutputStream dout = new DataOutputStream(SOCKET.getOutputStream());

                    //If Success then send Success message to client
                    if (Result == 1)
                    {
                        //login
                        dout.writeUTF("Success");
                    }
                    //else send Failed
                    else {
                        //nologin
                        dout.writeUTF("Failed");
                    }
                    dout.flush();

                    dout.close();

                    callableStatement.close();

                    break;

                    //if client requested Change username
                case "changeusername":

                    //check old username and password
                    //Calling procedure signin with first input as userName second as password and third as an output value
                    callableStatement = connection.prepareCall("{call signin(?,?,?)}");

                    callableStatement.setString(1, str[1]);

                    callableStatement.setString(2, str[2]);

                    callableStatement.registerOutParameter(3, Types.INTEGER);

                    callableStatement.execute();

                     Result = callableStatement.getInt(3);

                     dout = new DataOutputStream(SOCKET.getOutputStream());

                     //if User has entered vaid username and password
                    if (Result == 1) {

                        callableStatement = connection.prepareCall("{call checkusername(?,?)}");

                        callableStatement.setString(1, str[3]);

                        callableStatement.registerOutParameter(2, Types.INTEGER);

                        callableStatement.execute();

                        Result = callableStatement.getInt(2);

                        dout = new DataOutputStream(SOCKET.getOutputStream());

                        //username not Uniqie send Not unique Response to client
                        if (Result == 1)
                        {
                            dout.writeUTF("NotUnique");
                        }
                        //Update the username and send Sucess Response
                        else
                        {
                            PreparedStatement preparedStatement=connection.prepareStatement("update ClientInfo set Username=? where Username=?");

                            preparedStatement.setString(1,str[3]);

                            preparedStatement.setString(2,str[1]);

                            preparedStatement.executeUpdate();

                            preparedStatement.close();

                            dout.writeUTF("Success");
                        }
                    }
                    else {
                        //nologin
                        dout.writeUTF("Failed");
                    }

                    dout.flush();

                    dout.close();

                    break;

                    //If client wants to change password
                case "changepassword":

                    callableStatement = connection.prepareCall("{call signin(?,?,?)}");

                    callableStatement.setString(1, str[1]);

                    callableStatement.setString(2, str[2]);

                    callableStatement.registerOutParameter(3, Types.INTEGER);

                    callableStatement.execute();

                    Result = callableStatement.getInt(3);

                    dout = new DataOutputStream(SOCKET.getOutputStream());

                    //if User has entered vaid username and password update the password
                    if (Result == 1)
                    {

                        PreparedStatement preparedStatement = connection.prepareStatement("update ClientInfo set Password=? where Username=?");

                        preparedStatement.setString(1, str[3]);

                        preparedStatement.setString(2, str[1]);

                        preparedStatement.executeUpdate();

                        preparedStatement.close();

                        dout.writeUTF("Success");

                    }

                    //Invalid Username or oldpassword send failed Response
                    else {

                        dout.writeUTF("Failed");
                    }

                    dout.flush();

                    dout.close();

                    break;

                    //If a new user wants to Register
                case "Register":

                    //Call procedure to check if username Exists
                    callableStatement = connection.prepareCall("{call checkusername(?,?)}");

                    callableStatement.setString(1, str[1]);

                    callableStatement.registerOutParameter(2, Types.INTEGER);

                    callableStatement.execute();

                     Result = callableStatement.getInt(2);

                     dout = new DataOutputStream(SOCKET.getOutputStream());

                     //If exists send failed Responnse
                    if (Result == 1)
                    {
                        dout.writeUTF("Failed");
                    }
                    //Insert the User in database and send success response
                    else
                        {
                        PreparedStatement preparedStatement=connection.prepareStatement("Insert into ClientInfo values(?,?)");

                        preparedStatement.setString(1,str[1]);

                        preparedStatement.setString(2,str[2]);

                        preparedStatement.executeUpdate();

                        preparedStatement.close();

                        dout.writeUTF("Success");
                    }
                    dout.flush();

                    dout.close();

                    break;
            }

            Ois.close();

            connection.close();

            SOCKET.close();
        }
        catch (IOException | ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }

    }
}