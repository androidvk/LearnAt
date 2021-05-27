package com.coremacasia.learnat.xtras;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RulesTest extends AppCompatActivity {
    private static final String TAG = "Testing";
    String key1,key2,value1,value2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        EditText eKey=findViewById(R.id.eKey);
        EditText eValue=findViewById(R.id.eValue);

        EditText eKey2=findViewById(R.id.eKey2);
        EditText eValue2=findViewById(R.id.eValue2);

        Button submit=findViewById(R.id.bSubmit);
        TextView tInfo=findViewById(R.id.tText);
        TextView tIncomingData=findViewById(R.id.tIncomingData);


        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users")
                .document("1");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key1=eKey.getText().toString().trim();
                value1=eValue.getText().toString().trim();

                key2=eKey2.getText().toString().trim();
                value2=eValue2.getText().toString().trim();

                Map map=new HashMap();
                map.put(key1,value1);
                map.put(key2,value2);

                userRef.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        if(task.isComplete()){

                            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent( DocumentSnapshot snapshot,  FirebaseFirestoreException error) {
                                    /*tIncomingData.setText(snapshot.get(key1)+": "+snapshot.get(value1)
                                    +"\n"+snapshot.get(key2)+": "+snapshot.get(value2)+snapshot);*/
                                    //Log.e(TAG, "onEvent: "+snapshot.getData() );
                                    tIncomingData.setText(snapshot.getData()+"");
                                    if(error!=null){
                                        tInfo.setText(error.getLocalizedMessage()+"");
                                    }

                                }
                            });
                        }
                    }
                });
            }
        });
        //getData

    }
}