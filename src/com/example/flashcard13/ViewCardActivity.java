package com.example.flashcard13;

import model.Backend;
import model.Card;
import model.Deck;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewCardActivity extends Activity implements
		FragmentManager.OnBackStackChangedListener {
	private Handler mHandler = new Handler();
	private boolean mShowingBack = false;

	public static final String CARD_FRONT = "cardFrontFrag";
	public static final String CARD_BACK = "cardBackFrag";

	private Backend backend = Backend.getInstance(this);
	private Deck deck;
	private Card card;

	private CardFrontFragment cardFrontFrag;
	private CardBackFragment cardBackFrag;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_card_activity);

		Bundle bundle = getIntent().getExtras();
		deck = backend.load(bundle.getString(ViewDeckActivity.DECK_NAME));
		card = deck.getCard(bundle.getString(CARD_FRONT));

		cardFrontFrag = new CardFrontFragment();
		bundle = new Bundle();
		bundle.putString(CARD_FRONT, card.getFront());
		cardFrontFrag.setArguments(bundle);

		cardBackFrag = new CardBackFragment();
		bundle = new Bundle();
		bundle.putString(CARD_BACK, (String) card.getBack());
		cardBackFrag.setArguments(bundle);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, cardFrontFrag).commit();
		} else {
			mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		}

		getFragmentManager().addOnBackStackChangedListener(this);
		View view = (View) findViewById(R.id.container);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				flipCard();
			}
		});
	}

	private final int RIGHT_BUTTON = 0;
	private final int WRONG_BUTTON = 1;

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		if (mShowingBack) {
			// add right wrong buttons
			MenuItem right = menu.add(Menu.NONE, RIGHT_BUTTON, Menu.NONE,
					"Right");
			right.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			right.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					card.incrementRight();
					flipCard();
					backend.save(deck);
					return true;
				}
			});

			MenuItem wrong = menu.add(Menu.NONE, WRONG_BUTTON, Menu.NONE,
					"Wrong");
			wrong.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			wrong.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					card.incrementWrong();
					flipCard();
					backend.save(deck);
					return true;
				}
			});
		} else {
			menu.removeItem(RIGHT_BUTTON);
			menu.removeItem(WRONG_BUTTON);
		}

		MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
				mShowingBack ? R.string.action_front : R.string.action_back);
		item.setIcon(mShowingBack ? R.drawable.ic_action_photo
				: R.drawable.ic_action_info);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, ViewDeckActivity.class).putExtra(
					ViewDeckActivity.DECK_NAME, deck.getName());
			NavUtils.navigateUpTo(this, intent);
			return true;

		case R.id.action_flip:
			flipCard();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void flipCard() {
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		mShowingBack = true;

		getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.animator.card_flip_right_in,
						R.animator.card_flip_right_out,
						R.animator.card_flip_left_in,
						R.animator.card_flip_left_out)
				.replace(R.id.container, cardBackFrag).addToBackStack(null)
				.commit();

		mHandler.post(new Runnable() {
			public void run() {
				invalidateOptionsMenu();
			}
		});
	}

	public void onBackStackChanged() {
		mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		invalidateOptionsMenu();
	}

	public static class CardFrontFragment extends Fragment {

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_card_front, container,
					false);
		}

		public void onResume() {
			super.onResume();
			String front = getArguments().getString(CARD_FRONT);
			TextView text = (TextView) getActivity().findViewById(
					R.id.text_front);
			if (text != null)
				text.setText(front);
		}
	}

	public static class CardBackFragment extends Fragment {

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_card_back, container,
					false);
		}

		public void onResume() {
			super.onResume();
			String back = getArguments().getString(CARD_BACK);
			TextView text = (TextView) getActivity().findViewById(
					R.id.text_back);
			if (text != null)
				text.setText(back);
		}
	}
}
