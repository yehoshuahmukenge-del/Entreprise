package cd.esforca.entreprise.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cd.esforca.entreprise.R;
import cd.esforca.entreprise.model.Department;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {
    private List<Department> departments = new ArrayList<>();
    private OnDepartmentActionListener actionListener;

    public interface OnDepartmentActionListener {
        void onEdit(Department department);
        void onDelete(Department department);
        void onClick(Department department);
        void onAddAgent(Department department);
    }

    public void setOnDepartmentActionListener(OnDepartmentActionListener listener) {
        this.actionListener = listener;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        Department department = departments.get(position);
        holder.nameTextView.setText(department.getLibelle());

        holder.itemView.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onClick(department);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onEdit(department);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onDelete(department);
        });

        holder.btnAddAgent.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onAddAgent(department);
        });
    }

    @Override
    public int getItemCount() { return departments.size(); }

    static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageButton btnEdit, btnDelete, btnAddAgent;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.departmentName);
            btnEdit = itemView.findViewById(R.id.btnEditDepartment);
            btnDelete = itemView.findViewById(R.id.btnDeleteDepartment);
            btnAddAgent = itemView.findViewById(R.id.btnAddAgentToDept);
        }
    }
}