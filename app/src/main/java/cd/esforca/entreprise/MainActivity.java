package cd.esforca.entreprise;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cd.esforca.entreprise.adapter.AgentAdapter;
import cd.esforca.entreprise.model.Agent;
import cd.esforca.entreprise.model.Department;
import cd.esforca.entreprise.utils.ImageUtils;
import cd.esforca.entreprise.viewmodel.EntrepriseViewModel;

public class MainActivity extends AppCompatActivity {

    private EntrepriseViewModel viewModel;
    private AgentAdapter adapter;
    private Spinner departmentSpinner;
    private Uri selectedImageUri;
    private ImageView dialogImageView;

    // Gestion moderne de la sélection d'image
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    if (dialogImageView != null) dialogImageView.setImageURI(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuration de l'interface
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        departmentSpinner = findViewById(R.id.departmentSpinner);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAgents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AgentAdapter();
        recyclerView.setAdapter(adapter);

        // Écouteurs pour les clics sur les items de la liste
        adapter.setOnAgentClickListener(this::showAgentDialog);
        adapter.setOnAgentLongClickListener(this::showDeleteConfirmationDialog);

        // Initialisation du ViewModel
        viewModel = new ViewModelProvider(this).get(EntrepriseViewModel.class);

        // Observation des départements pour le Spinner de filtrage et pour l'adapter
        viewModel.departments.observe(this, departments -> {
            ArrayAdapter<Department> spinnerAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, departments);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            departmentSpinner.setAdapter(spinnerAdapter);
            
            // On passe aussi les départements à l'adapter pour afficher les noms dans les items
            adapter.setDepartments(departments);
        });

        // Filtrage automatique quand on change de département
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Department selectedDept = (Department) parent.getItemAtPosition(position);
                viewModel.filterAgents(selectedDept.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Mise à jour de la liste quand les agents changent
        viewModel.agents.observe(this, agents -> {
            adapter.setAgents(agents);
        });

        // Gérer le département passé en Intent (venant de ManageDepartmentsActivity)
        int initialDeptId = getIntent().getIntExtra("DEPARTMENT_ID", -1);
        boolean openAddDialog = getIntent().getBooleanExtra("OPEN_ADD_DIALOG", false);

        if (initialDeptId != -1) {
            viewModel.departments.observe(this, departments -> {
                for (int i = 0; i < departments.size(); i++) {
                    if (departments.get(i).getId() == initialDeptId) {
                        departmentSpinner.setSelection(i);
                        
                        // Si demandé, on ouvre le dialogue d'ajout après un court délai pour laisser l'UI s'initialiser
                        if (openAddDialog) {
                            getIntent().removeExtra("OPEN_ADD_DIALOG"); // Éviter de rouvrir en cas de recréation
                            findViewById(R.id.fabAddAgent).postDelayed(() -> showAgentDialog(null), 200);
                        }
                        break;
                    }
                }
            });
        }

        findViewById(R.id.fabAddAgent).setOnClickListener(v -> showAgentDialog(null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Rechercher un agent...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_manage_departments) {
            startActivity(new Intent(this, ManageDepartmentsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAgentDialog(Agent agentToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_agent, null);
        builder.setView(dialogView);

        TextInputEditText etNom = dialogView.findViewById(R.id.editTextAgentNom);
        TextInputEditText etPrenom = dialogView.findViewById(R.id.editTextAgentPrenom);
        TextInputEditText etPoste = dialogView.findViewById(R.id.editTextAgentPoste);
        TextInputEditText etSexe = dialogView.findViewById(R.id.editTextAgentSexe);
        TextInputEditText etPhone = dialogView.findViewById(R.id.editTextAgentTelephone);
        Spinner spinnerDept = dialogView.findViewById(R.id.spinnerAgentDepartment);
        dialogImageView = dialogView.findViewById(R.id.imageViewAgentPreview);

        dialogView.findViewById(R.id.buttonSelectImage).setOnClickListener(v -> mGetContent.launch("image/*"));

        // Remplissage du Spinner interne au dialogue
        List<Department> realDepts = new ArrayList<>();
        if (viewModel.departments.getValue() != null) {
            for (Department d : viewModel.departments.getValue()) if (d.getId() != 0) realDepts.add(d);
        }
        ArrayAdapter<Department> deptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, realDepts);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept.setAdapter(deptAdapter);

        if (agentToEdit != null) {
            builder.setTitle("Modifier l'agent");
            etNom.setText(agentToEdit.getNom());
            etPrenom.setText(agentToEdit.getPrenom());
            etPoste.setText(agentToEdit.getPoste());
            etSexe.setText(agentToEdit.getSexe());
            etPhone.setText(agentToEdit.getTelephone());

            for (int i = 0; i < realDepts.size(); i++) {
                if (realDepts.get(i).getId() == agentToEdit.getDepartmentId()) {
                    spinnerDept.setSelection(i);
                    break;
                }
            }

            if (agentToEdit.getImageUrl() != null) {
                Glide.with(this).load(new File(agentToEdit.getImageUrl())).into(dialogImageView);
            }
        } else {
            builder.setTitle("Ajouter un agent");
            selectedImageUri = null;
        }

        builder.setPositiveButton(agentToEdit == null ? "Ajouter" : "Modifier", (dialog, which) -> {
            String nom = etNom.getText().toString();
            String prenom = etPrenom.getText().toString();
            String poste = etPoste.getText().toString();
            String sexe = etSexe.getText().toString();
            String phone = etPhone.getText().toString();
            Department selectedDept = (Department) spinnerDept.getSelectedItem();

            if (!nom.isEmpty() && selectedDept != null) {
                String imagePath = (agentToEdit != null) ? agentToEdit.getImageUrl() : null;
                if (selectedImageUri != null) {
                    if (imagePath != null) ImageUtils.deleteImage(imagePath);
                    imagePath = ImageUtils.saveImageToInternalStorage(this, selectedImageUri);
                }

                if (agentToEdit == null) viewModel.addAgent(nom, prenom, poste, sexe, phone, selectedDept.getId(), imagePath);
                else viewModel.updateAgent(agentToEdit.getId(), nom, prenom, poste, sexe, phone, selectedDept.getId(), imagePath);
            }
        });
        builder.show();
    }

    private void showDeleteConfirmationDialog(Agent agent) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Voulez-vous supprimer cet agent ?")
                .setPositiveButton("Oui", (d, w) -> viewModel.deleteAgent(agent))
                .setNegativeButton("Non", null)
                .show();
    }
}