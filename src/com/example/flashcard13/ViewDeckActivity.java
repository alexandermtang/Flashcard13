package com.example.flashcard13;

import java.util.ArrayList;

import model.Backend;
import model.Card;
import model.Deck;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewDeckActivity extends ListActivity {

	public static final String DECK_NAME = "deckName";
	public static final int ADD_CARD_ACTIVITY = 0;
	
	Backend backend = Backend.getInstance(this);
	ListView listView;
	ArrayList<Card> cards = new ArrayList<Card>();
	Deck deck;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_deck_activity);
		
		Bundle bundle = getIntent().getExtras();
		String deckName = bundle.getString(DECK_NAME);
		
		deck = backend.load(deckName);
		cards = deck.getCards();
		
		listView = this.getListView();
		
		listView.setAdapter(new ArrayAdapter<Card>(this, R.layout.card, cards));

		TextView addCard = (TextView) findViewById(R.id.add_card);
		addCard.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				addCard();
			}
		});
		
	}
	
	private void addCard() {
		Intent intent = new Intent(this, AddCardActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(DECK_NAME, deck.getName());
		intent.putExtras(bundle);
		startActivityForResult(intent, ADD_CARD_ACTIVITY);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Card currCard = (Card) getListView().getItemAtPosition(position);
		
		Intent intent = new Intent(this, ViewCardActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString(ViewDeckActivity.DECK_NAME, deck.getName());
		bundle.putString(ViewCardActivity.CARD_FRONT, currCard.getFront());
		bundle.putString(ViewCardActivity.CARD_BACK, (String) currCard.getBack());
		intent.putExtras(bundle);
		
		startActivity(intent);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != RESULT_OK) return;

		Bundle bundle = intent.getExtras();
		if (bundle == null) return;

		String front = bundle.getString(AddCardActivity.CARD_FRONT);
		String back = bundle.getString(AddCardActivity.CARD_BACK);

		if (requestCode == ADD_CARD_ACTIVITY) {
			Card newCard = new Card(front, back);
			cards.add(newCard);
			backend.save(deck);
		}

		listView.setAdapter(new ArrayAdapter<Card>(this, R.layout.card, cards));
	}
	

}
