package util;

import java.util.Comparator;

import model.Card;

public class StrengthComparator implements Comparator<Card> {
	
	public int compare(Card a, Card b) {
		float aStrength = a.getStrength(), bStrength = b.getStrength();
		if (aStrength == bStrength) return 0;
		return aStrength < bStrength ? -1 : 1;
	}

}
