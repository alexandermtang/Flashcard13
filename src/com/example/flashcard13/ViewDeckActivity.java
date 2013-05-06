package com.example.flashcard13;

import java.util.ArrayList;

import model.Backend;
import model.Card;
import model.Deck;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ViewDeckActivity extends ListActivity {

	public static final String DECK_NAME = "deckName";
	public static final int ADD_CARD_ACTIVITY = 0;
	public static final int EDIT_CARD_ACTIVITY = 1;
	
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
		registerForContextMenu(listView);	
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
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Card currCard = (Card) getListView().getItemAtPosition(info.position);
	    switch (item.getItemId()) {
	        case R.id.edit:
	        	Intent intent = new Intent(this, AddCardActivity.class);
	        	Bundle bundle = new Bundle();
	        	bundle.putString(DECK_NAME, deck.getName());
	        	bundle.putString(AddCardActivity.OLD_CARD_FRONT, currCard.getFront());
	        	bundle.putString(AddCardActivity.OLD_CARD_BACK, (String) currCard.getBack());
	        	intent.putExtras(bundle);
	        	startActivityForResult(intent, EDIT_CARD_ACTIVITY);
	            return true;
	        case R.id.delete:
	        	deck.deleteCard(currCard.getFront());
	        	backend.save(deck);
	        	cards = deck.getCards();
	        	listView.setAdapter(new ArrayAdapter<Card>(this, R.layout.card, cards));
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
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
		
		if (requestCode == EDIT_CARD_ACTIVITY) {
			String oldCardFront = bundle.getString(AddCardActivity.OLD_CARD_FRONT);
//			String oldCardBack = bundle.getString(AddCardActivity.OLD_CARD_BACK);
			
			deck.deleteCard(oldCardFront);
			
			Card newCard = new Card(front, back);
			deck.addCard(newCard);
			backend.save(deck);
			
			cards = deck.getCards();
		}

		listView.setAdapter(new ArrayAdapter<Card>(this, R.layout.card, cards));
	}
	

}
