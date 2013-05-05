package com.example.flashcard13;

import java.util.ArrayList;

import model.Backend;
import model.Deck;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	public static final int ADD_DECK_ACTIVITY = 0;

	Backend backend = Backend.getInstance(this);
	ListView listView;
	ArrayList<Deck> decks = new ArrayList<Deck>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deck_list);

		listView = this.getListView();

		// TEMP DELETE THIS LATER
		backend.deleteAll();

		decks = backend.loadAll();

		listView.setAdapter(new ArrayAdapter<Deck>(this, R.layout.deck, decks));

		TextView addDeck = (TextView) findViewById(R.id.add_deck);
		addDeck.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				addDeck();
			}
		});

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Deck currDeck = (Deck) getListView().getItemAtPosition(position);

		
		Log.d(getClass().getSimpleName(), currDeck.getName());
		System.out.println(currDeck.getName());
		
		
		
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

		if (resultCode != RESULT_OK)
			return;

		Bundle bundle = intent.getExtras();
		if (bundle == null)
			return;

		String name = bundle.getString(AddDeckActivity.DECK_NAME);

		if (requestCode == ADD_DECK_ACTIVITY) {
			Deck newDeck = new Deck(name);
			decks.add(newDeck);
			Backend.getInstance(this).save(newDeck);
			
			decks = backend.loadAll();
		} else { // update
			// myList.update(new Song(bundle.getInt(AddSong.SONG_ID), name,
			// artist, album, year));
		}

		listView.setAdapter(new ArrayAdapter<Deck>(this, R.layout.deck, decks));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
