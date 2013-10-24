package de.michi.proximity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;

public class ChooseItems extends ListActivity {

	String[] values = new String[] { "Fuﬂball", "T-Shirt", "kurze Hose",
		    "lange Hose", "Fuﬂballschuhe", "Laufschuhe", "Trainingsjacke", "Stutzen",
		    "Schienbeinschoner", "Trinken", "Regenschirm", "Regenjacke", "Laptop", "Block",
		    "M‰ppchen", "Laptop-Ladekabel", "Handy-Ladekabel", "Essen", "Tablet"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// create an array of Strings, that will be put to our ListActivity
	    ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this, getModel());
	    setListAdapter(adapter);
	  }

	private List<Model> getModel() {
	    List<Model> list = new ArrayList<Model>();
	    for(int i = 0; i < values.length; i++){
	    	list.add(get(values[i]));
	    }
	   
	    return list;
	  }

	  private Model get(String s) {
	    return new Model(s);
	  }
	  
	  public void goToMenu(View view){
	    	Intent menu_intent = new Intent(this, MainActivity.class);
	    	startActivity(menu_intent);
	    }

}
