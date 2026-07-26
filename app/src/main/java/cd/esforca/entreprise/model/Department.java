package cd.esforca.entreprise.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "departments")
public class Department {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String libelle;
    private String code;

    public Department(int id, String libelle, String code) {
        this.id = id;
        this.libelle = libelle;
        this.code = code;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() { return libelle; }
}