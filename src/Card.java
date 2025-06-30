/*
 * Card - Blueprint to create a card
 * 2/8/25
*/

public class Card {

    private String suit;
    private String rank;


    // constructor for card
    public Card(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }


    /* GETTERS */   
    public String getSuit(){
        return suit;
    }

    public String getRank(){
        return rank;
    }

    @Override
    public String toString(){
        return rank + " of " + suit;
    }
}
