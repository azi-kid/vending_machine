package payments;

public class CoinAcceptor implements PaymentMethod{
    private int amount;

    public CoinAcceptor(int amount) {
        this.amount = amount;
    }

    @Override
    public int getAvailableBalance() {
        return amount;
    }

    @Override
    public void addFunds(int amount) {
        this.amount += amount;
    }

    @Override
    public boolean charge(int amount) {
        if(this.amount >= amount){
            this.amount -= amount;
            return true;
        }
        return false;
    }
}
