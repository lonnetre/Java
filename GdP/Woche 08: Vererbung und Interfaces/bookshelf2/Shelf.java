package bookshelf2;

public class Shelf_L {

    private Publication_L[] publications;

    public Shelf_L(Publication_L[] publications) {
        this.publications = publications;
    }

    public void printContent() {
        for (int i = 0; i < this.publications.length; i++) {
            publications[i].printInfo();
        }
    }

    public boolean contains(String title) {
        title = title.toLowerCase();

        for (int i = 0; i < publications.length; i++) {
            Publication_L publication = publications[i];

            if (publication.getTitle().toLowerCase().equals(title)) {
                return true;
            }
        }

        return false;
    }

    public boolean add(Publication_L publication) {
        if (contains(publication.getTitle())) {
            return false;
        }

        Publication_L[] publicationsCopy = new Publication_L[this.publications.length + 1];

        for (int i = 0; i < this.publications.length; i++) {
            publicationsCopy[i] = publications[i];
        }

        // Alternatively:
        //Arrays.copyOf(this.publications, this.publications.length + 1);

        publicationsCopy[publicationsCopy.length - 1] = publication;
        this.publications = publicationsCopy;

        return true;
    }
}
