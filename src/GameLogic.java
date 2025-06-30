/*
 * GameLogic
 * 2/8/25
*/

import java.util.*;

public class GameLogic {

    private ArrayList<Player> players; // holds the players
    private Deck deck; // instance of deck to get random cards
    private int currentPlayerIndex; // flag to track the current player
    public ArrayList<String> pairs; // list of pairs

    Scanner scan = new Scanner(System.in);
    
    private ArrayList<String> aiNames = new ArrayList<>(
        Arrays.asList("Charlie", "Max", "Bella",
        "Luna", "Oliver", "Milo", "Daisy", "Coco",
        "Rocky", "Zoe", "Buddy", "Riley","Gizmo",
        "Maggie", "Sophie", "Jack", "Chloe", "Teddy"));

    public GameLogic() {
        this.players = new ArrayList<>();
        this.deck = new Deck();
        this.currentPlayerIndex = 0; // start at 0
        initialize();
    }

    private void initialize(){
        int playersNum = 0;

        typeAnim("Welcome to Go Fish!\n", 40);
        typeAnim("How many players are playing? ", 40);

        while (true) {
            try {
                playersNum = Integer.parseInt(scan.nextLine());
                if (playersNum >= 1 && playersNum < 6) { // 1-5 players
                    break; // if the number of players is between 2  and 5 break the check loop
                } else {
                    typeAnim("Please enter a valid number of players (2-5): ", 40);
                }

            } catch (NumberFormatException e) {
                typeAnim("Invalid input. Please enter a number: ", 40);
            }
        }

        for (int i = 0; i < playersNum; i++) {
            typeAnim("What is player " + (i+1) + "'s' name? ", 40);
            String playerName = scan.nextLine().trim();
            players.add(new Player(playerName));
        }

        // add an AI player if only one human player
        if (playersNum == 1) {
            typeAnim("How many AI players would you like to play against? ", 40);
            int AIplayers = 0;
            while(true){
                try {
                    AIplayers = Integer.parseInt(scan.nextLine());
                    if (AIplayers > 0 && AIplayers < 6) { // 1 - 5
                        break;
                    }else {
                        typeAnim("Please enter a valid number of players (1-5): ", 40);
                    }
                    
                } catch (NumberFormatException e) {
                    typeAnim("Invalid input. Please enter a number: ", 40);
                }
            }
            Random rand = new Random();
            // add the specified number of AI players
            for (int i = 0; i < AIplayers; i++) {
                String aiName = aiNames.get(rand.nextInt(aiNames.size()));

                players.add(new AIPlayer(aiName));
                aiNames.remove(aiName); // delete the name used so it cant be used for the next players
                typeAnim("AI Player " + (i + 1) + " is named " + aiName + ".\n", 40);
            }
        }

        System.out.println(); // space

        // deal 7 cards to each player
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(deck); // grab from the deck and add to hand
            }    
        } 

        // check for pairs in each player's hand
        for (Player player : players) {
            pairProcess(player);
        }

        // start the game
        startGame();
    }


    public void pairProcess(Player player) {
        ArrayList<Card> hand = player.getHand(); // reference each player's hand
        ArrayList<String> ranksToRemove = new ArrayList<>(); // to track ranks that form pairs
        ArrayList<String> checkedRanks = new ArrayList<>(); // to track ranks that have been checked

        // check each card in the hand
        for (int i = 0; i < hand.size(); i++) {
            String rank = hand.get(i).getRank(); // get rank
            int count = 1; // track how many of that rank are present

            // check if the rank has already been checked
            if (checkedRanks.contains(rank)) {
                continue; // if so skip iteration
            }

            // count the number of cards with the same rank
            for (int j = i + 1; j < hand.size(); j++) {
                if (hand.get(j).getRank().equals(rank)) {
                    count++; // increment the count if a card with the same rank is found
                }
            }

            // if there are at least two of this rank, process pairs
            if (count >= 2) {
                // System.out.println(player.getName() + " has a pair of " + rank + "!");
                
                player.pairs.add(rank); // add the rank to the pairs list
                player.pair++; // increment the pair counter

                // add the rank to the removal list
                ranksToRemove.add(rank);
            }

            // add the rank to the checked ranks list to avoid rechecking it again
            checkedRanks.add(rank);
        }

        // remove pairs from hand
        for (String rank : ranksToRemove) {
            int toRemove = 2; // two cards per pair
            for (int i = hand.size() - 1; i >= 0 && toRemove > 0; i--) {
            // iterate backwards to avoid issues modifying the list while iterating over it
                if (hand.get(i).getRank().equals(rank)) { // for each rank remove two cards (the pair)
                    hand.remove(i); 
                    toRemove--;
                }
            }
        }
    }
    
    public void startGame(){
        do {
            // reference current player
            Player currentPlayer = players.get(currentPlayerIndex);
            
            // display the current player's info
            displayCurrentPlayerInfo(currentPlayer);

            // process the current player's request
            if(currentPlayer instanceof AIPlayer){ // if the current player is an instance of aiPLayer handle the turn different from the human
                aiTurn((AIPlayer) currentPlayer);
            } else {
                processRequest(currentPlayer);
            }

            // print newline for better readability
            System.out.println();

            // move to the next player
            nextPlayer();
        } while(!isGameOver(players));
    }

    public void processRequest(Player currentPlayer){
        while(true){
            Random rand = new Random();

            // prompt user for their request 
            typeAnim(currentPlayer.getName() + ", ask for a card (e.g., '" + players.get(rand.nextInt(players.size())).getName() + " do you have a " + deck.ranks[rand.nextInt(deck.ranks.length)] +"'): ", 40);
            String request = scan.nextLine().trim(); // read players request
            
            // Handle empty input
            if (request.isEmpty()) {
                typeAnim("You must enter a request. Please try again.\n", 40);
                continue; // Prompt again for valid request
            }

            // split the players request into parts based on spaces
            String[] parts = request.split(" ");

            // check if the format is valid 
            if (parts.length != 6 ||
                !parts[1].equalsIgnoreCase("Do") ||
                !parts[2].equalsIgnoreCase("you") ||
                !parts[3].equalsIgnoreCase("have") ||
                !parts[4].equalsIgnoreCase("a")){
                typeAnim("Invalid request format. Please use: '[Opponent Name] do you have a [Card Rank].'\n", 40);
                continue; // Go back to the start of the loop to ask for input again
            }

            String requestedRank = parts[5]; // The requested rank
            String opponentName = parts[0]; // The name of the player being asked

            // find the opp player
            Player opponent = null;
            for (Player player : players) {
                if (player.getName().equalsIgnoreCase(opponentName)) {
                    opponent = player; // set opponent based on the name found
                    break;
                }
            }

            // handle opponent not found
            if (opponent == null) {
                typeAnim("Opponent not found. Please specify a valid player.\n", 40);
                continue; // prompt again for valid request
            }

            // handle player asking themselves
            if (opponent.getName().equalsIgnoreCase(currentPlayer.getName())) {
                typeAnim("Cannot ask for a card from yourself. Please try again.\n", 40); 
                continue; // prompt again for valid request
            }

            // check if the opponent has the requested card 
            if (opponent.hasCard(requestedRank)) { 
                Card card = opponent.removeCard(requestedRank); // remove from the opponent
                currentPlayer.addCard(card); // and add it to the current player's hand
                typeAnim(currentPlayer.getName() + " received a " + requestedRank + " from " + opponent.getName() + ".\n", 40);
            } else {
                typeAnim(opponent.getName() + " does not have any " + requestedRank + ". Drawing from the deck...\n", 40);
                currentPlayer.drawCard(deck); // fish random card from deck
            }

            // process the pairs
            pairProcess(currentPlayer);

            break;  // Exit the loop after processing a valid request   
        }
    }

    public void aiTurn(AIPlayer aiPlayer){
        Random rand = new Random();

        // Create a delay to simulate thinking
        try {
            Thread.sleep(rand.nextInt(500) + 500); // delay between 500 and 1000 miliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        /* Randomly decide who the AI will ask */

        Player opponent;

        // Decide who to ask based on a probability (50% chance to ask the human)
        if (rand.nextInt(100) < 50) { // 50% chance
            opponent = players.get(0); // select the human whos always going to be the first player
        } else {
            List<Player> aiPlayers = new ArrayList<>();
            for (Player player : players) {
                // check if the player is an instance of AIplayer
                // ensure we only select AI players
                if (player instanceof AIPlayer && !player.equals(aiPlayer)) {
                    aiPlayers.add(player); // add all ai players excluding itself
                }
            }
            // Randomly pick an AI opponent
            if (!aiPlayers.isEmpty()) {
                opponent = aiPlayers.get(rand.nextInt(aiPlayers.size())); // random ai player from the list
            } else {
                opponent = players.get(0); // fallback to human if no other AI exists
            }
        }

        // Get the rank that the ai wants to ask for
        String rankToAsk = aiPlayer.askForCard();

        // AI's dialogue before asking
        typeAnim(aiPlayer.getName() + " is thinking...\n", 40);
        try {
            Thread.sleep(rand.nextInt(1500) + 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // Array of varied asking prompts
        String[] askingPrompts = {
            opponent.getName() + ", do you have a " + rankToAsk + "?",
            "Hey " + opponent.getName() + ", any chance you have a " + rankToAsk + "?",
            opponent.getName() + ", can I get a " + rankToAsk + " from you?",
            "What do you say, " + opponent.getName() + "? Do you have a " + rankToAsk + "?",
            opponent.getName() + ", I'm looking for a " + rankToAsk + ". Do you have one?"
        };

        // AI asks the selected opponent for the card
        typeAnim(aiPlayer.getName() + ": " + askingPrompts[rand.nextInt(askingPrompts.length)] + "\n", 60);

        // process the opponent's response
        if (opponent.hasCard(rankToAsk)) {
            Card card = opponent.removeCard(rankToAsk); // remove the card from the opponent's hand
            aiPlayer.addCard(card); // add the card to the AI's hand
            typeAnim(opponent.getName() + " has a " + rankToAsk + "! " + aiPlayer.getName() + " received it.\n", 40);
        } else {
            // Vary the output with personality
            String[] responses = {
                opponent.getName() + " does not have any " + rankToAsk + ". " + aiPlayer.getName() + " says 'Go Fish!'",
                opponent.getName() + " has nothing. Time to go fishing!",
                opponent.getName() + " has no " + rankToAsk + ". " + aiPlayer.getName() + " went fishing..."
            };
            typeAnim(responses[rand.nextInt(responses.length)] + "\n", 40);
            aiPlayer.drawCard(deck);
        }

        // check for pairs after the turn
        pairProcess(aiPlayer); 
    }

    public void nextPlayer(){
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
    }

    public boolean isGameOver(ArrayList<Player> p){
        for (Player player : p) {
            if (player.pair == 4) {
                typeAnim("🎉🎊🎈 Congratulations! 🎈🎊🎉\n", 40);
                typeAnim("🏆 " + player.getName() + " is the Go Fish Champion! 🏆\n", 40);
                typeAnim(player.getName() + " collected 4 pairs and won the game! 🥳\n", 40);
                return true;
            }
        }
        return false;
    }

    public void displayCurrentPlayerInfo(Player currentPlayer) {
        typeAnim("===== It's " + currentPlayer.getName() + "'s Turn =====\n", 40);
        typeAnim("\n" + currentPlayer.getName() + ", you have the following:\n", 40);
        typeAnim(" - Hand:\n", 40);
        for (Card card : currentPlayer.getHand()) {
            typeAnim("  -  " + card + "\n", 20);
        }
        typeAnim(" - Pairs:\n" + toString(currentPlayer.pairs) + "\n", 40);
        typeAnim("================================\n\n", 20); 
    }
   
    public void typeAnim(String message, int delay) {

        for (char ch : message.toCharArray()) {
            System.out.print(ch);
            try {
                Thread.sleep(delay); // delay between characters
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String toString(ArrayList<String> pairs) {
        String result = "";
        for (String string : pairs) {
            result += "  - " + string + "\n";
        }
        return result; 
    }

}
