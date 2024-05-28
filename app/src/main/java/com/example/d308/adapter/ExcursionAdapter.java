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
import com.example.d308.entities.Excursion;

public class ExcursionAdapter extends ListAdapter<Excursion, ExcursionAdapter.ExcursionHolder> {

    private OnItemClickListener listener;

    public ExcursionAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Excursion> DIFF_CALLBACK = new DiffUtil.ItemCallback<Excursion>() {

        @Override
        public boolean areItemsTheSame(@NonNull Excursion oldItem, @NonNull Excursion newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Excursion oldItem, @NonNull Excursion newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public ExcursionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_excursion_model, parent, false);
        return new ExcursionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionHolder holder, int position) {
        Excursion current = getItem(position);
        holder.text_view_title.setText(current.getTitle());
        holder.text_view_date.setText(current.getDate());
    }

    public Excursion getExcursionAt(int position) {
        return getItem(position);
    }

    class ExcursionHolder extends RecyclerView.ViewHolder {
        private TextView text_view_title;
        private TextView text_view_date;

        public ExcursionHolder(@NonNull View itemView) {
            super(itemView);
            text_view_title = itemView.findViewById(R.id.text_view_excursion_title);
            text_view_date = itemView.findViewById(R.id.text_view_excursion_date);

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
        void onItemClick(Excursion excursion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
