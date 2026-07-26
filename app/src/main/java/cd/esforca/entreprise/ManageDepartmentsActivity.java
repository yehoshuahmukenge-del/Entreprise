package cd.esforca.entreprise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import cd.esforca.entreprise.adapter.DepartmentAdapter;
import cd.esforca.entreprise.model.Department;
import cd.esforca.entreprise.viewmodel.EntrepriseViewModel;

public class ManageDepartmentsActivity extends AppCompatActivity {

    private EntrepriseViewModel viewModel;
    private DepartmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_departments);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDepartments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new DepartmentAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(EntrepriseViewModel.class);
        viewModel.departments.observe(this, departments -> {
            adapter.setDepartments(departments);
        });

        ExtendedFloatingActionButton buttonAddDepartment = findViewById(R.id.fabAddDepartment);
        buttonAddDepartment.setOnClickListener(v -> showAddDepartmentDialog());

        adapter.setOnDepartmentActionListener(new DepartmentAdapter.OnDepartmentActionListener() {
            @Override
            public void onEdit(Department department) {
                if (department.getId() == 0) return;
                showEditDepartmentDialog(department);
            }

            @Override
            public void onDelete(Department department) {
                if (department.getId() == 0) return;
                new AlertDialog.Builder(ManageDepartmentsActivity.this)
                        .setTitle("Supprimer le département")
                        .setMessage("Voulez-vous vraiment supprimer " + department.getLibelle() + " ? Cela supprimera également tous les agents de ce département.")
                        .setPositiveButton("Supprimer", (dialog, which) -> {
                            viewModel.deleteDepartment(department);
                            Toast.makeText(ManageDepartmentsActivity.this, "Département supprimé", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            }

            @Override
            public void onClick(Department department) {
                if (department.getId() == 0) return;
                Intent intent = new Intent(ManageDepartmentsActivity.this, MainActivity.class);
                intent.putExtra("DEPARTMENT_ID", department.getId());
                intent.putExtra("DEPARTMENT_NAME", department.getLibelle());
                startActivity(intent);
            }

            @Override
            public void onAddAgent(Department department) {
                if (department.getId() == 0) return;
                Intent intent = new Intent(ManageDepartmentsActivity.this, MainActivity.class);
                intent.putExtra("OPEN_ADD_DIALOG", true);
                intent.putExtra("DEFAULT_DEPARTMENT_ID", department.getId());
                startActivity(intent);
            }
        });
    }

    private void showAddDepartmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_department, null);
        builder.setView(dialogView);

        TextInputEditText editTextLibelle = dialogView.findViewById(R.id.editTextDepartmentLibelle);
        TextInputEditText editTextCode = dialogView.findViewById(R.id.editTextDepartmentCode);

        builder.setTitle("Nouveau Département")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String libelle = editTextLibelle.getText().toString().trim();
                    String code = editTextCode.getText().toString().trim();
                    if (!libelle.isEmpty() && !code.isEmpty()) {
                        viewModel.addDepartment(libelle, code);
                    } else {
                        Toast.makeText(this, "Le libellé et le code sont requis", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private void showEditDepartmentDialog(Department department) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_department, null);
        builder.setView(dialogView);

        TextInputEditText editTextLibelle = dialogView.findViewById(R.id.editTextDepartmentLibelle);
        TextInputEditText editTextCode = dialogView.findViewById(R.id.editTextDepartmentCode);

        editTextLibelle.setText(department.getLibelle());
        editTextCode.setText(department.getCode());

        builder.setTitle("Modifier le Département")
                .setPositiveButton("Enregistrer", (dialog, id) -> {
                    String libelle = editTextLibelle.getText().toString().trim();
                    String code = editTextCode.getText().toString().trim();
                    if (!libelle.isEmpty() && !code.isEmpty()) {
                        department.setLibelle(libelle);
                        department.setCode(code);
                        viewModel.updateDepartment(department);
                    } else {
                        Toast.makeText(this, "Le libellé et le code sont requis", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }
}
