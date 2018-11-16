package src.MatadorGame;

public class Player {

    private String name;
    private BankAccount account;


    public Player(String name) {

        this.name = name;
        this.account = new BankAccount(0);

    }

    public String getName() {
        return this.name;
    }


    public BankAccount getAccount() {
        return account;
    }
}