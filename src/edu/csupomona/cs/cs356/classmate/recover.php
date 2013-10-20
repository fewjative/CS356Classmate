<?php
$connect_error = 'Sorry. We are experiencing connection problems.';
mysql_connect('50.63.235.43','classmatedb','XXXXXXXXX') or die($connect_error);
// 50.63.235.43 
mysql_select_db('classmatedb');
$sql="select * from `users` where `user_id`=1";

$query = mysql_query($sql);
$rows = mysql_fetch_row($query);

	if(!is_null($rows)  && !empty($rows))
	{
		print(json_encode($rows));
	}	

mysql_close();
?>