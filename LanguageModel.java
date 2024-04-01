import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
    // Pseudocode for reading the file and populating the model
    // Assuming an efficient way to read the file character by character
    // and a method to update or add CharData to your CharDataMap
    for (each n-character window in the file) {
        char nextChar = read next character after the window;
        List charList = CharDataMap.getOrDefault(window, new List());
        charList.update(nextChar); // Update or add the nextChar in charList
        calculateProbabilities(charList); // Recalculate probabilities
        CharDataMap.put(window, charList); // Put updated list back into map
    }
}



    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List charList) {
    double totalChars = 0;
    for (int i = 0; i < charList.getSize(); i++) {
        totalChars += charList.get(i).count;
    }
    double cumulativeProbability = 0;
    for (int i = 0; i < charList.getSize(); i++) {
        CharData charData = charList.get(i);
        charData.p = charData.count / totalChars;
        cumulativeProbability += charData.p;
        charData.cp = cumulativeProbability;
    }
}


    double cumulativeProbability = 0;
    for (int i = 0; i < probs.getSize(); i++) {
        CharData charData = probs.get(i);
        charData.p = charData.count / totalChars;
        cumulativeProbability += charData.p;
        charData.cp = cumulativeProbability;
    }
}



    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
    double r = randomGenerator.nextDouble();
    for (int i = 0; i < probs.getSize(); i++) {
        if (r <= probs.get(i).cp) {
            return probs.get(i).chr;
        }
    }
    return probs.get(probs.getSize() - 1).chr; // Fallback to the last character if none matched (shouldn't happen if cp is correct).
}


    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
    StringBuilder output = new StringBuilder(initialText);
    while (output.length() < textLength) {
        String currentWindow = output.substring(Math.max(0, output.length() - windowLength));
        List charList = CharDataMap.get(currentWindow);
        if (charList == null) break; // Stop if no data for the current window
        char nextChar = getRandomChar(charList); // Implement based on cumulative probabilities
        output.append(nextChar);
    }
    return output.toString();
}



    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
    if (args.length < 5) {
        System.out.println("Usage: java LanguageModel <windowLength> <initialText> <textLength> <fixed/random> <fileName>");
        return;
    }

    try {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int textLength = Integer.parseInt(args[2]);
        boolean useFixedSeed = args[3].equalsIgnoreCase("fixed");
        String fileName = args[4];

        LanguageModel lm;
        if (useFixedSeed) {
            // Use a fixed seed for reproducibility (for testing/debugging)
            int seed = 20; // This could be any fixed number
            lm = new LanguageModel(windowLength, seed);
        } else {
            // Use a random seed for different results on each run (for production)
            lm = new LanguageModel(windowLength);
        }

        // Train the model with the specified file
        lm.train(fileName);

        // Generate text using the trained model
        String generatedText = lm.generate(initialText, textLength);

        // Output the generated text
        System.out.println(generatedText);
    } catch (NumberFormatException e) {
        System.out.println("Error: windowLength and textLength must be integers.");
    } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
        e.printStackTrace();
    }
}

}
