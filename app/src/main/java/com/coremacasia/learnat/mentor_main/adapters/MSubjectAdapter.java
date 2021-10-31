package com.coremacasia.learnat.mentor_main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.adapters.PopularAdapter;
import com.coremacasia.learnat.helpers.SubjectHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import java.util.ArrayList;

public class MSubjectAdapter extends RecyclerView.Adapter<MSubjectAdapter.Holder> {
    private ArrayList<String> list;

    public MSubjectAdapter(Context applicationContext) {

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_m_subjects,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

            for (SubjectHelper helper : MyStore.getCommonData().getAll_subjects()) {
                if (helper.getSubject_id().equals(list.get(position))) {
                    holder.tSubjects.setText(helper.getTitle());
                    //new ImageSetterGlide().defaultImg(getActivity(), helper.getIcon(), iSubjectImage);
                }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataModel(ArrayList<String> list) {

        this.list = list;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "MSubjectAdapter.Holder";
        private TextView tSubjects;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tSubjects=itemView.findViewById(R.id.textView78);
        }
    }
}
