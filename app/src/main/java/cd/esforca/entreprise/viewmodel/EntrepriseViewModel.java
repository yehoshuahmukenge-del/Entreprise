package cd.esforca.entreprise.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cd.esforca.entreprise.model.Department;
import cd.esforca.entreprise.model.Agent;
import cd.esforca.entreprise.repository.EntrepriseRepository;
import cd.esforca.entreprise.utils.ImageUtils;

public class EntrepriseViewModel extends AndroidViewModel {
    private final EntrepriseRepository repository;

    public final LiveData<List<Department>> departments;
    public final LiveData<List<Agent>> agents;

    private final MutableLiveData<Integer> filterDepartmentId = new MutableLiveData<>(0);
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MediatorLiveData<List<Agent>> filteredAgents = new MediatorLiveData<>();

    public EntrepriseViewModel(@NonNull Application application) {
        super(application);
        this.repository = new EntrepriseRepository(application);
        this.agents = filteredAgents;

        departments = Transformations.map(repository.getAllDepartments(), list -> {
            List<Department> departmentList = new ArrayList<>();
            departmentList.add(new Department(0, "Tous les départements", "ALL"));
            if (list != null) departmentList.addAll(list);
            return departmentList;
        });

        LiveData<List<Agent>> allAgents = repository.getAllAgents();
        filteredAgents.addSource(allAgents, p -> applyFilters(p, filterDepartmentId.getValue(), searchQuery.getValue()));
        filteredAgents.addSource(filterDepartmentId, id -> applyFilters(allAgents.getValue(), id, searchQuery.getValue()));
        filteredAgents.addSource(searchQuery, q -> applyFilters(allAgents.getValue(), filterDepartmentId.getValue(), q));
    }

    private void applyFilters(List<Agent> all, Integer deptId, String query) {
        if (all == null) return;
        List<Agent> filtered = all.stream()
                .filter(a -> deptId == 0 || a.getDepartmentId() == deptId)
                .filter(a -> query.isEmpty() || 
                        a.getNom().toLowerCase().contains(query.toLowerCase()) || 
                        a.getPrenom().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        filteredAgents.setValue(filtered);
    }

    public void filterAgents(int id) { filterDepartmentId.setValue(id); }
    public void setSearchQuery(String q) { searchQuery.setValue(q); }

    public void addDepartment(String libelle, String code) {
        repository.addDepartment(new Department(0, libelle, code), null);
    }

    public void updateDepartment(Department department) {
        repository.updateDepartment(department, null);
    }

    public void deleteDepartment(Department department) {
        repository.deleteDepartment(department, null);
    }

    public void addAgent(String nom, String prenom, String poste, String sexe, String telephone, int departmentId, String imageUrl) {
        Agent newAgent = new Agent(0, nom, prenom, poste, sexe, telephone, departmentId, imageUrl);
        repository.addAgent(newAgent, null);
    }

    public void updateAgent(int id, String nom, String prenom, String poste, String sexe, String telephone, int departmentId, String imageUrl) {
        Agent updatedAgent = new Agent(id, nom, prenom, poste, sexe, telephone, departmentId, imageUrl);
        repository.updateAgent(updatedAgent, null);
    }

    public void deleteAgent(Agent agent) {
        if (agent.getImageUrl() != null) {
            ImageUtils.deleteImage(agent.getImageUrl());
        }
        repository.deleteAgent(agent, null);
    }
}