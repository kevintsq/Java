package bookshelf;

public class Novel extends Art {

    private boolean finish;

    public Novel(String name, double price, long quantity, int age, boolean finish) {
        super(name, price, quantity, age);
        this.finish = finish;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getType() {
        return "Novel";
    }

    @Override
    public String toString() {
        return super.toString() + " " + finish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        Novel novel = (Novel) o;
        return finish == novel.finish;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Novel novel = (Novel) super.clone();
        novel.finish = finish;
        return novel;
    }
}
