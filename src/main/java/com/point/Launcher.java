package com.point;

import com.point.controllers.MainController;
import com.point.database.Database;
import com.point.models.Configuration;

public class Launcher {

	public static void main(String[] args) {		
    	Database.connection(); 	
    	Configuration.loadConfig();
		
		IndexApp.main(args);
		
	}

}
