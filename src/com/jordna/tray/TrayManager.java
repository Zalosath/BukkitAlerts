package com.jordna.tray;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;

public class TrayManager
{

    private TrayIcon trayIcon;

    private Image normalIcon;
    private Image erroredIcon;
    private Image notificationIcon;

    public TrayManager()
    {
	initializeIcons();
	createTrayIcon();
    }

    private void initializeIcons()
    {
	normalIcon = new ImageIcon(this.getClass().getResource("/icon.png")).getImage();
	erroredIcon = new ImageIcon(this.getClass().getResource("/icon_error.png")).getImage();
	notificationIcon = new ImageIcon(this.getClass().getResource("/icon_notification.png")).getImage();
    }

    private void createTrayIcon()
    {
	if (SystemTray.isSupported())
	{
	    PopupMenu menu = new PopupMenu();

	    MenuItem website = new MenuItem("Go to alerts");
	    MenuItem exit = new MenuItem("Exit");

	    menu.add(website);
	    menu.addSeparator();
	    menu.add(exit);

	    website.addActionListener(new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
		    if (Desktop.isDesktopSupported())
		    {
			try
			{
			    Desktop.getDesktop().browse(new URI("https://bukkit.org/account/alerts"));
			}
			catch (IOException e)
			{
			    return;
			}
			catch (URISyntaxException e)
			{
			    return;
			}
		    }
		}
	    });

	    exit.addActionListener(new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
		    System.exit(0);
		}
	    });

	    SystemTray systemTray = SystemTray.getSystemTray();
	    trayIcon = new TrayIcon(normalIcon, "Bukkit Alerts", menu);

	    try
	    {
		systemTray.add(trayIcon);
	    }
	    catch (AWTException e)
	    {
		return;
	    }
	}
    }

    public void setErroredIcon()
    {
	trayIcon.setImage(erroredIcon);
    }
    
    public void setNotification(boolean b)
    {
	if (b)
	{
	    trayIcon.setImage(notificationIcon);
	}
	else
	{
	    trayIcon.setImage(normalIcon);
	}
    }

}
