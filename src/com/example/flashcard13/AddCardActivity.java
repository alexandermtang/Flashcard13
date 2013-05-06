package com.example.flashcard13;

import model.Backend;
import model.Card;
import model.Deck;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddCardActivity extends FragmentActivity {

	public static final String CARD_FRONT = "cardFront";
	public static final String CARD_BACK = "cardBack";

	Backend backend = Backend.getInstance(this);
	EditText cardFront, cardBack;
	Button cardCancel, cardSave;
	String incompleteMessage="", duplicateMessage="";
	String deckName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_card);
		
		Bundle bundle = getIntent().getExtras();
		deckName = bundle.getString(ViewDeckActivity.DECK_NAME);
		
		cardFront = (EditText) findViewById(R.id.front_name);
		cardBack = (EditText) findViewById(R.id.back_name);

		cardSave = (Button) findViewById(R.id.deck_save);
		cardCancel = (Button) findViewById(R.id.deck_cancel);

		cardCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		cardSave.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				String front = cardFront.getText().toString();
				String back = cardBack.getText().toString();

				if (front == null || front.length() == 0 
						|| back == null || back.length() == 0) {
					incompleteMessage += "Front/Back";
					incompleteMessage();
					return;
				}
				
				// duplicate card front 
				Deck deck = backend.load(deckName);
				if (deck.hasCard(new Card(front, back))) {
					duplicateMessage += "Card with front: " + front;
					duplicateMessage();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString(CARD_FRONT, cardFront.getText().toString().trim());
				bundle.putString(CARD_BACK, cardBack.getText().toString().trim());

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

		newFragment.show(AddCardActivity.this.getSupportFragmentManager(),
				"incomplete message");
		return;
	}

	private void duplicateMessage() {
		duplicateMessage += " already exists!";
		DeckInfoDialogFragment newFragment = new DeckInfoDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DeckInfoDialogFragment.MESSAGE, duplicateMessage);
		newFragment.setArguments(bundle);

		newFragment.show(AddCardActivity.this.getSupportFragmentManager(),
				"duplicate message");
		return;
	}
}
