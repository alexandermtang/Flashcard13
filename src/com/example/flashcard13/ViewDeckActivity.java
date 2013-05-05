package com.example.flashcard13;

import model.Backend;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ViewDeckActivity extends ListActivity {

	public static final String DECK_NAME = "deckName";
	
	Backend backend = Backend.getInstance(this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_deck_activity);
		
		
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		
		
	}

}
