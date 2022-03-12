package com.jordna.web;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jordna.error.ErrorManager;
import com.jordna.settings.UserSettings;

public class AlertHandler
{

    // Checks for alerts!
    private ErrorManager errorManager;

    private Gson gson;

    public AlertHandler(ErrorManager _errorManager, LoginManager _loginManager, UserSettings _settings)
    {
	errorManager = _errorManager;
	gson = new Gson();

	Runnable checkAlertRunnable = new Runnable()
	{
	    @Override
	    public void run()
	    {
		if (!_loginManager.hasLoggedIn()) return;

		String alertString = null;
		try
		{
		    alertString = getAlertString();
		}
		catch (IOException e1)
		{
		    errorManager.setError("IOException when getting alert string");
		    return;
		}

		if (hasAlert(alertString))
		{
		    System.out.println("YOU'VE GOT AN ALERT");
		    errorManager.setError("alert");
		}
	    }
	};

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	executor.scheduleAtFixedRate(checkAlertRunnable, 1, _settings.getDelay(), TimeUnit.SECONDS);
    }

    private String getAlertString() throws IOException
    {
	URL uri = new URL("https://bukkit.org/forums/.json");

	URLConnection ec = uri.openConnection();
	BufferedReader in = new BufferedReader(new InputStreamReader(ec.getInputStream(), "UTF-8"));

	String inputLine;
	StringBuilder a = new StringBuilder();
	while ((inputLine = in.readLine()) != null)
	    a.append(inputLine);

	in.close();

	return a.toString();
    }

    private boolean hasAlert(String alertString)
    {
	try
	{
	    FileWriter myWriter = new FileWriter("error.txt");
	    myWriter.write(alertString);
	    myWriter.close();
	    System.out.println("Successfully wrote to the file.");
	}
	catch (IOException e)
	{
	    System.out.println("An error occurred.");
	    e.printStackTrace();
	}

	JsonObject alert = gson.fromJson(alertString, JsonObject.class);
	System.out.println(alert.toString());

	return hasAlertWithName(alert, "_visitor_conversationsUnread")
		|| hasAlertWithName(alert, "_visitor_alertsUnread");
    }

    private boolean hasAlertWithName(JsonObject obj, String name)
    {
	System.out.println("CHECKING " + name);
	if (obj.has(name))
	{
	    int val = obj.get("name").getAsInt();
	    System.out.println(val);

	    if (val != 0) return true;
	}
	return false;
    }

}
