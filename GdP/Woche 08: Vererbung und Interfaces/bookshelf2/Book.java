package bookshelf2;

public class Book_L extends Publication_L {

    private String author;

    Book_L(String title, String author, int year) {
        super(title, year);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "'" + this.title + "' (" + this.year + ") by " + this.author;
    }
}
