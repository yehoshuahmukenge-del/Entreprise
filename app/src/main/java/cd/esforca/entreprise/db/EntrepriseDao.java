package cd.esforca.entreprise.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cd.esforca.entreprise.model.Department;
import cd.esforca.entreprise.model.Agent;

@Dao
public interface EntrepriseDao {
    // Opérations sur les départements
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDepartment(Department department);

    @Update
    void updateDepartment(Department department);

    @Query("SELECT * FROM departments")
    LiveData<List<Department>> getAllDepartments();

    @Delete
    void deleteDepartment(Department department);

    // Opérations sur les agents
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAgent(Agent agent);

    @Update
    void updateAgent(Agent agent);

    @Query("SELECT * FROM agents")
    LiveData<List<Agent>> getAllAgents();

    @Delete
    void deleteAgent(Agent agent);
}