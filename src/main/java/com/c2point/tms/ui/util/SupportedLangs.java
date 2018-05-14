package com.c2point.tms.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SupportedLangs {

	private static Logger logger = LogManager.getLogger( SupportedLangs.class.getName());

//	public static final Locale LOCALE_NONE = new Locale("-");

	public static final Locale LOCALE_FI = new Locale("fi", "FI");

	public static final Locale LOCALE_ET = new Locale("et", "FI");

	public static final Locale LOCALE_EN = new Locale("en", "FI");

	public static final Locale LOCALE_SV = new Locale("sv", "FI");

	public static final Locale LOCALE_RU = new Locale("ru", "FI");

	public static final Locale DEFAULT_LOCALE = LOCALE_FI;

	private static SupportedLangs instance = null;
	private static Map<String, Lang> langMap;

	
	public static SupportedLangs getInstance() {
		
		if ( instance == null ) {
			
			instance = new SupportedLangs();
			instance.init();
		}
		
		return instance;
	}
	
	private void init() {
		
		langMap = new HashMap<String, Lang>();

		langMap.put( "language.en", new Lang( LOCALE_EN ));
		langMap.put( "language.fi", new Lang( LOCALE_FI ));
//		langMap.put( "language.sv", new Lang( LOCALE_SV ));
		langMap.put( "language.es", new Lang( LOCALE_ET ));
		langMap.put( "language.ru", new Lang( LOCALE_RU ));
		
	}
	

	public List<Lang> getAvailableLocales( ResourceBundle bundle ) {
		
		ArrayList<Lang> langs = new ArrayList<Lang>();
		
		langs.add( new Lang( bundle, LOCALE_EN, "language.en" ));
		langs.add( new Lang( bundle, LOCALE_FI, "language.fi" ));
//		langs.add( new Lang( bundle, LOCALE_SV, "language.sv" ));
		langs.add( new Lang( bundle, LOCALE_ET, "language.es" ));
		langs.add( new Lang( bundle, LOCALE_RU, "language.ru" ));
		

		return langs;
	}
	
	/**
	 * Load resource bundle for given Locale.
	 * 
	 * @param l
	 *            Locale of the resource bundle
	 * @return ResourceBundle or null if not found.
	 */
	public static ResourceBundle loadBundle(String bundleName, Locale l) {
		
		ResourceBundle b = null;
		try {
			b = ResourceBundle.getBundle(bundleName, l);
		} catch ( MissingResourceException e ) {
			logger.error( "Failed to load Resource Bundle '" + bundleName + "'" );
			b = null;
		}
		
		return b;
	}
	
}
