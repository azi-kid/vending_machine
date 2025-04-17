package payments;

import payments.PaymentMethod;

public class CreditCard implements PaymentMethod {
    private int cardNumber;
    private int PIN;
    private int balance;

    public CreditCard( int pin, int balance) {
        this.cardNumber = 450065783;
        this.PIN = pin;
        this.balance = balance;
    }


    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
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
