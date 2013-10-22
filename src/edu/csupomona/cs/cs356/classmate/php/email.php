<?php
$connect_error = 'Sorry. We are experiencing connection problems.';
mysql_connect('50.63.235.43','classmatedb','XXXXXXXXXX') or die($connect_error);
// 50.63.235.43 
mysql_select_db('classmatedb');

function email($to,$subject,$body) {
	mail($to,$subject,$body,'From: calpolyclassmate@lgmail.com');
}

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

function email_exists($email)
{
	$email = sanitize($email);
	$query = mysql_query("select COUNT(`user_id`) from `users` where `email` = '$email'");

	return (mysql_result($query,0)==1) ? true : false;
}

if(!empty($_GET['email']))
      {
	$email = sanitize($_GET['email']);
        if(email_exists($email))
	{
		email($email,'Your password for Classmate App','Your password is: ' . get_password($email));
		print(2);
	}else
		print(1);//no known email
      }
	  else
		print(0);//no get
mysql_close();
?>