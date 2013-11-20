package edu.csupomona.cs.cs356.classmate;

public interface Constants {
	int NO_USER = 0;

	String PHP_BASE_ADDRESS = "http://www.lol-fc.com/classmate/";

	String PHP_PARAM_USERID		= "user_id";
	String PHP_PARAM_NAME		= "username";
	String PHP_PARAM_EMAIL		= "email";
	String PHP_PARAM_PASSWORD	= "password";
	String PHP_PARAM_DEVICEID	= "device_id";

	String PREFS_WHICH		= "loginActivityPreferences";
	String PREFS_KEY_EMAIL		= PHP_PARAM_EMAIL;
	String PREFS_KEY_AUTOLOGIN	= "autologin";

	String INTENT_KEY_USERID	= PHP_PARAM_USERID;
	String INTENT_KEY_NAME		= PHP_PARAM_NAME;
	String INTENT_KEY_EMAIL		= PREFS_KEY_EMAIL;
	String INTENT_KEY_AUTOLOGIN	= PREFS_KEY_AUTOLOGIN;

	int CODE_REGISTER	= 0x0001;
	int CODE_RECOVER	= 0x0002;
	int CODE_LOGIN	= 0x0003;
}
