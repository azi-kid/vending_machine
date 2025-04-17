package payments;


public class CreditCard implements PaymentMethod {
    private String cardNumber;
    private final int PIN;
    private int balance;

    public CreditCard(String cardNumber) {
        this.cardNumber = cardNumber;
        this.PIN = 7575;
        this.balance = 150;
    }


    public int getPIN() {
        return PIN;
    }


    @Override
    public int getAvailableBalance() {
        return balance;
    }

    @Override
    public void addFunds(int amount) {
        this.balance = this.getAvailableBalance() + amount;
    }

    @Override
    public boolean charge(int amount) {
        if(this.balance >= amount){
            this.balance -= amount;
            return true;
        }
        return false;
    }
}
