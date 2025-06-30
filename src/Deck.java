/*
 * Deck - Simulates a Deck without having to create one
 * 2/8/25
*/

import java.util.*;

public class Deck {

    private String[] suits = {"Diamonds", "Hearts", "Clubs", "Spades"}; 
    public String[] ranks = {"Two", "Three", "Four", "Five", "Six", 
                              "Seven", "Eight", "Nine", "Ten", 
                              "Jack", "Queen", "King", "Ace", "Joker"};
    Random rand = new Random();

    public Card drawRandomCard(){
        String suit = suits[rand.nextInt(suits.length)];
        String rank = ranks[rand.nextInt(ranks.length)];
        return new Card(suit, rank); // directly generate card
    }
}
