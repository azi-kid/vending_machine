import enums.ActionLetter;
import model.*;
import payments.CoinAcceptor;
import payments.CreditCard;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;
    private final CreditCard card;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
        card = new CreditCard(1234, 150);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        print("В автомате доступны:");
        showProducts(products);

        System.out.println("Выберите способ оплаты:");
        System.out.println("1 - монетой");
        System.out.println("2 - кредитной картой");
        int option = Integer.parseInt(fromConsole());

        switch (option){
            case 1:
            coinAcceptor.payWith();
                print("Монет на сумму: " + coinAcceptor.getAmount());
                allowProducts.addAll(getAllowedProducts().toArray());
                chooseAction(allowProducts, coinAcceptor);
                break;
            case 2:
            default:
                card.payWith();
                print("На балансе: " + card.getBalance());
                allowProducts.addAll(getAllowedProducts().toArray());
                chooseAction(allowProducts, card);
        }

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private <T> void chooseAction(UniversalArray<Product> products, T paymentMethod) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");

        try {

            String action = fromConsole().substring(0, 1);

            if ("a".equalsIgnoreCase(action)) {
                addBalance(paymentMethod);
                return;
            }else if ("h".equalsIgnoreCase(action)) {
                isExit = true;
                return;
            }

            boolean isFound = false;
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().getValue().equalsIgnoreCase(action)) {
                    buy(paymentMethod, i);
                    print("Вы купили " + products.get(i).getName());
                    isFound = true;
                    break;
                }
            }

            if(!isFound){
                throw new IllegalArgumentException();
            }

        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попробуйте еще раз.");
            chooseAction(products, paymentMethod);
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private <T> void addBalance(T paymentMethod) {
        if(paymentMethod.equals(card)){
            card.setBalance(card.getBalance() + 10);
            print("Вы пополнили баланс на 10");
            print("На балансе: " + card.getBalance());
        }else if(paymentMethod.equals(coinAcceptor)){
            coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
            print("Вы пополнили баланс на 10");
            print("Монет на сумму: " + coinAcceptor.getAmount());
        }

    }

    private <T> void buy(T paymentMethod, int i) {
        if(paymentMethod.equals(card)){
            card.setBalance(card.getBalance() - products.get(i).getPrice());
        }else if(paymentMethod.equals(coinAcceptor)){
            coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
        }

    }
}
