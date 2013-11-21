package de.michi.proximity;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.michi.proximity.contentprovider.BagceptionContentProvider;

public class AddItem extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
	}
	
	public void goToMenu(View view){
    	Intent menu_intent = new Intent(this, MainActivity.class);
    	startActivity(menu_intent);
    }

	public void onClickAddItem(View view){
		
		// Add new Item
		ContentValues values = new ContentValues();
		
		values.put(BagceptionContentProvider.NAME, ((EditText)findViewById(R.id.editName)).getText().toString());
		values.put(BagceptionContentProvider.DESCRIPTION, ((EditText)findViewById(R.id.editDescription)).getText().toString());
		values.put(BagceptionContentProvider.VISIBILITY, ((Spinner)findViewById(R.id.visibility)).getSelectedItem().toString());
		values.put(BagceptionContentProvider.TAG_ID, ((Spinner)findViewById(R.id.foundTags)).getSelectedItem().toString());
		
		Uri uri = getContentResolver().insert(BagceptionContentProvider.CONTENT_URI, values);
		
		Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
	}
	
}
