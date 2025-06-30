/*
 * GoFish - Runner
 * 2/8/25
*/

import java.util.*;

public class GoFish {
    public static void main(String[] args) throws Exception {
        String answer;
        Scanner scan = new Scanner(System.in);
        GameLogic gameLogic;

        do{
            gameLogic = new GameLogic();

            gameLogic.typeAnim("Do you want to play again? ", 40);
            answer = scan.nextLine().trim();
        } while(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("true"));
        
        gameLogic.typeAnim("Thank you for playing my Go Fish minigame!", 60);
    }
}
