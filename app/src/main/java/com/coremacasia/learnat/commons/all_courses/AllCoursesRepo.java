package com.coremacasia.learnat.commons.all_courses;

import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AllCoursesRepo {
    private static final String TAG = "CompExamRepo";
    private OnFirestoreTaskComplete onFirestoreTaskComplete;
    public AllCoursesRepo(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommonData(DocumentReference documentReference) {
        DocumentReference reference= Reference.superRef(RMAP.all_courses);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (value!=null && value.exists()) {
                    onFirestoreTaskComplete.commonData(value
                            .toObject(CourseModel.class));
                    
                } else {
                    onFirestoreTaskComplete.error(error);
                }
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void commonData(CourseModel CourseModel);
        void error(Exception e);
    }
}
