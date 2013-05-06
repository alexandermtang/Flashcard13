package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import util.FrontComparator;
import util.StrengthComparator;

public class Deck implements Comparable<Deck>, Serializable {
	
	private static final long serialVersionUID = 1L;
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
	
	public Card getCard(String front) {
		return cards.get(cards.indexOf(new Card(front, "")));
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
	
	public int compareTo(Deck deck) {
		return name.compareToIgnoreCase(deck.getName());
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof Deck)) return false;
		Deck other = (Deck)o;
		return name.equalsIgnoreCase(other.getName());
	}
	
	public String toString() {
		return name + "\n" + size() + " card" + (size() == 1 ? "" : "s");
	}
}
