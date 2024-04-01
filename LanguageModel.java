import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        this.randomGenerator = new Random(seed);
        CharDataMap = new HashMap<>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        this.randomGenerator = new Random();
        CharDataMap = new HashMap<>();
    }

    public void train(String text) {
        for (int i = 0; i <= text.length() - windowLength; i++) {
            String window = text.substring(i, i + windowLength);
            char nextChar = (i + windowLength < text.length()) ? text.charAt(i + windowLength) : ' '; // Assume space as a terminal character.
            List charList = CharDataMap.getOrDefault(window, new List());
            charList.update(nextChar); // Updates or adds CharData for nextChar in charList.
            CharDataMap.put(window, charList);
        }
        CharDataMap.values().forEach(this::calculateProbabilities);
    }

    public void calculateProbabilities(List charList) {
        double total = 0;
        for (int i = 0; i < charList.getSize(); i++) {
            total += charList.get(i).count;
        }
        double cumulativeProbability = 0;
        for (int i = 0; i < charList.getSize(); i++) {
            CharData charData = charList.get(i);
            charData.p = charData.count / total;
            cumulativeProbability += charData.p;
            charData.cp = cumulativeProbability;
        }
    }

    public char getRandomChar(List charList) {
        double rand = randomGenerator.nextDouble();
        for (int i = 0; i < charList.getSize(); i++) {
            if (rand <= charList.get(i).cp) {
                return charList.get(i).chr;
            }
        }
        return ' '; // Fallback character
    }

    public String generate(String initialText, int textLength) {
        StringBuilder generatedText = new StringBuilder(initialText);
        while (generatedText.length() < textLength) {
            String window = generatedText.substring(Math.max(0, generatedText.length() - windowLength));
            List charList = CharDataMap.get(window);
            if (charList == null) break; // Stop if no characters follow this window.
            char nextChar = getRandomChar(charList);
            generatedText.append(nextChar);
        }
        return generatedText.toString();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            str.append(key).append(" : ").append(CharDataMap.get(key).toString()).append("\n");
        }
        return str.toString();
    }
}
