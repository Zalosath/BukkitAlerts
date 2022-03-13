package com.jordna.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jordna.audio.AudioManager;
import com.jordna.error.ErrorManager;
import com.jordna.settings.UserSettings;
import com.jordna.tray.TrayManager;

public class AlertHandler
{

    // Checks for alerts!
    private ErrorManager errorManager;
    private AudioManager audioManager;

    private Gson gson;

    public AlertHandler(ErrorManager _errorManager, AudioManager _audioManager)
    {
	errorManager = _errorManager;
	audioManager = _audioManager;
	gson = new Gson();
    }
    
    public void start(LoginManager _loginManager, UserSettings _settings, TrayManager _trayManager)
    {
	if (_settings.getDelay() <= 0)
	{
	    errorManager.setError("You cannot have a delay shorter than or equal to 0 seconds.", true);
	    return;
	}

	Runnable checkAlertRunnable = new Runnable()
	{
	    @Override
	    public void run()
	    {
		if (!_loginManager.hasLoggedIn()) return;

					 // Sample alert, will fake a conversation alert
		String alertString = ""; //"{\"error\":[\"Security error occurred. Please press back, refresh the page, and try again.\"],\"templateHtml\":\"\\n\\n\\n\\n\\n<div class=\\\"errorOverlay\\\">\\n\\t<a class=\\\"close OverlayCloser\\\"><\\/a>\\n\\t\\n\\t\\t<h2 class=\\\"heading\\\">The following error occurred:<\\/h2>\\n\\t\\t\\n\\t\\t<div class=\\\"baseHtml\\\">\\n\\t\\t\\n\\t\\t\\t<label for=\\\"ctrl_0\\\" class=\\\"OverlayCloser\\\">Security error occurred. Please press back, refresh the page, and try again.<\\/label>\\n\\t\\t\\n\\t\\t<\\/div>\\n\\t\\n<\\/div>\",\"_visitor_conversationsUnread\":\"2\",\"_visitor_alertsUnread\":\"0\"}";
		try
		{
		    alertString = getAlertString();
		}
		catch (IOException e1)
		{
		    errorManager.setError("IOException when getting alert string\nSTACKTRACE:\n" + e1.getStackTrace());
		    return;
		}

		if (hasAlert(alertString))
		{
		    _trayManager.setNotification(true);
		    audioManager.playPing();
		}
		else
		{
		    _trayManager.setNotification(false);
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
	JsonObject alert = gson.fromJson(alertString, JsonObject.class);
	System.out.println(alert.toString());

	return hasAlertWithName(alert, "_visitor_conversationsUnread") || hasAlertWithName(alert, "_visitor_alertsUnread");
    }

    private boolean hasAlertWithName(JsonObject obj, String name)
    {
	System.out.println("CHECKING " + name);
	if (obj.has(name))
	{
	    int val = obj.get(name).getAsInt();
	    System.out.println(val);

	    if (val != 0) return true;
	}
	else
	{
	    errorManager.setError("Login was not successful. Check details.json", true);
	}
	return false;
    }

}
