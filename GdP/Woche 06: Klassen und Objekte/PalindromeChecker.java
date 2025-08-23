public class PalindromeChecker_L {

    static void checkForPalindromes(String[] words) {
        boolean[] arePalindromes = new boolean[words.length];

        for (int i = 0; i < words.length; i++) {
            arePalindromes[i] = PalindromeChecker_L.isPalindrome(words[i]);
        }

        PalindromeChecker_L.printPalindromeResults(words, arePalindromes);
    }

    static boolean isPalindrome(String word) {
        word = word.toLowerCase();

        for (int i = 0; i < word.length() / 2; i++) {
            boolean sameLetter = PalindromeChecker_L.sameCharAtFrontAndBack(word, i);
            if (!sameLetter) {
                return false;
            }
        }

        return true;
    }

    static boolean sameCharAtFrontAndBack(String word, int index) {
        return word.charAt(index) == word.charAt(word.length() - 1 - index);
    }

    static void printPalindromeResults(String[] words, boolean[] arePalindromes) {
        System.out.println("These words are palindromes:");
        PalindromeChecker_L.printSelectedWords(words, arePalindromes, true);
        System.out.println();

        System.out.println("These words are not palindromes:");
        PalindromeChecker_L.printSelectedWords(words, arePalindromes, false);
    }

    static void printSelectedWords(String[] words, boolean[] arePalindromes, boolean selector) {
        for (int i = 0; i < arePalindromes.length; i++) {
            boolean isPalindrome = arePalindromes[i];
            if (isPalindrome == selector) {
                System.out.println(words[i]);
            }
        }
    }

    public static void main(String[] args) {
        PalindromeChecker_L.checkForPalindromes(args);
    }
}
