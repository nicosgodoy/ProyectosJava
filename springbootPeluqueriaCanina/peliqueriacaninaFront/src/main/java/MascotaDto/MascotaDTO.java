
package MascotaDto;


public class MascotaDTO {
    
    private int id;
    private String nombreMascota;
    private String raza;
    private String color;
    private String observaciones;
    private String nombreDuenio;
    private String celDuenio;
    private String alergico;
    private String atencionEspecial;

    public MascotaDTO() {
    }

    public MascotaDTO(int id, String nombreMascota, String raza, String color, String observaciones, String nombreDuenio, String celDuenio, String alergico, String atencionEspecial) {
        this.id = id;
        this.nombreMascota = nombreMascota;
        this.raza = raza;
        this.color = color;
        this.observaciones = observaciones;
        this.nombreDuenio = nombreDuenio;
        this.celDuenio = celDuenio;
        this.alergico = alergico;
        this.atencionEspecial = atencionEspecial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreDuenio() {
        return nombreDuenio;
    }

    public void setNombreDuenio(String nombreDuenio) {
        this.nombreDuenio = nombreDuenio;
    }

    public String getCelDuenio() {
        return celDuenio;
    }

    public void setCelDuenio(String celDuenio) {
        this.celDuenio = celDuenio;
    }

    public String getAlergico() {
        return alergico;
    }

    public void setAlergico(String alergico) {
        this.alergico = alergico;
    }

    public String getAtencionEspecial() {
        return atencionEspecial;
    }

    public void setAtencionEspecial(String atencionEspecial) {
        this.atencionEspecial = atencionEspecial;
    }
    
    
}
