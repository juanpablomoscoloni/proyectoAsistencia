package clases;

public class Asistencia {

    private String id;
    private String idInscripcion;
    private String idAlumno;
    private String idDiaClase;
    private String nombreAlumno;
    private String apellidoAlumno;
    private String estado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(String idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidoAlumno() {
        return apellidoAlumno;
    }

    public void setApellidoAlumno(String apellidoAlumno) {
        this.apellidoAlumno = apellidoAlumno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdDiaClase() {
        return idDiaClase;
    }

    public void setIdDiaClase(String idDiaClase) {
        this.idDiaClase = idDiaClase;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }
}
