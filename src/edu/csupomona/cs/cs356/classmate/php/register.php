<?php
$connect_error = 'Sorry. We are experiencing connection problems.';
mysql_connect('50.63.235.43','classmatedb','XXXXXXXX') or die($connect_error);
// 50.63.235.43 
mysql_select_db('classmatedb');

function sanitize($data)
{
	return mysql_real_escape_string($data);
}

function email_exists($email)
{
	$email = sanitize($email);
	$query = mysql_query("select COUNT(`user_id`) from `users` where `email` = '$email'");

	return (mysql_result($query,0)==1) ? true : false;
}

function get_userid($email)
{
	$sql = "select `user_id` from `users` where `email`='$email'";
	$query = mysql_query($sql);
	$row = mysql_fetch_row($query);
	return $row[0];
}


if(!empty($_GET['email']) && !empty($_GET['password']) && !empty($_GET['device_id']))
 {
	$email = sanitize($_GET['email']);
	$password = sanitize($_GET['password']);
	$device_id = sanitize($_GET['device_id']);


        if(email_exists($email))
	{
		print(0);
	}
		else
	{
	$sql = "INSERT into users(email,password,device_id) VALUES('$email','$password','$device_id')";
		if(mysql_query($sql))
		{
			print(get_userid($email));
		}
		else
		{
		//print(mysql_error());
		print(0);
		}
	}
}
mysql_close();
?>