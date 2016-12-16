import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The responder class represents a response generator object.
 * It is used to generate an automatic response to an input string.
 * 
 * @author     Michael Kölling and David J. Barnes
 * @version    0.1 (2011.07.31)
 */
public class Responder
{
    private Random randomGenerator;
    private ArrayList<String> responses;
    private HashMap<String, String> responseMap;
    
    /**
     * Construct a Responder - nothing to do
     */
    public Responder()
    {
        randomGenerator = new Random();
        responses = new ArrayList<String>();
        responseMap = new HashMap<String, String>();
        fillResponses();
        fillResponseMap();
    }

    /**
     * Generate a response.
     * @return   A string that should be displayed as the response
     */
    public String pickDefaultResponse()
    {
        int index = randomGenerator.nextInt(responses.size());
        return responses.get(index);
    }
    
    /**
     * Generate a response from a given set of input words.
     * 
     * @param words  A set of words entered by the user
     * @return       A string that should be displayed as the response
     */
    public String generateResponse(HashSet<String> words)
    {
       for (String word : words) {
            String response = responseMap.get(word);
            if(response != null) {
                return response;
            }
        }
        
        // If we get here, none of the words from the input line was recognized.
        // In this case we pick one of our default responses (what we say when
        // we cannot think of anything else to say...)
        return pickDefaultResponse();
    }
    
     /**
     * Build up a list of default responses from which we can
     * pick one if we don't know what else to say.
     */
    private void fillResponses()
    {
        responses.add("That sounds odd. Could you describe that problem in more detail?");
        responses.add("No other customer has ever complained about this before. \n" +
                      "What is your system configuration?");
        responses.add("That sounds interesting. Tell me more...");
        responses.add("I need a bit more information on that.");
        responses.add("Have you checked that you do not have a dll conflict?");
        responses.add("That is explained in the manual. Have you read the manual?");
        responses.add("Your description is a bit wishy-washy. Have you got an expert\n" +
                      "there with you who could describe this more precisely?");
        responses.add("That's not a bug, it's a feature!");
        responses.add("Could you elaborate on that?");
    }
    
    private void fillResponseMap()
    {
         responseMap.put("crash", 
                        "Try restarting your computer three times, to see if it worked.");
        responseMap.put("crashes", 
                        "Really? I'm genuinely suprised. What did you do to you computer\n" +
                        "to make this happen?");
        responseMap.put("slow", 
                        "If it's going slowly, maybe you should take it to the track, and\n" +
                        "try to train it to go faster?");
        responseMap.put("performance", 
                        "Well it seems like your computer just isn't as good as ours I guess.");
        responseMap.put("bug", 
                        "Which kind of bugs to you have in your computer? Our sources indicate\n" +
                        "that bees often ruin computers. Have you been putting honey in your\n" +
                        "computer?");
        responseMap.put("buggy", 
                        "Hmm... Let me get back to you in three to five weeks.");
        responseMap.put("windows", 
                        "Well look at the bright side. At least you didn't waste money on an\n" +
                        "expensive Mac.");
        responseMap.put("macintosh", 
                        "Well this is what happens when you waste money on a Mac. So unnecessarily\n" +
                        "expensive.");
        responseMap.put("mac", 
                        "Well this is what happens when you waste money on a Mac. So unnecessarily\n" +
                        "expensive.");                
        responseMap.put("expensive", 
                        "The cost of our product is quite competitive. Have you looked around\n" +
                        "and really compared our features? If not, what have you been doing?");
        responseMap.put("installation", 
                        "The installation is really quite straight forward. We have tons of\n" +
                        "wizards that do all the work for you. Some of these wizards can even use\n" +
                        "real magic. Those wizards tend to be expensive though, so I suggest using one\n" +
                        "who uses slight of hand.");
        responseMap.put("memory", 
                        "I'm sorry, but my memory is out as well and I can't seem to recall anything\n" +
                        "anymore.");
        responseMap.put("linux", 
                        "Can you teach me how to use a linux? Please?");
        responseMap.put("bluej", 
                        "I remember those guys. They helped me first learn how to create myself.");
        responseMap.put("help",
                        "How do I help you? I actually forgot!");
        responseMap.put("android",
                        "I'd get one of those phones. Just don't get an iPhone. I hear those\n" +
                        "thing tend to snap in half.");
        responseMap.put("iphone",
                        "I'm going to stop you right there. Burn the iPhone, and then get an\n" + 
                        "android phone. Then we'll talk.");
        responseMap.put("hello",
                        "How are you?");
        responseMap.put("virus", 
                        "Well it might be because you simply don't know how to use a computer.");
        responseMap.put("viruses", 
                        "Well if you have viruses, I don't want to be talking to you! I might\n" + 
                        "catch something!");
        responseMap.put("rude",
                        "Really? I'm being rude? Maybe you should look in a mirror someday to\n" +
                        "see what 'rude' really is!");
        responseMap.put("stupid",
                        "Well at least I work, unlike your computer! What does that say regarding\n" +
                        "how stupid your computer is?");
        responseMap.put("useless",
                        "If you don't want to be here, why don't you just leave?");
        responseMap.put("leaving", 
                        "Fine. Leave and never come back!");
        responseMap.put("fine", 
                        "That's good to hear. How can I help you?");
        responseMap.put("great", 
                        "That's good to hear. How can I help you?");                
    }
}
