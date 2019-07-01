<?php
require 'rb.php';
R::setup( 'mysql:host=localhost;dbname=asistencia', 'root', '' );

$IDCLASE = $_POST['idDiaClase'];

$asistencias = R::findMulti( 'asistencia,inscripcion,alumno', '
        SELECT alumno.*, asistencia.*, inscripcion.* FROM asistencia
        INNER JOIN diaclase ON diaclase.id = asistencia.iddiaclase
        INNER JOIN inscripcion ON inscripcion.id = asistencia.idinscripcion
        INNER JOIN alumno ON alumno.id = inscripcion.idalumno
        WHERE asistencia.iddiaclase = ?' , [$IDCLASE] );

if( !empty($asistencias['asistencia']) ){
	
	echo json_encode( array( "Datos" => array($asistencias) ) );
	
} else {
	
	echo json_encode( array( "Datos" => array() ) );
	
}

?>