package cd.esforca.entreprise.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "agents",
        foreignKeys = @ForeignKey(
                entity = Department.class,
                parentColumns = "id",
                childColumns = "departmentId",
                onDelete = ForeignKey.CASCADE),
        indices = {@androidx.room.Index("departmentId")})
public class Agent {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String prenom;
    private String poste; // Correction confirmée
    private String sexe;
    private String telephone;
    private int departmentId;
    private String imageUrl;

    public Agent(int id, String nom, String prenom, String poste, String sexe, String telephone, int departmentId, String imageUrl) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.sexe = sexe;
        this.telephone = telephone;
        this.departmentId = departmentId;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}