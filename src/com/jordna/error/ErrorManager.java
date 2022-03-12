package com.jordna.error;

import com.jordna.tray.TrayManager;

public class ErrorManager
{

    private TrayManager trayManager;

    public ErrorManager(TrayManager _trayManager)
    {
	trayManager = _trayManager;
    }

    public void setError(String reason)
    {
	trayManager.setErroredIcon();
	System.out.println("There was an error: " + reason);
    }

}
