<?php
require 'rb.php';
R::setup( 'mysql:host=localhost;dbname=asistencia', 'root', '' );

$id = $_POST['id'];

//$docente = R::findOne( 'docente', ' id = ? ', [$id]);	

$docente = R::findMulti( 'docente,cursada,comision,asignatura', '
        SELECT docente.*, cursada.*, comision.*, asignatura.* FROM docente
        INNER JOIN cursada ON cursada.idDocente = docente.id
		INNER JOIN comision ON comision.id = cursada.idComision
		INNER JOIN asignatura ON asignatura.id = cursada.idAsignatura
        WHERE docente.id = ?
    ', [ $id] );	

if( !empty($docente) ){
	
	echo json_encode( array( "Datos" => array($docente) ) );
	
} else {
	
	echo json_encode( array( "Datos" => array() ) );
	
}

?>