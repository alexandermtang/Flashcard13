package com.example.flashcard13;

import java.util.ArrayList;

import model.Backend;
import model.Deck;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	public static final int ADD_DECK_ACTIVITY = 0;
	public static final int EDIT_DECK_ACTIVITY = 1;

	Backend backend = Backend.getInstance(this);
	ListView listView;
	ArrayList<Deck> decks = new ArrayList<Deck>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deck_list);

		listView = this.getListView();

		decks = backend.loadAll();
		listView.setAdapter(new ArrayAdapter<Deck>(this, R.layout.deck, decks));

		TextView addDeck = (TextView) findViewById(R.id.add_deck);
		addDeck.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				addDeck();
			}
		});
		registerForContextMenu(listView);
	}
	
	protected void onResume() {
		super.onResume();
		decks = backend.loadAll();
		listView.setAdapter(new ArrayAdapter<Deck>(this, R.layout.deck, decks));
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Deck currDeck = (Deck) getListView().getItemAtPosition(position);
		
		Intent intent = new Intent(this, ViewDeckActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString(ViewDeckActivity.DECK_NAME, currDeck.getName());

		intent.putExtras(bundle);
		startActivity(intent);
	}

	void addDeck() {
		Intent intent = new Intent(this, AddDeckActivity.class);
		startActivityForResult(intent, ADD_DECK_ACTIVITY);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != RESULT_OK) return;

		Bundle bundle = intent.getExtras();
		if (bundle == null) return;

		String name = bundle.getString(AddDeckActivity.DECK_NAME);

		if (requestCode == ADD_DECK_ACTIVITY) {
			Deck newDeck = new Deck(name);
			decks.add(newDeck);
			Backend.getInstance(this).save(newDeck);
			
			decks = backend.loadAll();
		} 
		
		if (requestCode == EDIT_DECK_ACTIVITY) {
			String oldDeckName = bundle.getString(AddDeckActivity.OLD_DECK_NAME);
			
			Deck newDeck = backend.load(oldDeckName);
			newDeck.setName(name);
			
			backend.delete(oldDeckName);
			backend.save(newDeck);
			
			decks = backend.loadAll();
		}

		listView.setAdapter(new ArrayAdapter<Deck>(this, R.layout.deck, decks));
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Deck currDeck = (Deck) getListView().getItemAtPosition(info.position);
	    switch (item.getItemId()) {
	        case R.id.edit:
	        	Intent intent = new Intent(this, AddDeckActivity.class);
	        	Bundle bundle = new Bundle();
	        	bundle.putString(AddDeckActivity.OLD_DECK_NAME, currDeck.getName());
	        	intent.putExtras(bundle);
	        	startActivityForResult(intent, EDIT_DECK_ACTIVITY);
	            return true;
	        case R.id.delete:
	            //action for delete
	        	backend.delete(currDeck.getName());
	        	
	        	decks = backend.loadAll();
	        	listView.setAdapter(new ArrayAdapter<Deck>(this, R.layout.deck, decks));
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
