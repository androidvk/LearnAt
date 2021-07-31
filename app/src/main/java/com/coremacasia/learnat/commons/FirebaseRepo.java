package com.coremacasia.learnat.commons;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

public class FirebaseRepo {
    private static final String TAG = "FirebaseRepo";
    private OnFirestoreTaskComplete onFirestoreTaskComplete;
    public FirebaseRepo(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommonData(DocumentReference documentReference) {
        if (CommonData.getCommonDataModel() != null) {
            Log.e(TAG, "getCommonData: Old Fetch" );
            onFirestoreTaskComplete.commonData(CommonData.getCommonDataModel());
        } else {
            Log.e(TAG, "getCommonData:  New Fetch" );
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                    if (value!=null && value.exists()) {
                        onFirestoreTaskComplete.commonData(value
                                .toObject(CommonDataModel.class));
                        CommonData.setCommonDataModel(value.toObject(CommonDataModel.class));

                    } else {
                        onFirestoreTaskComplete.error(error);
                    }
                }
            });
        }
    }

    public interface OnFirestoreTaskComplete {
        void commonData(CommonDataModel commonDataModel);

        void error(Exception e);
    }
}
