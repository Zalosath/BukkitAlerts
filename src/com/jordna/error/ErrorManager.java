package com.jordna.error;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jordna.tray.TrayManager;

public class ErrorManager
{

    private TrayManager trayManager;

    public ErrorManager(TrayManager _trayManager)
    {
	trayManager = _trayManager;

	createLogsDirectory();
    }

    public void setError(String reason)
    {
	setError(reason, false);
    }
    
    public void setError(String reason, boolean quitApplication)
    {
	trayManager.setErroredIcon();
	System.out.println("There was an error: " + reason);
	createLogFile(reason);
	
	if (quitApplication)
	{
	    System.exit(1);
	}
    }

    public void createLogFile(String error)
    {
	try
	{
	    String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    
	    FileWriter myWriter = new FileWriter("logs/" + fileName + ".log");
	    myWriter.write(error);
	    myWriter.close();
	    System.out.println("Successfully wrote to the file.");
	}
	catch (IOException e)
	{
	    // we can't do anything here, if we try to setError then we'll get a lovely
	    // infinite loop of trying to save, the best we can do is try to recreate the
	    // Logs directory and hope for the best
	    createLogsDirectory();
	    System.out.println("Failed due to IOException");
	    e.printStackTrace();
	    return;
	}
    }
    
    private void createLogsDirectory()
    {
	File directory = new File("logs");
	if (!directory.exists())
	{
	    directory.mkdir();
	}
    }

}
