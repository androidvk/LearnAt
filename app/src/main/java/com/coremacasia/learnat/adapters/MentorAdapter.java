package com.coremacasia.learnat.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.fragments.MentorViewFragment;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.Holder> {
    private static final String TAG = "MentorAdapter";
    private ArrayList<String> list = new ArrayList<>();
    private CategoryDashboardHelper dataModel;
    private Context activity;

    public void setDataModel(CategoryDashboardHelper dataModel) {
        this.dataModel = dataModel;
        list = dataModel.getMentor_id();
    }

    public MentorAdapter(Context activity) {
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_mentor, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MentorAdapter.Holder holder, int position) {
        String mentor_id = list.get(position);
        for (MentorHelper helper : MyStore.getCommonData().getMentors()) {
            if (helper.getMentor_id().equals(mentor_id)) {
                holder.tName.setText(helper.getName());
                new ImageSetterGlide().defaultImg(holder.itemView.getContext()
                        , helper.getImage(), holder.imageView);
                holder.mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager manager = ((AppCompatActivity) holder.context)
                                .getSupportFragmentManager();

                        Bundle bundle = new Bundle();
                        //bundle.putString("cat", helper.getCategory_id());
                        bundle.putString("from", "homeMentorAdapter");
                        bundle.putString("mentorId", helper.getMentor_id());

                        FragmentTransaction fragmenttransaction =
                                manager.beginTransaction();
                        fragmenttransaction.
                                setCustomAnimations(R.anim.enter_from_right,
                                        R.anim.exit_to_left,
                                        R.anim.enter_from_left,
                                        R.anim.exit_to_right);

                        MentorViewFragment frag = new MentorViewFragment();
                        frag.setArguments(bundle);
                        fragmenttransaction.replace(android.R.id.content, frag)
                                .addToBackStack(frag.TAG);
                        fragmenttransaction.commit();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "MentorHolder";
        private ImageView imageView;
        private TextView tName;
        private View mainView;
        private Context context;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.textView40);
            imageView = itemView.findViewById(R.id.imageView12);
            mainView = itemView.findViewById(R.id.mainView);
            context = itemView.getContext();
        }

    }
}
