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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public void payWith() {
        System.out.println("Выбранный способ оплаты — кредитная карта.");
    }


    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }
}
