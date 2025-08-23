package bookshelf2;

public class Test_L {

    public static void main(String[] args) {

        Publication_L[] publications = new Publication_L[] {
                new Book_L("Lord of the Rings: The Return of the King", "J. R. R. Tolkien", 1955),
                new Magazine_L("iX - Energie sparen in der IT", 2022, 9),
                new Book_L("The Cafe on the Edge of the World", "John Strelecky", 2003),
                new Magazine_L("Der Spiegel - Rette sich, wer kann", 2022, 45),
                new Book_L("1984", "George Orwell", 1949)
        };

        Shelf_L shelf = new Shelf_L(publications);
        shelf.printContent();

        String search = "ix - energie sparen in der it";
        System.out.println("Contains " + search + ": " + shelf.contains(search));

        search = "ABC";
        System.out.println("Contains " + search + ": " + shelf.contains(search));

        shelf.add(new Book_L("The Name of the Wind", "Patrick Rothfuss", 2007));
        shelf.printContent();
    }
}
