package nl.hu.zrb.diariesWithFragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class InsertDiary extends AppCompatActivity {
    EditText titleET, contentET;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleET = (EditText) findViewById(R.id.editText1);
        contentET = (EditText) findViewById(R.id.editText2);
        mDatabase =  FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insert_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveItem) {
            DiaryEntry entry = new DiaryEntry();
            entry.setTitle(titleET.getText().toString());
            entry.setContent(contentET.getText().toString());
            entry.setDate(new Date().getTime());
            //String key = mDatabase.child("entries").push().getKey();
            //entry.key = key;
            mDatabase.child("entries").push().setValue(entry);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

}
