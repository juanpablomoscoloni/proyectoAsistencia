package clases;

public class Cursada {

    private String id;
    private String anio;
    private String faltasMaximas;
    private Comision comision;
    private Asignatura asignatura;
    private Docente docente;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getFaltasMaximas() {
        return faltasMaximas;
    }

    public void setFaltasMaximas(String faltasMaximas) {
        this.faltasMaximas = faltasMaximas;
    }

    public Comision getComision() {
        return comision;
    }

    public void setComision(Comision comision) {
        this.comision = comision;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
