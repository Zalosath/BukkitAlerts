package com.jordna.web;

public class AlertObject 
{
	public int _visitor_conversationsUnread = 0;
	public int _visitor_alertsUnread = 0;
	
	public boolean hasAlert()
	{
		return _visitor_conversationsUnread != 0 || _visitor_alertsUnread != 0;
	}
}
