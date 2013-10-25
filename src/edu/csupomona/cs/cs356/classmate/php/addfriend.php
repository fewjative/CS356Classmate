<?php
$connect_error = 'Sorry. We are experiencing connection problems.';
mysql_connect('50.63.235.43','classmatedb','XXXXXXXXXXX') or die($connect_error);
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

function alreadyadded($email,$friend)
{
	$query = mysql_query("select COUNT(`user_id`) from `friendlist` where `email` = '$email' and `femail`='$friend'");

	return (mysql_result($query,0)==1) ? true : false;
}


function get_userid($email)
{
	$sql = "select `user_id` from `users` where `email`='$email'";
	$query = mysql_query($sql);
	$row = mysql_fetch_row($query);
	return $row[0];
}


if(!empty($_GET['email']) && !empty($_GET['friend']))
 {
	$email = sanitize($_GET['email']);
	$friend = sanitize($_GET['friend']);

        if(email_exists($email) && email_exists($friend) && alreadyadded($email,$friend))
	{
		print(-1);
	}
	else if(email_exists($friend))
	{
	
	$user_id = get_userid($email);
	$friend_id = get_userid($friend);

	$sql = "INSERT into friendlist(user_id,email,friend_id,femail,iignore,added,request) VALUES($user_id,'$email',$friend_id,'$friend',0,0,0)";
		if(mysql_query($sql))
		{
			print(2);
		}
		else
		{
		//print(mysql_error());
		print(-2);
		}
	}
	else
	print(0);
}
mysql_close();
?>