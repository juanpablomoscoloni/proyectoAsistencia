<?php
require 'rb.php';
R::setup( 'mysql:host=localhost;dbname=asistencia', 'root', '' );

$user = $_POST['user'];
$pass = $_POST['password'];

$user = R::findOne('cuenta', 'usuario = ? AND contrasena = ?', [$user,$pass]);

if( !empty($user) ){
	
	echo json_encode( array( "Usuario" => array($user) ) );
	
} else {
	
	echo json_encode( array( "Usuario" => array() ) );
	
}

?>