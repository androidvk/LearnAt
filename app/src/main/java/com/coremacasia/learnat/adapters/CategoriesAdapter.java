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
import com.coremacasia.learnat.helpers.CategoryHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Holder> {
    private Context activity;

    public void setDataModel(CommonDataModel dataModel) {
        this.dataModel = dataModel;
        list=dataModel.getCategory();

    }

    private CommonDataModel dataModel;
    private ArrayList<CategoryHelper> list=new ArrayList<>();
    private static final String TAG = "CategoriesAdapter";
    public CategoriesAdapter(Context activity) {
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_course_category,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoriesAdapter.Holder holder, int position) {
        holder.onBind(holder,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "CategoryHolder";
        private Holder holder;
        private int position;
        private CircleImageView imageView;
        private TextView tName;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView13);
            tName=itemView.findViewById(R.id.textView41);
        }

        public void onBind(Holder holder, int position) {
            this.holder = holder;
            this.position = position;
            CategoryHelper helper=list.get(position);
            new ImageSetterGlide().circleImg(itemView.getContext(),
                    helper.getThumbnail(), imageView);
            tName.setText(helper.getName());
        }
    }
}
