package cd.esforca.entreprise.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cd.esforca.entreprise.db.AppDatabase;
import cd.esforca.entreprise.db.EntrepriseDao;
import cd.esforca.entreprise.model.Department;
import cd.esforca.entreprise.model.Agent;

public class EntrepriseRepository {
    private final EntrepriseDao entrepriseDao;
    private final ExecutorService executorService;

    public EntrepriseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        entrepriseDao = db.entrepriseDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Récupération
    public LiveData<List<Department>> getAllDepartments() {
        return entrepriseDao.getAllDepartments();
    }

    public LiveData<List<Agent>> getAllAgents() {
        return entrepriseDao.getAllAgents();
    }

    // Actions (Exécutées en arrière-plan)
    public void addDepartment(Department department, Runnable onComplete) {
        executorService.execute(() -> {
            entrepriseDao.insertDepartment(department);
            if (onComplete != null) onComplete.run();
        });
    }

    public void updateDepartment(Department department, Runnable onComplete) {
        executorService.execute(() -> {
            entrepriseDao.updateDepartment(department);
            if (onComplete != null) onComplete.run();
        });
    }

    public void deleteDepartment(Department department, Runnable onComplete) {
        executorService.execute(() -> {
            entrepriseDao.deleteDepartment(department);
            if (onComplete != null) onComplete.run();
        });
    }

    public void addAgent(Agent agent, Runnable onComplete) {
        executorService.execute(() -> {
            entrepriseDao.insertAgent(agent);
            if (onComplete != null) onComplete.run();
        });
    }

    public void updateAgent(Agent agent, Runnable onComplete) {
        executorService.execute(() -> {
            entrepriseDao.updateAgent(agent);
            if (onComplete != null) onComplete.run();
        });
    }

    public void deleteAgent(Agent agent, Runnable onComplete) {
        executorService.execute(() -> {
            entrepriseDao.deleteAgent(agent);
            if (onComplete != null) onComplete.run();
        });
    }
}