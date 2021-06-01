package com.coremacasia.learnat.utility;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

}
