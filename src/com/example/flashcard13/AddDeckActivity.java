package com.example.flashcard13;

import model.Backend;
import model.Deck;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddDeckActivity extends FragmentActivity {

	public static final String DECK_NAME = "deckName";
	public static final String OLD_DECK_NAME = "oldDeckName";

	Backend backend = Backend.getInstance(this);
	EditText deckName;
	Button deckCancel, deckSave;
	String incompleteMessage="", duplicateMessage="", oldDeckName;
	Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_deck);

		deckName = (EditText) findViewById(R.id.deck_name);

		deckSave = (Button) findViewById(R.id.deck_save);
		deckCancel = (Button) findViewById(R.id.deck_cancel);

		deckCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		bundle = getIntent().getExtras();
		if (bundle == null) {
			addDeck();
		} else {
			editDeck();
		}
	}
	
	private void addDeck() {
		deckSave.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				String name = deckName.getText().toString();

				if (name == null || name.length() == 0) {
					incompleteMessage += "Name";
					incompleteMessage();
					return;
				}
				
				// duplicate deck name
				if (backend.hasDeck(new Deck(name))) {
					duplicateMessage += name;
					duplicateMessage();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString(DECK_NAME, deckName.getText().toString().trim());

				Intent intent = new Intent();
				intent.putExtras(bundle);

				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	private void editDeck() {
		oldDeckName = bundle.getString(OLD_DECK_NAME);
		deckName.setText(oldDeckName);
		
		deckSave.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				String name = deckName.getText().toString();

				if (name == null || name.length() == 0) {
					incompleteMessage += "Name";
					incompleteMessage();
					return;
				}
				
				// duplicate deck name
				if (backend.hasDeck(new Deck(name))) {
					duplicateMessage += name;
					duplicateMessage();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString(OLD_DECK_NAME, oldDeckName);
				bundle.putString(DECK_NAME, name.trim());

				Intent intent = new Intent();
				intent.putExtras(bundle);

				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void incompleteMessage() {
		incompleteMessage += " required!";
		DeckInfoDialogFragment newFragment = new DeckInfoDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DeckInfoDialogFragment.MESSAGE, incompleteMessage);
		newFragment.setArguments(bundle);

		newFragment.show(AddDeckActivity.this.getSupportFragmentManager(),
				"incomplete message");
		return;
	}

	private void duplicateMessage() {
		duplicateMessage += " already exists!";
		DeckInfoDialogFragment newFragment = new DeckInfoDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DeckInfoDialogFragment.MESSAGE, duplicateMessage);
		newFragment.setArguments(bundle);

		newFragment.show(AddDeckActivity.this.getSupportFragmentManager(),
				"duplicate message");
		return;
	}
}
