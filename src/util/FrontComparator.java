package util;

import java.util.Comparator;

import model.Card;

public class FrontComparator implements Comparator<Card> {
	
	public int compare(Card a, Card b) {
		return a.getFront().compareToIgnoreCase(b.getFront());
	}
}
