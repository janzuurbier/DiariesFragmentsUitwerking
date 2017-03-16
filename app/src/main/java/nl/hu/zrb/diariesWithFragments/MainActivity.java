package nl.hu.zrb.diariesWithFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, MasterFragment.OnFragmentInteractionListener  {
	// Firebase instance variables
	private FirebaseAuth mFirebaseAuth;
	private FirebaseUser mFirebaseUser;

	private String mUsername;
	private String mPhotoUrl;
	private String TAG = "MainActivity";
	private String keyOfEntryShowingInDetailFragement = null;
	DetailFragment detail;

	public static final String ANONYMOUS = "anonymous";
	private GoogleApiClient mGoogleApiClient;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.this, InsertDiary.class );
				startActivity(i);
			}
		});

		mUsername = ANONYMOUS;

		// Initialize Firebase Auth
		mFirebaseAuth = FirebaseAuth.getInstance();
		mFirebaseUser = mFirebaseAuth.getCurrentUser();
		if (mFirebaseUser == null) {
			// Not signed in, launch the Sign In activity
			startActivity(new Intent(this, SignInActivity.class));
			finish();
			return;
		} else {
			mUsername = mFirebaseUser.getDisplayName();
			if (mFirebaseUser.getPhotoUrl() != null) {
				mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
			}
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API)
				.build();

		detail = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.details_frag);
	}
		

	@Override
	public void onResume(){
		super.onResume();
	}    
	
	@Override
	public void onPause(){
		super.onPause();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_show_diaries, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.sign_out_menu) {
			mFirebaseAuth.signOut();
			Auth.GoogleSignInApi.signOut(mGoogleApiClient);
			mUsername = ANONYMOUS;
			startActivity(new Intent(this, SignInActivity.class));
			return true;
		}
		if (id == R.id.action_delete){
			DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
			mDatabase.child("entries").child(keyOfEntryShowingInDetailFragement).removeValue();
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		// An unresolvable error has occurred and Google APIs (including Sign-In) will not
		// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onFragmentInteraction(DiaryEntry entry, String key) {
		detail.setDiaryEntry(key);
		keyOfEntryShowingInDetailFragement = key;
	}
}
