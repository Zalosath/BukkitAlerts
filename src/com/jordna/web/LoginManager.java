package com.jordna.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.jordna.error.ErrorManager;
import com.jordna.settings.UserSettings;

public class LoginManager 
{

	// Manages the whole logging in stuff
	private ErrorManager errorManager;
	private UserSettings settings;
	
	// Variables
	private CookieManager cookieManager;
	private String request = "";
	private URL url;
	private HttpsURLConnection conn = null;

	private boolean loggedIn;
	
	public LoginManager(ErrorManager _errorManager, UserSettings _settings)
	{
		errorManager = _errorManager;
		settings = _settings;
		
		login();
		
		/*
		try {
			tLogin();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("problem");
		}*/
	}
	
	private void tLogin() throws IOException
	{
		HttpsURLConnection.setFollowRedirects(true);
	     CookieManager manager = new CookieManager();
	     manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
	     CookieHandler.setDefault(manager);
	     HttpsURLConnection.setFollowRedirects(true);
	       {
	         String request = "";
	         URL obj = new URL("https://bukkit.org");
	         HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
	         conn.setUseCaches(false);
	         conn.setRequestMethod("GET");
	         conn.setRequestProperty("Host", "bukkit.org");
	         conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	         conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	         conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	         conn.setRequestProperty("Connection", "keep-alive");
	         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	         conn.setRequestProperty("Content-Length", Integer.toString(request.length()));
	         conn.setDoInput(true);
	         InputStream inStream = conn.getInputStream();
	         InputStreamReader streamReader = new InputStreamReader(inStream);
	         BufferedReader in = new BufferedReader(streamReader);
	         String inputLine;
	         StringBuffer response = new StringBuffer();
	         while ((inputLine = in.readLine()) != null) {
	           response.append(inputLine);
	         }
	         conn.disconnect();
	       }
	       { // Login
	         String request = "login=" + settings.getUsername() + "&register=0&password=" + settings.getPassword()
	             + "&cookie_check=1";
	         URL obj = new URL("https://bukkit.org/login/login");
	         HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
	         conn.setUseCaches(false);
	         conn.setRequestMethod("POST");
	         conn.setRequestProperty("Host", "bukkit.org");
	         conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	         conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	         conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	         conn.setRequestProperty("Connection", "keep-alive");
	         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	         conn.setRequestProperty("Content-Length", Integer.toString(request.length()));
	         conn.setDoOutput(true);
	         conn.setDoInput(true);
	         DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	         wr.writeBytes(request);
	         wr.flush();
	         wr.close();
	         if (conn.getResponseCode() == 303) {
	           String cookies = conn.getHeaderField("Set-Cookie");
	         }
	         InputStream inStream = conn.getInputStream();
	         InputStreamReader streamReader = new InputStreamReader(inStream);
	         BufferedReader in = new BufferedReader(streamReader);
	         String inputLine;
	         StringBuffer response = new StringBuffer();
	         while ((inputLine = in.readLine()) != null) {
	           response.append(inputLine);
	         }
	         conn.disconnect();
	         loggedIn = true;
	       }
	}
	
	private void login()
	{
		setupHttpsUrlConnection();
		setupCookieManager();
		setupHttpsUrlConnection();
		
		setupURL("https://bukkit.org");
		connectURLGet();
		
		setupURL("https://bukkit.org/login/login");
		connectURLPost();
		
	}
	
	private void setupHttpsUrlConnection()
	{
		HttpsURLConnection.setFollowRedirects(true);
	}
	
	private void setupCookieManager()
	{
	    cookieManager = new CookieManager();
	    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
	    CookieHandler.setDefault(cookieManager);
	}
	
	private void setupURL(String urlString)
	{
		try
		{
			url = new URL(urlString);
		}
		catch (MalformedURLException e)
		{
			errorManager.setError("Couldn't read URL " + url);
			return;
		}
	}
	
	private void connectURLGet()
	{
		try 
		{
			conn = (HttpsURLConnection) url.openConnection();
		} 
		catch (IOException e) 
		{
			errorManager.setError("IOException on conn");
			return;
		}
		
	    conn.setUseCaches(false);
	    
		try 
		{
			conn.setRequestMethod("GET");
		} 
		catch (ProtocolException e) 
		{
			errorManager.setError("ProtocolException thrown when setting request method");
			return;
		}
	    
		// Other connection properties
	    conn.setRequestProperty("Host", "bukkit.org");
	    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	    conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	    conn.setRequestProperty("Connection", "keep-alive");
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setRequestProperty("Content-Length", Integer.toString(request.length()));
	    
	    conn.setDoInput(true);
	    
	    InputStream inStream;
		try 
		{
			inStream = conn.getInputStream();
		    InputStreamReader streamReader = new InputStreamReader(inStream);
		    
		    BufferedReader in = new BufferedReader(streamReader);
		    StringBuffer response = new StringBuffer();
		    
		    String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
		} 
		catch (IOException e) 
		{
			errorManager.setError("IOException with GET request");
			return;
		}
	    
	    conn.disconnect();
	}
	
	private void connectURLPost()
	{
		request = "login=" + settings.getUsername() + "&register=0&password=" + settings.getPassword() + "&cookie_check=1&redirect=%2Faccount%2Falerts&_xfToken=\"";
		
		try 
		{
			conn = (HttpsURLConnection) url.openConnection();
		} 
		catch (IOException e) 
		{
			errorManager.setError("Failed to connect to URL");
			return;
		}
		
	    conn.setUseCaches(false);
	    
	    try 
	    {
			conn.setRequestMethod("POST");
		} 
	    catch (ProtocolException e) 
	    {
			errorManager.setError("ProtocolException thrown when setting request method");
	    	return;
		}
	    
	    conn.setRequestProperty("Host", "bukkit.org");
	    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	    conn.setRequestProperty("Accept", 
	        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	    conn.setRequestProperty("Connection", "keep-alive");
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setRequestProperty("Content-Length", Integer.toString(request.length()));
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
		
		try 
		{
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

		    wr.writeBytes(request);
		    wr.flush();
		    wr.close();
		    
		    @SuppressWarnings("unused")
			String cookies = "";
		    if (conn.getResponseCode() == 303)
		    	cookies = conn.getHeaderField("Set-Cookie"); 
		    
		    InputStream inputStream1 = conn.getInputStream();
		    InputStreamReader inputStreamReader1 = new InputStreamReader(inputStream1);
		    
		    BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
		    StringBuffer stringBuffer1 = new StringBuffer();
		    
		    String str1;
		    while ((str1 = bufferedReader1.readLine()) != null)
		      stringBuffer1.append(str1); 
		    
		    conn.disconnect();
		    bufferedReader1.close();
		    
		    loggedIn = true;
		} 
		catch (IOException e)
		{
			errorManager.setError("Failed to read data from POST connection");
			return;
		}
	}
	
	public boolean hasLoggedIn()
	{
		return loggedIn;
	}
	
}
