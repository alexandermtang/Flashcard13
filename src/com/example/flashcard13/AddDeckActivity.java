//package com.example.flashcard13;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//
//public class AddDeckActivity extends FragmentActivity {
//
//	public static final String DECK_NAME = "deckName";
//
//	EditText deckName;
//
//	Button songSave;
//	Button songCancel;
//
//	String incompleteMessage;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.add_song);
//
//		songName = (EditText) findViewById(R.id.song_name);
//
//		songSave = (Button) findViewById(R.id.song_save);
//		songCancel = (Button) findViewById(R.id.song_cancel);
//
//		songCancel.setOnClickListener(new OnClickListener() {
//			public void onClick(View view) {
//				setResult(RESULT_CANCELED);
//				finish();
//			}
//		});
//
//		songSave.setOnClickListener(new OnClickListener() {
//			public void onClick(View view) {
//
//				String name = songName.getText().toString();
//				String artist = songArtist.getText().toString();
//
//				boolean ok = true;
//				incompleteMessage = "";
//
//				if (name == null || name.length() == 0) {
//					incompleteMessage += "Song ";
//					ok = false;
//				}
//				if (artist == null || artist.length() == 0) {
//					if (!ok) {
//						incompleteMessage += "and artist are ";
//					} else {
//						incompleteMessage += " Artist is ";
//					}
//					ok = false;
//				} else {
//					if (!ok) {
//						incompleteMessage += " is ";
//					}
//				}
//
//				//				if (!ok) {
//				//					// show the dialog
//				//					incompleteMessage += " required";
//				//					DialogFragment newFragment = new SongInfoDialogFragment();
//				//					Bundle bundle = new Bundle();
//				//					bundle.putString(SongInfoDialogFragment.MESSAGE,
//				//							incompleteMessage);
//				//					newFragment.setArguments(bundle);
//				//
//				//					newFragment.show(AddSong.this.getSupportFragmentManager(),
//				//							"incomplete message");
//				//					return;
//				//				}
//
//				Bundle bundle = new Bundle();
//				bundle.putString(SONG_NAME, songName.getText().toString());
//				bundle.putString(SONG_ARTIST, songArtist.getText().toString());
//				bundle.putString(SONG_ALBUM, songAlbum.getText().toString());
//				bundle.putString(SONG_YEAR, songYear.getText().toString());
//
//				Intent intent = new Intent();
//				intent.putExtras(bundle);
//
//				setResult(RESULT_OK, intent);
//				finish();
//			}
//		});
//
//	}
//
//}
