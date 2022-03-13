package com.jordna.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jordna.error.ErrorManager;

public class UserSettings
{

    private ErrorManager errorManager;
    private Gson gson;

    // Locally stored data, presuming you don't have a virus we should be good to
    // store it in plain text
    private String username;
    private String password;

    // Any other settings
    private int delay;
    private boolean playPings;

    public UserSettings(ErrorManager _errorManager)
    {
	errorManager = _errorManager;
	gson = new Gson();
	
	retrieveDetails();
    }

    private void retrieveDetails()
    {
	File file = new File("details");
	if (!file.exists())
	{
	    file.mkdir();

	    try
	    {
		FileWriter myWriter = new FileWriter("details/details.json");
		myWriter.write("{\r\n"
			+ "    \"username\": \"\",\r\n"
			+ "    \"password\": \"\",\r\n"
			+ "    \"delay\": 30,\r\n"
			+ "    \"playPings\": true\r\n"
			+ "}");
		myWriter.close();
		System.out.println("Successfully wrote to the file.");
	    }
	    catch (IOException e)
	    {
		errorManager.setError("Failed to create details.json\nSTACKTRACE\n" + e.getStackTrace());
		return;
	    }

	    errorManager.setError("You haven't set your details yet, please fill in details/details.json to continue", true);
	    return;
	}
	
	File details = new File("details/details.json");
	
	Scanner reader;
	try
	{
	    reader = new Scanner(details);
	}
	catch (FileNotFoundException e)
	{
	    errorManager.setError("details/details.json couldn't be found, re-run the application", true);
	    return;
	}
	
	String data = "";
	while (reader.hasNextLine())
	{
	    data += reader.nextLine();
	}
	
	JsonObject detailsJson = gson.fromJson(data, JsonObject.class);
	
	if (detailsJson.has("username") && detailsJson.has("password") && detailsJson.has("delay") && detailsJson.has("playPings"))
	{
	    username = detailsJson.get("username").getAsString();
	    password = detailsJson.get("password").getAsString();
	    delay = detailsJson.get("delay").getAsInt();
	    playPings = detailsJson.get("playPings").getAsBoolean();
	}
	else
	{
	    errorManager.setError("details/details.json does not contain the correct fields, please restart the program to regenerate the file", true);
	}
	
	reader.close();
    }

    public String getUsername()
    {
	return username;
    }

    public String getPassword()
    {
	return password;
    }

    public int getDelay()
    {
	return delay;
    }
    
    public boolean getPlayPings()
    {
	return playPings;
    }

}
