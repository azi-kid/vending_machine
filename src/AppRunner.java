import enums.ActionLetter;
import model.*;
import payments.CoinAcceptor;
import payments.CreditCard;
import payments.PaymentMethod;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final PaymentMethod coinAcceptor;
    private final PaymentMethod card;

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
        System.out.println("Выберите способ оплаты:");
        System.out.println("1 - монетой");
        System.out.println("2 - кредитной картой");
        int option = Integer.parseInt(fromConsole());
        if(option == 1){
            System.out.println("Выбранный способ оплаты — Монета");
            while (!isExit) {
                app.startSimulation(app.coinAcceptor);
            }
        }else{
            System.out.println("Выбранный способ оплаты — кредитная карта.");
            while (!isExit) {
                app.startSimulation(app.card);
            }
        }
    }

    private void startSimulation(PaymentMethod paymentMethod) {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        print("В автомате доступны:");
        showProducts(products);
        print("Доступно средств: " + paymentMethod.getAvailableBalance());
        allowProducts.addAll(getAllowedProducts(paymentMethod).toArray());
        chooseAction(allowProducts, paymentMethod);

    }

    private UniversalArray<Product> getAllowedProducts(PaymentMethod paymentMethod) {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (paymentMethod.getAvailableBalance() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products, PaymentMethod paymentMethod) {
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

    private static String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private static void print(String msg) {
        System.out.println(msg);
    }

    private void addBalance(PaymentMethod paymentMethod) {

        paymentMethod.addFunds(10);
        print("Вы пополнили баланс на 10");
        print("На балансе: " + paymentMethod.getAvailableBalance());
    }

    private void buy(PaymentMethod method, int index) {
        Product product = products.get(index);
        if(!method.charge(product.getPrice())){
            System.out.println("Недостаточно средств");
        }
    }
}
