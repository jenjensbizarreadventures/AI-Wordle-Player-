import java.util.Scanner;

public class HumanWordlePlayer extends WordlePlayer
{
    public String getGuess(int [] previousResults, int guessesRemaining)
    {
        if (previousResults != null)
        {
            for (int i = 0; i < 13; i++)
            {
                System.out.print(" ");
            }
            for (int i = 0; i < previousResults.length; i++)
            {
                if (previousResults[i] == 0)
                {
                    System.out.print('-');
                }
                else if (previousResults[i] == 1)
                {
                    System.out.print('Y');
                }
                else 
                {
                    System.out.print('G');
                }
                
            }
            System.out.println();
        }
        Scanner ui = new Scanner(System.in);
        System.out.println(guessesRemaining + " guesses remaining");
        System.out.print("Enter guess: ");
        String guess = ui.nextLine();
        return guess;
    }
}