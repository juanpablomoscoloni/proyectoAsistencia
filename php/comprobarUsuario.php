<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Allow: GET, POST, OPTIONS, PUT, DELETE");

require 'rb.php';
R::setup( 'mysql:host=localhost;dbname=asistencia', 'root', '' );

$user = $_POST['user'];
$pass = $_POST['password'];

$user = R::findOne('cuenta', 'usuario = ? AND contrasena = ?', [$user,$pass]);

if( !empty($user) ){
	
	echo json_encode( $user );
	
} else {
	
	echo json_encode( array( ) );
	
}

?>