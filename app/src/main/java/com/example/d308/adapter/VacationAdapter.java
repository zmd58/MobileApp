package com.example.d308.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.R;
import com.example.d308.entities.Vacation;

public class VacationAdapter extends ListAdapter<Vacation, VacationAdapter.VacationHolder> {
    private OnItemClickListener listener;

    public VacationAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Vacation> DIFF_CALLBACK = new DiffUtil.ItemCallback<Vacation>() {

        @Override
        public boolean areItemsTheSame(@NonNull Vacation oldItem, @NonNull Vacation newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Vacation oldItem, @NonNull Vacation newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getLodging().equals(newItem.getLodging()) &&
                    oldItem.getStart_date().equals(newItem.getStart_date()) &&
                    oldItem.getEnd_date().equals(newItem.getEnd_date());
        }
    };

    @NonNull
    @Override
    public VacationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_vacation_model, parent, false);
        return new VacationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationHolder holder, int position) {
        Vacation current = getItem(position);
        holder.text_view_id.setText(String.valueOf(current.getId()));
        holder.text_view_title.setText(current.getTitle());
        holder.text_view_lodging.setText(current.getLodging());
        holder.text_view_start_date.setText(current.getStart_date());
        holder.text_view_end_date.setText(current.getEnd_date());
    }

    public Vacation getVacationAt(int position) {
        return getItem(position);
    }

    class VacationHolder extends RecyclerView.ViewHolder {
        private TextView text_view_id;
        private TextView text_view_title;
        private TextView text_view_lodging;
        private TextView text_view_start_date;
        private TextView text_view_end_date;

        public VacationHolder(@NonNull View itemView) {
            super(itemView);
            text_view_id = itemView.findViewById(R.id.text_view_id);
            text_view_title = itemView.findViewById(R.id.text_view_title);
            text_view_lodging = itemView.findViewById(R.id.text_view_lodging);
            text_view_start_date = itemView.findViewById(R.id.text_view_start_date);
            text_view_end_date = itemView.findViewById(R.id.text_view_end_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Vacation vacation);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
