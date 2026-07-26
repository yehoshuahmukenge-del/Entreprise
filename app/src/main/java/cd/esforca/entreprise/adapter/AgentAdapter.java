package cd.esforca.entreprise.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import cd.esforca.entreprise.R;
import cd.esforca.entreprise.model.Agent;
import cd.esforca.entreprise.model.Department;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {
    private List<Agent> agents = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();
    private OnAgentClickListener clickListener;
    private OnAgentLongClickListener longClickListener;

    public interface OnAgentClickListener { void onAgentClick(Agent agent); }
    public interface OnAgentLongClickListener { void onAgentLongClick(Agent agent); }

    public void setOnAgentClickListener(OnAgentClickListener l) { this.clickListener = l; }
    public void setOnAgentLongClickListener(OnAgentLongClickListener l) { this.longClickListener = l; }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
        notifyDataSetChanged();
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agent, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        Agent agent = agents.get(position);
        String fullDisplayName = agent.getNom() + " " + agent.getPrenom();
        holder.nameTextView.setText(fullDisplayName);
        holder.posteTextView.setText(agent.getPoste());

        // Affichage du département dans le Chip
        if (departments != null && !departments.isEmpty()) {
            boolean found = false;
            for (Department d : departments) {
                if (d.getId() == agent.getDepartmentId()) {
                    holder.deptChip.setText(d.getLibelle());
                    holder.deptChip.setVisibility(View.VISIBLE);
                    found = true;
                    break;
                }
            }
            if (!found) holder.deptChip.setVisibility(View.GONE);
        } else {
            holder.deptChip.setVisibility(View.GONE);
        }

        Glide.with(holder.itemView.getContext())
                .load(agent.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.agentImageView);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onAgentClick(agent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onAgentLongClick(agent);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() { return agents.size(); }

    static class AgentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, posteTextView;
        ImageView agentImageView;
        Chip deptChip;

        public AgentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.agentName);
            posteTextView = itemView.findViewById(R.id.agentPoste);
            agentImageView = itemView.findViewById(R.id.agentImage);
            deptChip = itemView.findViewById(R.id.agentDeptChip);
        }
    }
}
