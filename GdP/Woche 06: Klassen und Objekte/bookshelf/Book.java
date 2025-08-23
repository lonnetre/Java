public class Book_L {
    String title;
    String author;
    int year;

    Book_L(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    void printInfo() {
        System.out.println("'" + this.title + "' (" + this.year + ") by " + this.author);
    }
}
