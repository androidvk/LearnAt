package com.coremacasia.learnat.ui.home.subjects;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.commons.CommonDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.Holder> {
    private Context activity;
    private static final String TAG = "SubjectAdapter";
    private CommonDataModel dataModel;
    private ArrayList<SubjectHelper> list = new ArrayList<>();

    public void setDataModel(CommonDataModel dataModel) {
        this.dataModel = dataModel;
        list = dataModel.getSubjects();
    }

    public SubjectAdapter(Context activity) {
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_subjects,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubjectAdapter.Holder holder, int position) {
        holder.onBind(holder,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "Subject Holder";
        private TextView tName,tCategory;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tName=itemView.findViewById(R.id.textView38);
            tCategory=itemView.findViewById(R.id.textView39);
        }

        public void onBind(Holder holder, int position) {
            SubjectHelper helper=list.get(position);

            tName.setText(helper.getName());
            tCategory.setText(helper.getCategoryid());

        }
    }
}
