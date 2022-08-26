import java.util.Scanner;
import java.io.File;
import java.util.HashSet;
import java.util.ArrayList;

public class WordleGame
{
    //word lists
    HashSet<String> allowedGuesses;
    ArrayList<String> possibleAnswers;

    //info about the current game
    String answer;
    int guessesRemaining;

    public WordleGame()
    {
        allowedGuesses = new HashSet<String>();
        possibleAnswers = new ArrayList<String>(2500);
        try
        {
            //load the words for guesses and answers from text files
            Scanner s = new Scanner(new File("wordleWords.txt"));
            while (s.hasNext())
            {
                String word = s.nextLine();
                allowedGuesses.add(word);
            }
            s = new Scanner(new File("wordleAnswers.txt"));
            while (s.hasNext())
            {
                String word = s.nextLine();
                int len = word.length();
                //possibleAnswers is an arraylist of all possible answers taken from the txt file. 
                //ex: ["match", "bangs", "adieu"]
                possibleAnswers.add(word.substring(len - 5).toLowerCase());
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            allowedGuesses = null;
            possibleAnswers = null;
            return;
        }
    }
    //returns the number of guesses needed to get the word
    //or -1 if the word was never guessed
    public int playGame(WordlePlayer player)
    {
        guessesRemaining = 6;
        answer = possibleAnswers.get((int)(Math.random() * possibleAnswers.size()));
        System.out.println("Answer is: " + answer);
        int [] results = null;
        while (guessesRemaining > 0)
        {
            //get the guess from the player, and its results
            String guess = player.getGuess(results, guessesRemaining);
            results = makeGuess(guess);
            if (results == null)
            {
                System.out.println("Invalid Guess");
            }
            else if (guess.equals(answer))  //guess was correct
            {
                return 6 - guessesRemaining;
            }
        }
        //never got the answer
        System.out.println("The word was: " + answer);
        return -1;
    }

    //this method evaluates the performance of the AI wordle player
    //by measurng how many guesses it takes to guess each possible answer in the possible answers list
    //than averages the guesses by dividing totalGuesses/all answers 
    public static double evaluatePlayer()
    {   
        int totalGuesses = 0;
        WordleGame game = new WordleGame();
        int i = 0;
        //loops through the game's possible Answers 
        for(String answer: game.possibleAnswers)
        {
            int guesses = 1; 
            int[] results = null;
            AIWordlePlayer player = new AIWordlePlayer();
            //keep guessing untill the AI gets the right answer 
            while(true)
            {
                String guess = player.getGuess(results, 0);
                if(guess.equals(answer))
                {
                    break;
                }
                results = AIWordlePlayer.makeResult(guess, answer);
                guesses ++;
            }
            totalGuesses += guesses;
            i++;
            System.out.println("It took " + guesses +" guesses to get " + answer + " " + i + "/" + game.possibleAnswers.size());
        }
        double average = (double)totalGuesses/game.possibleAnswers.size();
        System.out.println("Average # of guesses required: ")
        return average;
    }

    //returns an int array that corresponds to the given letter
    //0 is letter not in word
    //1 is letter in word, but not at correct spot
    //2 is letter in word and at correct spot
    //null means a guess is invalid
    private int [] makeGuess(String guess)
    {
        if (guess.length() != 5 || !allowedGuesses.contains(guess))
        {
            return null;
        }
        int [] ret = new int [5];
        for (int i = 0; i < ret.length; i++)
        {
            if (answer.charAt(i) == guess.charAt(i))
            {
                ret[i] = 2;
            }
            else if (answer.indexOf(guess.charAt(i)) != -1)
            {
                ret[i] = 1;
            }
            else 
            {
                ret[i] = 0;
            }
        }
        guessesRemaining -= 1;
        return ret; 
    }
    public static void main(String [] args)
    {
        //an example using a human player
        WordleGame wg = new WordleGame();  //make the game
        //WordlePlayer player = new HumanWordlePlayer();  //make the player
        WordlePlayer player = new AIWordlePlayer();
        int score = wg.playGame(player);  //play the game
        if (score == -1)  //evaluate the score
        {
            System.out.println("You failed...");
        }
        else 
        {
            System.out.println("Congratulations, you got the word in " + score + " guesses"); 
        }
        //evaluates AI: for me it returns around 3.6769164140320485 attempts to correctly guess the word on average 
        double average = evaluatePlayer();
        System.out.println(average);
    }
}