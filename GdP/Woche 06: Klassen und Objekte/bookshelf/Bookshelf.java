public class Bookshelf_L {

    Book_L[] books;

    public Bookshelf_L(Book_L[] books) {
        this.books = books;
    }

    void printBooks() {
        for (int i = 0; i < this.books.length; i++) {
            books[i].printInfo();
        }
    }

    boolean containsBook(String title) {
        title = title.toLowerCase();

        for (int i = 0; i < books.length; i++) {
            Book_L book = books[i];

            if (book.title.toLowerCase().equals(title)) {
                return true;
            }
        }

        return false;
    }

    boolean addBook(Book_L book) {
        if (containsBook(book.title)) {
            return false;
        }

        Book_L[] booksCopy = new Book_L[this.books.length + 1];

        for (int i = 0; i < this.books.length; i++) {
            booksCopy[i] = books[i];
        }

        // Alternatively:
        //Arrays.copyOf(this.books, this.books.length + 1);

        booksCopy[booksCopy.length - 1] = book;
        this.books = booksCopy;

        return true;
    }
}
