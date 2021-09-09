package com.coremacasia.learnat.utility;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Reference {
    public static FirebaseFirestore reference = FirebaseFirestore.getInstance();
    public static CollectionReference userRef() {
        return reference.collection(RMAP.users);
    }

    public static DocumentReference userRef(String userId) {
        return userRef().document(userId);
    }

    public static CollectionReference superRef(){
        return reference.collection(RMAP.super_);
    }
    public static DocumentReference superRef(String docId){
        return superRef().document(docId);
    }


    public static CollectionReference superCourseRef(String CAT) {
        return reference.collection("c_" + CAT);
    }
    public static DocumentReference superCourseRef(String CAT, String courseId) {
        return superCourseRef(CAT).document(courseId);
    }


    //=================================
    public static StorageReference storage = FirebaseStorage.getInstance().getReference();

    public static StorageReference getMentorImageRef(){
        return storage.child("mentors");
    }

}
