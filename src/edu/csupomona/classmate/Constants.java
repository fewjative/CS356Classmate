package edu.csupomona.classmate;

public interface Constants {
	int NO_USER = 0;

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
	String PHP_ADDRESS_REQUESTFRIEND	= "addfriend.php";
	String PHP_ADDRESS_SEARCHGROUPS	= "searchgroups.php";
	String PHP_ADDRESS_SEARCHEVENTS	= "searchevents.php";
	String PHP_ADDRESS_ADDEVENT		= "addevent.php";
	String PHP_ADDRESS_GETTERMS		= "getterms.php";
	String PHP_ADDRESS_GETMAJORS		= "getmajors.php";
	String PHP_ADDRESS_GETCOURSES		= "getclasses.php";
	String PHP_ADDRESS_ADDCLASS2		= "addclass2.php";
	String PHP_ADDRESS_REMOVECLASS2	= "removeclass2.php";
	String PHP_ADDRESS_GETBOOKLIST = "getbooklist.php";
	String PHP_ADDRESS_GETADMINBOOKLIST = "getadminbooklist.php";
	String PHP_ADDRESS_REMOVEBOOK = "removebook.php";
	String PHP_ADDRESS_GETACTIVITYFEED	= "getactivityfeed.php";
	String PHP_ADDRESS_GETNEWSFEED	= "getnewsfeed.php";

	String PHP_ADDRESS_UPLOADS		= "http://www.lol-fc.com/classmate/uploads/";
	String[] AVATAR_EXTENSIONS		= { "jpg", "jpeg", "png" };
	int AVATAR_DEFAULT_RESID		= R.drawable.ic_action_person;

	String PHP_PARAM_ACTIVITYTYPE	= "activity_type";
	String PHP_PARAM_CLASSNUM		= "class_num";
	String PHP_PARAM_DEVICEID		= "device_id";
	String PHP_PARAM_DISTINCT		= "distinct";
	String PHP_PARAM_EMAIL			= "email";
	String PHP_PARAM_FRIEND			= "friend";
	String PHP_PARAM_FRIENDEMAIL	= "femail";
	String PHP_PARAM_FRIENDID		= "friend_id";
	String PHP_PARAM_FRIENDUSERNAME	= "fusername";
	String PHP_PARAM_GROUPID		= "group_id";
	String PHP_PARAM_GROUPTITLE		= "title";
	String PHP_PARAM_EVENTID		= "event_id";
	String PHP_PARAM_MAJOR			= "major";
	String PHP_PARAM_MAJORLONG		= "major_long";
	String PHP_PARAM_MAJORSHORT		= "major_short";
	String PHP_PARAM_MESSAGE		= "message";
	String PHP_PARAM_NAME			= "username";
	String PHP_PARAM_PASSWORD		= "password";
	String PHP_PARAM_SEARCH			= "search";
	String PHP_PARAM_SUBJECT		= "subject";
	String PHP_PARAM_TERM			= "term";
	String PHP_PARAM_USERID			= "user_id";
	String PHP_PARAM_BOOKLISTID		= "booklist_id";
	String PHP_PARAM_BOOKTITLE 		= "title";
	String PHP_PARAM_BOOKCLASSNAME  = "classname";
	String PHP_PARAM_BOOKCONDITION  = "condition";
	String PHP_PARAM_BOOKPRICE 		= "price";
	String PHP_PARAM_VERSION		= "version";
	String PHP_PARAM_DATE			= "date";
	String PHP_PARAM_TITLE			= "title";
	String PHP_PARAM_DESC			= "desc";
	String PHP_PARAM_IMAGEURL		= "imageURL";
	String PHP_PARAM_ARTICLEURL		= "articleURL";

	String PHP_PARAM_CLASS_ID		= "class_id";
	String PHP_PARAM_CLASS_TITLE		= "title";
	String PHP_PARAM_CLASS_STARTTIME	= "time_start";
	String PHP_PARAM_CLASS_ENDTIME	= "time_end";
	String PHP_PARAM_CLASS_WEEKDAYS	= "weekdays";
	String PHP_PARAM_CLASS_STARTDATE	= "date_start";
	String PHP_PARAM_CLASS_ENDDATE	= "date_end";
	String PHP_PARAM_CLASS_INSTRUCTOR	= "instructor";
	String PHP_PARAM_CLASS_BUILDING	= "building";
	String PHP_PARAM_CLASS_ROOM		= "room";
	String PHP_PARAM_CLASS_SECTION	= "section";

	String PREFS_WHICH				= "loginActivityPreferences";
	String PREFS_KEY_EMAIL			= PHP_PARAM_EMAIL;
	String PREFS_KEY_AUTOLOGIN		= "autologin";

	String INTENT_KEY_AUTOLOGIN		= PREFS_KEY_AUTOLOGIN;
	String INTENT_KEY_GROUP			= "group";
	String INTENT_KEY_EMAIL			= PREFS_KEY_EMAIL;
	//String INTENT_KEY_NAME		= PHP_PARAM_NAME;
	String INTENT_KEY_SECTION 		= "section";
	String INTENT_KEY_SELECTEDITEMPOS	= "selectedItemPos";
	String INTENT_KEY_USER			= "user";
	//String INTENT_KEY_USERID		= PHP_PARAM_USERID;
	String INTENT_KEY_NEWSARTICLE	= "newsArticle";
	String INTENT_KEY_FBUSER 		= "fb_user";


	int CODE_REGISTER				= 0x0001;
	int CODE_RECOVER				= 0x0002;
	int CODE_MAIN					= 0x0003;
	int CODE_MANAGEGROUP			= 0x0004;
	int CODE_ADDMEMBER				= 0x0005;
	int CODE_ADDCLASS				= 0x0006;
	int CODE_VIEWSECTION			= 0x0007;
	int CODE_CAMERA_REQUEST 		= 0x048;
	int CODE_GALLERY_REQUEST 		= 0x058;
}
