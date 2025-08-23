package bookshelf2;

public abstract class Publication_L {
    protected String title;
    protected int year;

    public Publication_L(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void printInfo() {
        System.out.println(this.toString());
    }

    @Override
    public abstract String toString();
}
