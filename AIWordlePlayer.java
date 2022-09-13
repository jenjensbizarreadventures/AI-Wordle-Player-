import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class AIWordlePlayer extends WordlePlayer
{
    ArrayList<String> allowedGuesses;
    ArrayList<String> allGuesses;
    String previousGuess; 

    public AIWordlePlayer()
    {
        allowedGuesses = new ArrayList<String>();
        allGuesses = new ArrayList<String>();

        previousGuess = null;
        try
        {
            //load the words for guesses from text files
            Scanner s = new Scanner(new File("wordleWords.txt"));
            while (s.hasNext())
            {
                String word = s.nextLine();
                allGuesses.add(word);
            }
            s = new Scanner(new File("wordleAnswers.txt"));
            while (s.hasNext())
            {
                String line = s.nextLine();
                int i = line.lastIndexOf(" ");
                String word = line.substring(i + 1).toLowerCase();
                allowedGuesses.add(word);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public static int [] makeResult(String guess, String answer)
    {
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
        return ret; 
    }
    public static int convert(int [] result)
    {
        int i = 0; 
        int pow = 1; 
        for(int x: result)
        {
            i += pow * x;
            pow *= 3;
        }
        return i;
    }
    //cited in paragraph 
    public double getScore(String guess)
    {   
        int [] counts = new int[243];
        for (String answer: allowedGuesses)
        {   
            int i = convert(makeResult(guess, answer));
            counts[i] += 1; 

        }
        double score = 0;
        for(int i = 0; i < 243; i++)
        {
            double probability = (double)counts[i]/allowedGuesses.size();
            if(probability != 0)
            {
                score -= probability * Math.log(probability);
            }
        }
        return score;
    }
    public String getGuess(int [] previousResults, int guessesRemaining)
    {   
        if (previousResults == null)
        {   
            //first guess of the game 
            previousGuess = "aesir";
            return "aesir";
        }
        else 
        {
            int result1 = convert(previousResults);
            for (int i = allowedGuesses.size()-1; i >= 0; i--)
            {
                String answers = allowedGuesses.get(i);
                int result2 = convert(makeResult(previousGuess, answers));
                //if a potential answer does not match the pattern requirement calculated from previous results, delete it. 
                if(result1 != result2)
                {
                    allowedGuesses.remove(i);
                }
            }
            if(allowedGuesses.size() == 1)
            {
                return allowedGuesses.get(0);
            }

            String bestGuess = null; 
            double bestScore = -1; 
            
            for(String guess: allGuesses)
            {
                double score = getScore(guess);
                if(score > bestScore)
                {
                    bestScore = score; 
                    bestGuess = guess; 
                }
            }
            previousGuess = bestGuess;
            return bestGuess;
        }
    }
}
