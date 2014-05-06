package edu.csupomona.classmate;

public interface Constants {
	long NO_USER = 0;

	String PHP_BASE_ADDRESS			= "http://www.lol-fc.com/classmate/2/";

	String PHP_ADDRESS_LOGIN		= "login.php";
	String PHP_ADDRESS_GETFRIENDS		= "getfriends.php";
	String PHP_ADDRESS_GETREQUESTS	= "getrequests.php";
	String PHP_ADDRESS_ACCEPTFRIEND	= "acceptfriend.php";
	String PHP_ADDRESS_DISMISSFRIEND	= "dismissfriend.php";
	String PHP_ADDRESS_REMOVEFRIEND	= "removefriend.php";
	String PHP_ADDRESS_CREATEGROUP	= "creategroup.php";
	String PHP_ADDRESS_GETGROUPS		= "getgroups.php";
	String PHP_ADDRESS_REMOVEGROUP	= "removegroup.php";
	String PHP_ADDRESS_GETGROUP		= "getpeopleingroup.php";
	String PHP_ADDRESS_EMAILGROUP		= "emailgroup.php";
	String PHP_ADDRESS_SEARCHUSERS	= "searchfriends.php";
	String PHP_ADDRESS_JOINGROUP		= "addtogroup.php";

	String PHP_ADDRESS_UPLOADS		= "http://www.lol-fc.com/classmate/uploads/";
	String[] AVATAR_EXTENSIONS		= { "jpg", "jpeg", "png" };
	int AVATAR_DEFAULT_RESID		= R.drawable.ic_action_person;

	String PHP_PARAM_DEVICEID		= "device_id";

	String PHP_PARAM_EMAIL			= "email";
	String PHP_PARAM_FRIENDEMAIL		= "femail";
	String PHP_PARAM_FRIENDID		= "friend_id";
	String PHP_PARAM_FRIENDUSERNAME	= "fusername";
	String PHP_PARAM_GROUPID		= "group_id";
	String PHP_PARAM_GROUPTITLE		= "title";
	String PHP_PARAM_MESSAGE		= "message";
	String PHP_PARAM_NAME			= "username";
	String PHP_PARAM_PASSWORD		= "password";
	String PHP_PARAM_SEARCH			= "search";
	String PHP_PARAM_SUBJECT		= "subject";
	String PHP_PARAM_USERID			= "user_id";
	String PHP_PARAM_VERSION		= "version";

	String PREFS_WHICH			= "loginActivityPreferences";
	String PREFS_KEY_EMAIL			= PHP_PARAM_EMAIL;
	String PREFS_KEY_AUTOLOGIN		= "autologin";

	String INTENT_KEY_AUTOLOGIN		= PREFS_KEY_AUTOLOGIN;
	String INTENT_KEY_GROUP			= "group";
	String INTENT_KEY_EMAIL			= PREFS_KEY_EMAIL;
	//String INTENT_KEY_NAME		= PHP_PARAM_NAME;
	String INTENT_KEY_USER			= "user";
	//String INTENT_KEY_USERID		= PHP_PARAM_USERID;

	int CODE_REGISTER		= 0x0001;
	int CODE_RECOVER		= 0x0002;
	int CODE_MAIN		= 0x0003;
	int CODE_MANAGEGROUP	= 0x0004;
	int CODE_ADDMEMBER	= 0x0005;
}
