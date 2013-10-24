<?php
$connect_error = 'Sorry. We are experiencing connection problems.';
mysql_connect('50.63.235.43','classmatedb','XXXXXXXX') or die($connect_error);
// 50.63.235.43 
mysql_select_db('classmatedb');

function sanitize($data)
{
	return mysql_real_escape_string($data);
}

function get_password($email)
{
	$sql = "select `password` from `users` where `email`='$email'";
	$query = mysql_query($sql);
	$row = mysql_fetch_row($query);
	return $row[0];
}

function get_userid($email)
{
	$sql = "select `user_id` from `users` where `email`='$email'";
	$query = mysql_query($sql);
	$row = mysql_fetch_row($query);
	return $row[0];
}

function get_device_id($email)
{
	$sql = "select `device_id` from `users` where `email`='$email'";
	$query = mysql_query($sql);
	$row = mysql_fetch_row($query);
	return $row[0];
}


function email_exists($email)
{
	$email = sanitize($email);
	$query = mysql_query("select COUNT(`user_id`) from `users` where `email` = '$email'");

	return (mysql_result($query,0)==1) ? true : false;
}


if(!empty($_GET['email']) && !empty($_GET['password']) && !empty($_GET['device_id']) )
      {
	$email = sanitize($_GET['email']);
	$device_id = sanitize($_GET['device_id']);

        if(email_exists($email))
	{
		if(get_password($email)==$_GET['password'])
		{
			mysql_query("update `users` set `device_id`='$device_id' where `email`='$email'");
			print(get_userid($email));
		}
		else print(0);
	}else
		print(0);//no known email
      }
else
if(!empty($_GET['email']) && !empty($_GET['device_id']))
{
		$email = sanitize($_GET['email']);
		$device_id = sanitize($_GET['device_id']);
		
       	if(email_exists($email) && ($device_id==get_device_id($email)))
		{		
			print(get_userid($email));
		}
		else print(0);
}
else print(0);

mysql_close();
?>