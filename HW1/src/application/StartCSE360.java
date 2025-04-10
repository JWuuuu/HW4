package application;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;

import databasePart1.DatabaseHelper;


public class StartCSE360 extends Application {

	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	
	public static void main( String[] args )
	{
		 launch(args);
	}
	
	public void start(Stage primaryStage) {
	    try {
	        databaseHelper.connectToDatabase(); // Connect to the database

	        // 🔍 Show users for debug
	        databaseHelper.printAllUsers();

	        if (databaseHelper.isDatabaseEmpty()) {
	            new FirstPage(databaseHelper).show(primaryStage);
	        } else {
	            new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	        }
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}

}