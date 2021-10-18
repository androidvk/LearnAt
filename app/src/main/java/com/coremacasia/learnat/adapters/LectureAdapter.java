package com.coremacasia.learnat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.activities.WatchLecture;
import com.coremacasia.learnat.databinding.ListLectureBinding;
import com.coremacasia.learnat.helpers.LectureHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.Holder> {
    private List<LectureHelper> list;
    private Context context;

    public LectureAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Holder(ListLectureBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        LectureHelper helper=list.get(position);
        holder.tTitle.setText(helper.getTitle());
        holder.tPosition.setText(""+(position+1));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(
                        holder.itemView.getContext(), WatchLecture.class
                ));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLectureList(List<LectureHelper> list) {
        this.list = list;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "Holder";
        private TextView tTitle,tPosition;
        private View mainView;
        public Holder(@NotNull @NonNull ListLectureBinding itemView) {
            super(itemView.getRoot());
            tTitle=itemView.textView35;
            tPosition=itemView.textView47;
            mainView=itemView.mainView;
        }
    }
}
