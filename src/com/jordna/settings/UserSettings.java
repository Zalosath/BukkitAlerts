package com.jordna.settings;

public class UserSettings 
{

	// Locally stored data, presuming you don't have a virus we should be good to store it in plain text
	private String username;
	private String password;
	
	// Any other settings
	private int delay;
	
	public UserSettings(String _username, String _password, int _delay)
	{
		username = _username;
		password = _password;
		delay = _delay;
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
	
}
