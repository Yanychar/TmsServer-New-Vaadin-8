package com.c2point.tms;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.c2point.tms.entity.SessionData;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("tms")
public class TmsUI extends UI {

	private static final long serialVersionUID = -691835849583627833L;

	private static Logger logger = LogManager.getLogger( TmsUI.class.getName());
	
	private SessionData sessionData;
	
    @Override
    protected void init( VaadinRequest request ) {

    	LogManager.getLogger( TmsUI.class.getName());
    	
//		TmsConfiguration.readConfiguration(); // this );
//		TmsDBUpdate.updateTmsDatabase();
		
		sessionData = new SessionData();
		
/*		
		addListener( new Window.CloseListener() {
			   @Override
			    public void windowClose( CloseEvent e ) {
			       logger.debug( "Closing the application" );
			       getMainWindow().getApplication().close();
			    } 
			});		
*/
		// Gets current cookies
		Cookie[] cookies = request.getCookies();
		if ( cookies != null ) {
			getFromCookies( cookies );
		} else {
			if ( logger.isDebugEnabled()) logger.debug( "Cookies were read already!" );
		}
		
		LoginView loginView = new LoginView( this );

		putView( loginView );
		loginView.addLoginListener( this );

		setContent( loginView );
		
    }
    
	public SessionData getSessionData() {
		if ( sessionData == null ) {
			sessionData = new SessionData();
		}

		return sessionData;
	}

	public void deleteCookies() {
		storeInCookies( null, null, false, "" );
	}
	
	public void storeInCookies( String name, String pwd, boolean toRemember, String language ) {

		Cookie nameCookie = new Cookie( "storedname", name );
		Cookie pwdCookie = new Cookie( "storedpwd", pwd );
		Cookie rememberCookie = new Cookie( "storedrememberflag", Boolean.toString( toRemember ));
		Cookie languageCookie = new Cookie( "storedlanguage", language );
		
		
		nameCookie.setPath( "/" );
		pwdCookie.setPath( "/" );
		rememberCookie.setPath( "/" );
		languageCookie.setPath( "/" );
		
		if ( name != null && toRemember ) {
			// Store cookies
			nameCookie.setMaxAge( 2592000 ); // 30 days
			pwdCookie.setMaxAge( 2592000 ); // 30 days
			rememberCookie.setMaxAge( 2592000 ); // 30 days
			languageCookie.setMaxAge( 2592000 ); // 30 days
			if ( logger.isDebugEnabled()) logger.debug( "Cookies will be stored" );
		} else {
			// Delete cookies
			nameCookie.setMaxAge( 0 );
			pwdCookie.setMaxAge( 0 ); // 30 days
			rememberCookie.setMaxAge( 0 ); // 30 days
			languageCookie.setMaxAge( 0 ); // 30 days
			if ( logger.isDebugEnabled()) logger.debug( "Cookies will be deleted" );
		}

		VaadinServletResponse response = 
				   (VaadinServletResponse) VaadinService.getCurrentResponse();
		
		response.addCookie( nameCookie );
		response.addCookie( pwdCookie );
		response.addCookie( rememberCookie );
		response.addCookie( languageCookie );

		if ( logger.isDebugEnabled()) logger.debug( "Cookies were added to response" );
	}

	private void getFromCookies( Cookie[] cookies ) {
		if ( cookies != null ) {
			String name;
			for ( int i=0; i < cookies.length; i++ ) {
				name = cookies[ i ].getName();
				if ("storedname".equals( name )) {
					// Log the user in automatically
					storedName = cookies[ i ].getValue();
					if ( logger.isDebugEnabled()) logger.debug( "StoredName found and = " + storedName );
				} else if ("storedpwd".equals( name )) {
					storedPwd = cookies[ i ].getValue();
					if ( logger.isDebugEnabled()) logger.debug( "StoredPwd found and = " + storedPwd );
				} else if ("storedrememberflag".equals( name )) {
					String str = cookies[ i ].getValue();
					storedRememberFlag = Boolean.parseBoolean( str );
					if ( logger.isDebugEnabled()) logger.debug( "StoredRememberFlag found and = " + storedRememberFlag );
				} else if ("storedlanguage".equals( name )) {
					storedLanguage = cookies[ i ].getValue();
					if ( logger.isDebugEnabled()) logger.debug( "StoredLanguage found and = " + storedLanguage );
				} else {
					if ( logger.isDebugEnabled()) logger.debug( "Wrong cookies were found!" );
				}
			}
		} else {
			if ( logger.isDebugEnabled()) logger.debug( "There is no cookies stored!" );
		}
	}
	
	private String storedName = null;
	private String storedPwd = null;
	private boolean storedRememberFlag = false;
	private String storedLanguage = null;
	
	public String getNameFromCookies() { return storedName; }
	public String getPwdFromCookies() { return storedPwd; }
	public boolean getRememberFlagFromCookies() { return storedRememberFlag; }
	public String getLanguageFromCookies() { return storedLanguage; }

	private  void putView( AbstractMainView view ) {
		if ( !mapViews.containsKey( view.getClass().getSimpleName())) {
			mapViews.put( view.getClass().getSimpleName(), view );
		}
	}

	public String getResourceStr( String key ) {
		
		try {
			return this.getSessionData().getBundle().getString( key );
		} catch (Exception e) {
			logger.error(  "Could not find string resource '" + key + "'" );
		}
		return "";
	}

	@Override
	public void newUserLogged(TmsUser user) {

		if (mainView == null) {
			mainView = new MainView( this );
			mainView.initWindow();
		}

		setContent( mainView );
		
	}
    
    

    @WebServlet(urlPatterns = "/*", name = "TmsUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TmsUI.class, productionMode = false)
    public static class TmsUIServlet extends VaadinServlet {

    	private static final long serialVersionUID = -197057138263452976L;
    	
    }
}
