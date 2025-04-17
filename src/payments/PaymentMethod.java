package payments;

public interface PaymentMethod {
    int getAvailableBalance();
    void addFunds(int amount);
    boolean charge(int amount);
}
