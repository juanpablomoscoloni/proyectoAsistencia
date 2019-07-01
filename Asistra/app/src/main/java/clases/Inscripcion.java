package clases;

public class Inscripcion {

    private String id;
    private String idAlumno;
    private String idCursada;
    private String inasistencias;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInasistencias() {
        return inasistencias;
    }

    public void setInasistencias(String inasistencias) {
        this.inasistencias = inasistencias;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getIdCursada() {
        return idCursada;
    }

    public void setIdCursada(String idCursada) {
        this.idCursada = idCursada;
    }
}
