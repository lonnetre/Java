public class Test_L {

    public static void main(String[] args) {

        Book_L[] books = new Book_L[] {
                new Book_L("Lord of the Rings: The Return of the King", "J. R. R. Tolkien", 1955),
                new Book_L("The Cafe on the Edge of the World", "John Strelecky", 2003),
                new Book_L("1984", "George Orwell", 1949)
        };

        Bookshelf_L shelf = new Bookshelf_L(books);
        shelf.printBooks();

        String search = "the cafe on the edge of the world";
        System.out.println("Contains " + search + ": " + shelf.containsBook(search));

        search = "ABC";
        System.out.println("Contains " + search + ": " + shelf.containsBook(search));

        shelf.addBook(new Book_L("The Name of the Wind", "Patrick Rothfuss", 2007));
        shelf.printBooks();
    }
}
