package bookshelf2;

public class Magazine_L extends Publication_L {

    private int edition;

    public Magazine_L(String title, int year, int edition) {
        super(title, year);
        this.edition = edition;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    @Override
    public String toString() {
        return "'" + this.title + "' (" + this.year + ", #" + this.edition + ")";
    }
}
