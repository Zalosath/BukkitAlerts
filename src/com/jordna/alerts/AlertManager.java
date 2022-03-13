package com.jordna.alerts;

import com.jordna.audio.AudioManager;
import com.jordna.error.ErrorManager;
import com.jordna.settings.UserSettings;
import com.jordna.tray.TrayManager;
import com.jordna.web.AlertHandler;
import com.jordna.web.LoginManager;

public class AlertManager
{

    /*
     * Steps for successful fetch and decode of alerts 1: Load various app settings,
     * paths, tray icon 2: Login to the website 3: Keep checking on a delay, the
     * number of alerts reported by bukkit.org/forums/.json 4: Use JSON library to
     * decode value 5: Ping when an alert is available 6: Win
     */

    private AudioManager audioManager;
    private ErrorManager errorManager;
    private UserSettings settings;
    private LoginManager loginManager;
    private AlertHandler alertHandler;
    private TrayManager trayManager;

    public AlertManager()
    {
	trayManager = new TrayManager();
	errorManager = new ErrorManager(trayManager);
	settings = new UserSettings(errorManager);

	audioManager = new AudioManager();
	audioManager.setPlayAudio(settings.getPlayPings());
	
	loginManager = new LoginManager(errorManager, settings);
	
	alertHandler = new AlertHandler(errorManager, audioManager);
	alertHandler.start(loginManager, settings, trayManager);
    }

}
