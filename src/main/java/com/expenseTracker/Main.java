package com.expenseTracker;

import com.expenseTracker.gui.ExpTrackerGUI;

import com.expenseTracker.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db_Connection = new DatabaseConnection();
        try{
            Connection cn = db_Connection.getDBConnection();
            System.out.println("Database connection is successful");
        }
        catch(SQLException e){
            System.out.println("The Database connection has failed");
            System.exit(1);
        }

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.err.println("Could not set look and feel "+e.getMessage());
        }

        SwingUtilities.invokeLater(
            () -> {
                try{
                    new ExpTrackerGUI().setVisible(true);
                }catch(Exception e){
                    System.err.println("Error starting the application "+e.getLocalizedMessage());
                }
            }
        );
    }
}
