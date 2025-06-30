/*
 * AIPlayer - BluePrint for AI extending Player
 * 2/8/25
*/

import java.util.*;

public class AIPlayer extends Player{ // extending Player so it has the same methods from the Player class

    Random rand = new Random();

    public AIPlayer(String name){
        super(name); // calls the player constructor to initialize the name field
    }

    public String askForCard(){
        ArrayList<Card> hand = getHand();
        
        // randomly select a rank from the AI's hand to ask for
        
        if (hand.isEmpty()) {
            return null; // No cards to ask about
        }
        
        // choose a random card from the hand
        Card cardToAsk = hand.get(rand.nextInt(hand.size()));
        return cardToAsk.getRank(); // return the rank of the chosen card
    }
}
