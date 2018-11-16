package src.MatadorGame;

public class ChanceCards {

    private String Description;
    private String name;
    private int number;

    public ChanceCards(String Description, String name, int number) {

        this.Description = Description;
        this.name = name;
        this.number = number;

    }


    public String toString() {
        return this.Description + " " + this.name + "\n";
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
