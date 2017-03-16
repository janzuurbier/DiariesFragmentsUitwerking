package nl.hu.zrb.diariesWithFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class DetailFragment extends Fragment {
    DiaryEntry entry;
    TextView tv1, tv2, tv3;

    public DetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        tv1 = (TextView)v.findViewById(R.id.titleView);
        tv2 = (TextView) v.findViewById(R.id.contentView);
        tv3 = (TextView) v.findViewById(R.id.dateView);
        if (entry != null){
            tv1.setText(entry.getTitle());
            tv2.setText(entry.getContent());
            Date date = new Date(entry.getDate());
            String datedata = DateFormat.format("MMM dd, yyyy h:mmaa", date).toString();
            tv3.setText(datedata);
        }
        return v;
    }

    public void setDiaryEntry(String key){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("entries").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                entry = dataSnapshot.getValue(DiaryEntry.class);
                if (entry != null){
                    tv1.setText(entry.getTitle());
                    tv2.setText(entry.getContent());
                    Date date = new Date(entry.getDate());
                    String datedata = DateFormat.format("MMM dd, yyyy h:mmaa", date).toString();
                    tv3.setText(datedata);
                }
                else {
                    tv1.setText("leeg");
                    tv2.setText("leeg");
                    tv3.setText("leeg");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
