import java.util.*;

public class Messenger_L {

    /** Stores all messages that have been posted */
    private static final List<String> messages = new ArrayList<>();

    /** Stores the indices of messages in the {@link #messages}-list by the tags that they contain */
    private static final Map<String, Set<Integer>> messagesByTag = new HashMap<>();


    /**
     * Checks if the message is surrounded by quotation marks and, if so, removes them.
     * @param message a message that is possibly surrounded by quotation marks
     * @return the message that is stripped of the quotation marks. If it is not surrounded by
     * a quotation mark at the beginning and end, then the original message is returned.
     */
    private static String stripQuotationMarksIfAny(String message) {
        if (message.length() >= 2 && message.startsWith("\"") && message.endsWith("\"")) {
            return message.substring(1, message.length() - 1);
        }
        return message;
    }


    /**
     * Extracts the tags from the given message, i.e., the words that are prefixed with a '#'
     * @param message a text which possible contains one or multiple tags
     * @return a set of extracted tags from the message
     */
    private static Set<String> extractTags(String message) {
        // Save the tags to a set to remove duplicates
        Set<String> tags = new HashSet<String>();

        // Split the text into individual words by splitting at empty spaces
        String[] words = message.split(" ");

        // Find tags by searching for words that start with '#'
        for (String word : words) {
            if (word.length() > 1 && word.charAt(0) == '#') {
                String newTag = word.substring(1, word.length()).toLowerCase();
                tags.add(newTag);
            }
        }

        return tags;
    }


    /**
     * Saves the message to the system ({@link #messages}) and indexes the message by the tags that it contains
     * @param message a text that possibly contains tags
     */
    private static void saveMessage(String message) {
        messages.add(message);
        int messageIndex = messages.size() - 1;

        Set<String> tags = extractTags(message);

        // Remember this message for every of its tags
        for (String tag : tags) {
            Set<Integer> referencedMessages;

            if (messagesByTag.containsKey(tag)) {
                referencedMessages = messagesByTag.get(tag);
            } else {
                referencedMessages = new HashSet<>();
                messagesByTag.put(tag, referencedMessages);
            }

            referencedMessages.add(messageIndex);
        }
    }


    /**
     * Prints all text messages that contain the exact given tag
     * @param tag a tag to filter all messages
     */
    private static void printMessagesOfTag(String tag) {
        if (messagesByTag.containsKey(tag)) {
            Set<Integer> referencedMessages = messagesByTag.get(tag);

            for (Integer messageIndex : referencedMessages) {
                System.out.println("> " + messages.get(messageIndex));
            }
        }
    }


    /**
     * Runs a command-line interface for the user to interact with the messenger
     */
    public static void runCommandLineInterface() {
        Scanner scanner = new Scanner(System.in);

        boolean quit = false;
        while (!quit) {
            String userInput = scanner.nextLine();

            if (userInput.startsWith("post ")) {
                String message = userInput.substring(5);
                message = Messenger_L.stripQuotationMarksIfAny(message);

                if (message.length() <= 150) {
                    Messenger_L.saveMessage(message);
                    System.out.println("> " + message);
                } else {
                    System.err.println("Request denied: The message is too long.");
                }

            } else if (userInput.startsWith("findTag #")) {
                String tag = userInput.substring(9);
                tag = tag.split(" ")[0].toLowerCase();
                Messenger_L.printMessagesOfTag(tag);

            } else if (userInput.equals("exit")) {
                quit = true;

            } else {
                System.out.println("Invalid command");
            }
        }
    }


    public static void main(String[] args) {
        Messenger_L.runCommandLineInterface();
    }
}
