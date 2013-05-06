package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

public class Backend implements BackendInterface {

	private static Backend backend;
	private static Context context;
	private ArrayList<Deck> decks;

	private Backend() {
		decks = new ArrayList<Deck>();
	}

	public static synchronized Backend getInstance(Context ctx) {
		context = ctx;
		if (backend == null) backend = new Backend();
		return backend;
	}

	public void setContext(Context ctx) { context = ctx; }

	public boolean save(Object o) {
		Deck deck = (Deck) o;
		String fileName = deck.getName();
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(deck);
			os.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	// loads a single deck
	public Deck load(String fileName) {
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			Deck deck = (Deck) is.readObject();
			is.close();
			return deck;
		} catch (IOException ioe) {
			return null;
		} catch (ClassNotFoundException cnfe) {
			return null;
		}
	}

	// loads all decks
	public ArrayList<Deck> loadAll() {
		decks = new ArrayList<Deck>();
		for (File f : context.getFilesDir().listFiles()) {
			Deck deck = load(f.getName());
			decks.add(deck);
		}
		Collections.sort(decks);
		return decks;
	}
	
	public boolean hasDeck(Deck deck) {
		loadAll();
		return decks.contains(deck);
	}
	
	public boolean delete(String fileName) {
		return context.deleteFile(fileName);
	}
	
	// nuke
	public void deleteAll() {
		for (File f : context.getFilesDir().listFiles()) {
			f.delete();
		}
		decks = new ArrayList<Deck>();
	}
}
