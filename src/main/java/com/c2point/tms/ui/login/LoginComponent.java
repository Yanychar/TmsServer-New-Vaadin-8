package com.c2point.tms.ui.login;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.c2point.tms.TmsUI;
import com.c2point.tms.ui.util.Lang;
import com.c2point.tms.ui.util.SupportedLangs;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class LoginComponent extends CustomComponent {
	
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger( LoginComponent.class.getName());

	TmsUI					app;
	
	private TextField 			user;
	private PasswordField 		pwd;
	private ComboBox<Lang>		languageSelect;
	private CheckBox 			rememberMeButton;
	private Button 				okButton;
	private Button 				forgotButton;
	
	public LoginComponent( TmsUI app ) {
		super();
		
		this.app = app;
		initView();
		
		fillContent();
		
	}

	public void addLoginButtonListener( Button.ClickListener listener ) {
		okButton.addClickListener( listener );
	}
	
	private void initView() {
		
		Panel panel = new Panel();
		setCompositionRoot( panel );

		setWidthUndefined();
		setHeight(  "20ex" );
		
		// Add components
		// Add components
		user = new TextField();
		pwd = new PasswordField();
		user.setWidth("20em");
		pwd.setWidth("20em");

		languageSelect = new ComboBox<Lang>( 
				null, 
				SupportedLangs.getAvailableLocales( app.getSessionData().getBundle())
		);
		
		languageSelect.setItemCaptionGenerator( Lang::getName );
		languageSelect.setEmptySelectionAllowed( false );

		languageSelect.addValueChangeListener( event -> {

			Locale locale;
			try {
				
				locale = languageSelect.getValue().getLocale();
				
			} catch ( Exception e ) {
				logger.error( "Cannot get locale from Combo. Will use default!" );
				locale = SupportedLangs.DEFAULT_LOCALE;
				
			}
			
			ResourceBundle bundle = SupportedLangs.loadBundle( "com.c2point.tms.web.resources.WebResources", locale );
			if ( bundle == null && locale != SupportedLangs.DEFAULT_LOCALE ) {
				locale = SupportedLangs.DEFAULT_LOCALE;
				bundle = SupportedLangs.loadBundle( "com.c2point.tms.web.resources.WebResources", locale );
					
			} 
				
			app.getSessionData().setBundle( bundle );
			app.getSessionData().setLocale( locale );
				
			if ( bundle != null ) {
				
				refreshCaptions();
//					getUI().setCaption( app.getSessionData().getBundle().getString( "mainWindow.title" ));
					
			}
				
				
				
		});

		
		
		
		rememberMeButton = new CheckBox();
		okButton = new Button();
		
		VerticalLayout vl = new VerticalLayout();
		
		vl.addComponent( user );
		vl.addComponent( pwd );
		vl.addComponent( languageSelect );
		vl.addComponent( rememberMeButton );
		vl.addComponent( okButton );
		
		// Set the size as undefined at all levels
		vl.setSpacing( true );
		vl.setMargin( true );
		
		panel.setContent( vl );
		
	}
	
	public String getName() {
		return user.getValue().toString();
	}
	public void setName( String nameStr ) {
		if ( nameStr != null ) this.user.setValue( nameStr );
	}
	
	public String getPwd() {
		return pwd.getValue().toString();
	}
	public void setPwd( String pwdStr ) {
		if ( pwdStr != null ) this.pwd.setValue( pwdStr );
	}
	
	public boolean toRemember() {
		return ( Boolean )rememberMeButton.getValue();
	}
	public void setRemember( boolean toRemember ) {
		this.rememberMeButton.setValue( toRemember );
	}
	
	public String getLanguage() {
		return this.languageSelect.getValue().toString();
	}
	
	public void setLanguage( String langStr ) {
		if ( langStr != null ) {
			
			languageSelect
			
			
			
			for ( Lang l : Lang.getAvailableLocales()) {
				if ( langStr.equals( l.toString())) {
					languageSelect.setValue( l );
					break;
				}
			}
		} else {
			languageSelect.setValue( Lang.DEFAULT_LOCALE );
		}
//		refreshCaptions();
	}
	
	public void invalid() {
		
		// Show message
		Notification.show( 
				app.getResourceStr( "login.errors.credentials.header" ),
				app.getResourceStr( "login.errors.credentials.body" ),
				Notification.Type.WARNING_MESSAGE
		);		
		// Clear password
		pwd.setValue( "" );
		// Select name and set focus
		user.selectAll();
	}

	private void refreshCaptions() {
		getCompositionRoot().setCaption( app.getResourceStr( "login.caption" ));

		user.setCaption( app.getResourceStr( "login.username" ));
		pwd.setCaption( app.getResourceStr( "login.password" ));
		languageSelect.setCaption( app.getResourceStr( "login.language" ));
		rememberMeButton.setCaption( app.getResourceStr( "login.rememberme" ));
		okButton.setCaption( app.getResourceStr( "login.login" ));

		languageSelect.setItemCaption( Lang.LOCALE_EN, app.getResourceStr( "language.en" ));
		languageSelect.setItemCaption( Lang.LOCALE_FI, app.getResourceStr( "language.fi" ));
		languageSelect.setItemCaption( Lang.LOCALE_ET, app.getResourceStr( "language.es" ));
		languageSelect.setItemCaption( Lang.LOCALE_RU, app.getResourceStr( "language.ru" ));
		
		languageSelect.setValue( app.getSessionData().getLocale());
//		languageSelect.requestRepaint();
	
	}

	// For testing to generate 'Login' button click event
	public void click() {
		
	}


	ItemCaptionGenerator<Locale> itemCaptionGenerator = new ItemCaptionGenerator(){

		@Override
		public String apply(Object item) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	



}
