package model;

import java.io.Serializable;
import java.util.Calendar;

public class Card implements Comparable<Card>, Serializable {
	
	private static final long serialVersionUID = 1L;
	private String front;
	private Object back;
	private int right;
	private int wrong;
	private Calendar cal;
	
	public Card(String front, Object back) {
		this.front = front;
		this.back = back;
		this.cal = Calendar.getInstance();
	}
	
	public String getFront() { return front; }
	public Object getBack() { return back; }
	public Calendar getCal() { return cal; }
	public float getStrength() { return (float) right / (right + wrong); }
	
	public void setFront(String front) { this.front = front; }
	public void setBack(Object back) { this.back = back; }
	
	public void incrementRight() { right++; }
	public void incrementWrong() { wrong++; }
	
	public String toString() { return front; }
	public int compareTo(Card card) { return cal.compareTo(card.getCal()); }   
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Card)) return false;
		Card other = (Card)o;
		return front.equalsIgnoreCase(other.getFront());
	}
}
