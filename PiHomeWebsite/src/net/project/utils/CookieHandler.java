package net.project.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility class to handle the cookies.
 * 
 * 
 */
public class CookieHandler {

	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;

	public CookieHandler(HttpServletRequest servletRequest){
		this.servletRequest = servletRequest;
	}

	public CookieHandler(HttpServletResponse servletResponse){
		this.servletResponse = servletResponse;
	}
	public CookieHandler(HttpServletRequest servletRequest, HttpServletResponse servletResponse){
		this.servletRequest = servletRequest;
		this.servletResponse = servletResponse;
	}


	public  Cookie verifyCookie( String cookieName ){

		Cookie cookie = null;

		for(Cookie c : servletRequest.getCookies()) {
			if (c.getName().equals(cookieName))
				cookie = c;
			break;
		}

		return cookie;
	}

	public  void saveCookie(String cookieName, int value){
		// Save to cookie
		Cookie div = new Cookie(cookieName, String.format("%d",value));
		div.setMaxAge(60*60*24*365); // Make the cookie last a year!
		servletResponse.addCookie(div);

	}
	public  void saveCookie(String cookieName, String value){
		// Save to cookie
		Cookie div = new Cookie(cookieName, value);
		div.setMaxAge(60*60*24*365); // Make the cookie last a year!
		servletResponse.addCookie(div);
	}

}
