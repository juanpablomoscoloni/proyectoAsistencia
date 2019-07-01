<?php
require 'rb.php';
R::setup( 'mysql:host=localhost;dbname=asistencia', 'root', '' );

$idAlumno = $_POST['id'];
$token = $_POST['token'];

$asistencia = R::findMulti( 'alumno,inscripcion,diaclase,asistencia,cursada,asignatura', '
        SELECT asistencia.*, inscripcion.*, diaclase.*, asignatura.* FROM inscripcion
        INNER JOIN alumno ON alumno.id = inscripcion.idalumno
        INNER JOIN asistencia ON asistencia.idinscripcion = inscripcion.id
        INNER JOIN diaclase ON diaclase.id = asistencia.iddiaclase
		INNER JOIN cursada ON cursada.id = diaclase.idcursada
		INNER JOIN asignatura ON asignatura.id = cursada.idasignatura
        WHERE alumno.id = ? AND diaclase.token = ? AND diaclase.estadotoken= ? AND asistencia.estado= ?' , [ $idAlumno,$token,1,0] );


if( !empty(array_values($asistencia)[2]) ){
	
	echo json_encode( array( "Dato" => array($asistencia) ) );
	
} else {
	
	echo json_encode( array( "Dato" => array() ) );
	
}

?>