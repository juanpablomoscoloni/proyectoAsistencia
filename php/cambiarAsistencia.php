<?php
require 'rb.php';
R::setup( 'mysql:host=localhost;dbname=asistencia', 'root', '' );

$IDASISTENCIA = $_POST['id'];

$asistencia = R::load('asistencia', $IDASISTENCIA);

if ( !$asistencia['id'] == 0 ) {
	
	$asistencia->estado=1;
	$id = R::store($asistencia);
	
	echo json_encode( array( "Dato" => array($id) ) );
	
} else {
	
	echo json_encode( array( "Dato" => array() ) );
}

?>