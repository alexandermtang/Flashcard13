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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewCardActivity extends Activity
        implements FragmentManager.OnBackStackChangedListener {
    private Handler mHandler = new Handler();
    private boolean mShowingBack = false;
    
    public static final String CARD_FRONT = "cardFrontFrag";
    public static final String CARD_BACK = "cardBackFrag";
    
    private Backend backend = Backend.getInstance(this);
    
    private CardFrontFragment cardFrontFrag;
    private CardBackFragment cardBackFrag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_card_activity);
        
        Bundle bundle = getIntent().getExtras();
        Deck deck = backend.load(bundle.getString(ViewDeckActivity.DECK_NAME));
        Card card = deck.getCard(bundle.getString(CARD_FRONT));
        
        cardFrontFrag = new CardFrontFragment();
        bundle = new Bundle();
        bundle.putString(CARD_FRONT, card.getFront());
        cardFrontFrag.setArguments(bundle);
        
        cardBackFrag = new CardBackFragment();
        bundle = new Bundle();
        bundle.putString(CARD_BACK, (String)card.getBack());
        cardBackFrag.setArguments(bundle);
        
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, cardFrontFrag)
                    .commit();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
                mShowingBack
                        ? R.string.action_front
                        : R.string.action_back);
        item.setIcon(mShowingBack
                ? R.drawable.ic_action_photo
                : R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
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
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, cardBackFrag)
                .addToBackStack(null)
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
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
        
        public void onResume() {
        	super.onResume();
        	String front = getArguments().getString(CARD_FRONT);
        	TextView text = (TextView) getActivity().findViewById(R.id.text_front);
        	if (text != null) text.setText(front);
        }
    }

    public static class CardBackFragment extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
        
        public void onResume() {
        	super.onResume();
        	String back = getArguments().getString(CARD_BACK);
        	TextView text = (TextView) getActivity().findViewById(R.id.text_back);
        	if (text != null) text.setText(back);
        }
    }
}
