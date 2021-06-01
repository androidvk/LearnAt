package com.coremacasia.learnat.startItems;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.Reference;
import com.coremacasia.learnat.utility.kMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseMenuAdapter extends RecyclerView.Adapter<CourseMenuAdapter.Holder> {
    private Context activity;
    private ArrayList<menu_list> listHelper;
    private static final String TAG = "CourseMenuAdapter";
    private Group gSubjects;
    private Group gIcode;
    private FirebaseUser phoneUser= FirebaseAuth.getInstance().getCurrentUser();

    public CourseMenuAdapter(Context activity, ArrayList<menu_list> listHelper,
                             Group gSubjects, Group gIcode) {
        this.gSubjects = gSubjects;
        this.gIcode = gIcode;
        Log.e(TAG, "CourseMenuAdapter: ");
        this.activity = activity;
        this.listHelper = listHelper;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_course_menu, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseMenuAdapter.Holder holder,
                                 int position) {
        holder.onBind(holder, position);
    }

    @Override
    public int getItemCount() {
        return listHelper.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "Holder";
        private TextView tName, tDesc;
        private ImageView iThumbnail;
        private CardView cardView;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.tName);
            tDesc = itemView.findViewById(R.id.tDesc);
            cardView = itemView.findViewById(R.id.c1);
            iThumbnail = itemView.findViewById(R.id.iCourseThumbnail);
        }

        public void onBind(Holder holder, int position) {
            cardView = holder.cardView;
            tDesc = holder.tDesc;
            tName = holder.tName;
            menu_list list = listHelper.get(position);

            tName.setText(list.getName_en());
            tDesc.setText(list.getDesc_en());
            new ImageSetterGlide().defaultImg(itemView.getContext(),
                    list.getImage(), iThumbnail);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map map=new HashMap<>();
                    map.put(kMap.preferred_type1,list.getId());
                    Reference.userRef().document(phoneUser.getUid()).set(map, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    gSubjects.setVisibility(View.GONE);
                                    gIcode.setVisibility(View.VISIBLE);
                                }
                            });
                }
            });
        }
    }
}
