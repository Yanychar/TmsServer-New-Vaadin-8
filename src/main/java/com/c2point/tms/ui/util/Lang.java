package com.c2point.tms.ui.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {


	private ResourceBundle	bundle;
	private Locale			locale;
	private String 			resource;
	
	public Lang ( Locale locale, String resource  ) {
		
		this.bundle = null;
		this.locale = locale;
		this.resource = resource;
		
	}

	public void setBundle( ResourceBundle bundle ) {
		
		this.bundle = bundle;
	}

	public ResourceBundle getBundle() {
		
		return this.bundle;
	}

	public String getName() {
		
		if ( bundle != null && resource != null ) {
			
			return bundle.getString( resource );
		} 
		
		// If bundle or resource is missing than return locale.toString()
		return locale.toString();
		
	}
	
	public Locale getLocale() {
		
		return this.locale;
	}
	
	public String getResource() {
		
		return this.resource;
	}
	
}
