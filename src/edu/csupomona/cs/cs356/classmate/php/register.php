<?php
$connect_error = 'Sorry. We are experiencing connection problems.';
mysql_connect('68.178.216.98','classmatedb','XXXXXXXX') or die($connect_error);
// 50.63.235.43 
mysql_select_db('classmatedb') or die($connect_error);
?>