package de.michi.proximity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

@SuppressLint("NewApi")
public class AddActivity extends Activity {
	
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_add);
	  }
	  
	  public void chooseItem(View view){
	    	Intent choose_item = new Intent(this, ChooseItems.class);
	    	startActivity(choose_item);
	    }
	  
	  public void goToMenu(View view){
	    	Intent menu_intent = new Intent(this, MainActivity.class);
	    	startActivity(menu_intent);
	    }
} 