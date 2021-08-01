package com.coremacasia.learnat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.SubjectHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.Holder> {
    private Context activity;
    private static final String TAG = "SubjectAdapter";
    private CategoryDashboardHelper dataModel;
    private ArrayList<String> list = new ArrayList<>();

    public void setDataModel(CategoryDashboardHelper dataModel) {
        this.dataModel = dataModel;
        list = dataModel.getSubject_id();
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
        String subjectId=list.get(position);
        for(SubjectHelper helper: MyStore.getCommonData().getAll_subjects()){
            if(helper.getSubject_id().equals(subjectId)){
                holder.tName.setText(helper.getTitle());
                new ImageSetterGlide().circleImg(
                        holder.itemView.getContext(),helper.getIcon(),
                        holder.imageView);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "Subject Holder";
        private TextView tName;
        private CircleImageView imageView;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tName=itemView.findViewById(R.id.textView38);
            imageView=itemView.findViewById(R.id.imageView11);
        }
    }
}
