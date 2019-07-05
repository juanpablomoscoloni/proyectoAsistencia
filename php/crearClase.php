<?php
require 'rb.php';
R::setup( 'mysql:host=localhost;dbname=asistencia', 'root', '' );

$IDCURSADA = $_POST['idCursada'];
$IDDOCENTE = $_POST['idDocente'];
$TEMA = $_POST['tema'];
$TOKEN = $_POST['tkn'];

$inscriptos = R::find('inscripcion', ' idCursada = ? ', [$IDCURSADA]);

$cantidad = sizeof($inscriptos);

//Si se encontraron inscriptos
if (sizeof($inscriptos)>0){
	
	$DiaClase = R::dispense('diaclase');
	$DiaClase->iddocente = (int)$IDDOCENTE;
	$DiaClase->idcursada = (int)$IDCURSADA;
	$DiaClase->tema = $TEMA;
	$DiaClase->token = $TOKEN;
	$DiaClase->estadotoken = 1;
	$id = R::store($DiaClase);

for ($i=0; $i < $cantidad ; $i++) { 

	$idAlu= (array_values($inscriptos)[$i]['id']);
	
	$asistencia=R::dispense('asistencia');
	$asistencia->estado = 0 ;
	$asistencia->idinscripcion = (int)$idAlu;
	$asistencia->iddiaclase = (int)$id;
	$listado = R::store($asistencia);
	
}

$alumnos = R::findMulti( 'asistencia,inscripcion,alumno,diaclase', '
        SELECT alumno.*, asistencia.*, inscripcion.*, diaclase.* FROM asistencia
        INNER JOIN diaclase ON diaclase.id = asistencia.iddiaclase
        INNER JOIN inscripcion ON inscripcion.id = asistencia.idinscripcion
        INNER JOIN alumno ON alumno.id = inscripcion.idalumno
        WHERE diaclase.id = ?' , [$id] );
}




if( !empty($alumnos) ){
	
	echo json_encode(array("Datos"=> array($alumnos)));
	
} else {
	
	echo json_encode(array("Datos"=> array()));
	
}
   
?>