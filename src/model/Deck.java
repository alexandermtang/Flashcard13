package model;

import java.util.ArrayList;
import java.util.Collections;

import util.FrontComparator;
import util.StrengthComparator;

public class Deck {
	
	private String name;
	private ArrayList<Card> cards;
	
	public Deck(String name) {
		this.name = name;
		cards = new ArrayList<Card>();
	}
	
	public String getName() { return name; }
	public ArrayList<Card> getCards() { return cards; }
	public int size() { return cards.size(); }
	
	public void setName(String name) { this.name = name; }
	
	public boolean hasCard(Card card) { return cards.contains(card); }
	
	public void addCard(Card card) { 
		if (!(cards.contains(card))) cards.add(card);
	} 
	
	public boolean deleteCard(String front) {
		Card temp = new Card(front, null);
		return cards.remove(temp);
	}
	
	public Card searchForCard(String front) {
		Card temp = new Card(front, null);
		int index = cards.indexOf(temp);
		return cards.get(index);
	}
	
	public void sortByCalendar() {
		Collections.sort(cards);
	}
	
	public void sortByFront() {
		Collections.sort(cards, new FrontComparator());
	}
	
	public void sortByStrength() { 
		Collections.sort(cards, new StrengthComparator());
	}

}
