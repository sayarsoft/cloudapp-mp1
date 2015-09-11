
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];

        Map<String, Integer> map = new HashMap<String, Integer>();
        List<String> lines = new ArrayList<String>();

        // Read input file to a String list
        String currentLine;
        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        while ((currentLine = br.readLine()) != null)
            lines.add(currentLine);
        br.close();

        // Get indices of lines to be processed
        Integer[] line_indexes = getIndexes();

        // Loop to process all necessary lines
        for (int i=0; i < line_indexes.length; i++) {
            // Get the line to be processed
            String line = lines.get(line_indexes[i]);

            // Tokenize the line
            StringTokenizer st = new StringTokenizer(line, delimiters);

            // Loop over tokens
            while (st.hasMoreTokens()) {
                // Make the token lowercase and trim whitespace
                String token = st.nextToken().toLowerCase().trim();

                // Not process common tokens
                if (Arrays.asList(stopWordsArray).contains(token))
                    continue;

                // Put the token on the hashmap, increase frequency if the token
                // is already on the hashmap
                if (map.containsKey(token))
                    map.put(token, map.get(token) + 1);
                else
                    map.put(token, 1);
            }
        }

        class Word_Count {
            String word;
            Integer count;

            public Word_Count(String word, Integer count) {
                this.word = word;
                this.count = count;
            }
        }

        // Create a list of Word_Count to sort
        List<Word_Count> list = new ArrayList<Word_Count>();

        // Fill Word_Count list with the content of hashmap
        for (Map.Entry<String, Integer> entry : map.entrySet())
            list.add(new Word_Count(entry.getKey(), entry.getValue()));

        // Sort the list with a special comparator
        Collections.sort(list, new Comparator<Word_Count>() {
            public int compare(Word_Count i1, Word_Count i2) {
                int compare = i2.count - i1.count;
                return (compare == 0) ? i1.word.compareTo(i2.word) : compare;
            }
        });

        // Get the most frequent words
        for (int i=0; i < ret.length; i++)
            ret[i] = list.get(i).word;

        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
