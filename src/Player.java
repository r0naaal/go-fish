/*
 * Player - Blueprint for player
 * 2/8/25
*/

import java.util.ArrayList;

public class Player {

    private String name;
    private ArrayList<Card> hand; // holds the cards during the game
    public int pair; // number of pairs
    public ArrayList<String> pairs; // list of pairs


    public Player(String name){
        this.name = name;
        this.pair = 0;
        this.pairs = new ArrayList<>();
        this.hand = new ArrayList<>(); 
    }

    public void drawCard(Deck deck){ 
        // take deck as instance and get random card from it and add it to the hand
        hand.add(deck.drawRandomCard());
    }

    public boolean hasCard(String rank){
        for (Card card : hand) { // for each card in the player's hand
            if (card.getRank().equalsIgnoreCase(rank)) { // compare ranks ignoring case
                return true;
            }
        }
        return false; // card not found
    }

    public void addCard(Card requestedCard){
        hand.add(requestedCard);
    } 

    public Card removeCard(String requestedRank){
        for (Card card : hand) { // for each card in the player's hand
            if (card.getRank().equalsIgnoreCase(requestedRank)) { // check if they have the requested rank
                hand.remove(card); // if they do, remove it from the player
                return card; // return it to then add it to the asking player
            }
        }
        return null; // card not found
    }

    /* GETTERS */
    public String getName(){
        return name;
    }

    public ArrayList<Card> getHand(){
        return hand;
    }

    @Override
    public String toString() {
        String result = name + "'s hand:\n";
        for (Card card : hand) {
            result += " - " + card + "\n";
        }
        return result;
    }

}
