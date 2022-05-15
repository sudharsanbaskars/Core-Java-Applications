package sudharsan.zoho_questions.virtualgiftcardsystem;

import sudharsan.zoho_questions.virtualgiftcardsystem.pojos.Bank;
import sudharsan.zoho_questions.virtualgiftcardsystem.pojos.CustomerAccount;
import sudharsan.zoho_questions.virtualgiftcardsystem.pojos.GiftCard;
import sudharsan.zoho_questions.virtualgiftcardsystem.pojos.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;


public class Application {

    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static void addNewAccount(Bank bankDb) throws IOException { // add a new customer account into the bank

        System.out.print("Enter the Customer Name: ");
        String customerName = input.readLine();

        try {
            System.out.print("Enter the Initial Amount to credit: ");
            long amount = Long.parseLong(input.readLine());
            CustomerAccount account = new CustomerAccount(customerName, amount, bankDb);

            bankDb.addNewCustomer(account);
            System.out.println("Account created Successfully!!");

        } catch (Exception e){
            System.out.println("Please enter a valid input!!");
        }

    }


    public static void displayCustomerLogic(CustomerAccount currCustomerAccount, Bank bankDb){
        while (true){
            System.out.println("Welcome "+currCustomerAccount.getCustomerName()+"("+currCustomerAccount.getCustomerName()+")");
            System.out.println("\t1. Create Gift Card");
            System.out.println("\t2. Top-Up Gift Card");
            System.out.println("\t3. Close Gift Card");
            System.out.println("\t4. Purchase Item");
            System.out.println("\t5. Block cards");
            System.out.println("\t6. Unblock cards");
            System.out.println("\t7. Show all Gift cards");

            System.out.print("Select a option(0 for back): ");

            try {
                int subOption = Integer.parseInt(input.readLine());

                if (subOption == 0) break;
                else if (subOption == 1){ // create gift card
                    createGiftCardLogic(currCustomerAccount, bankDb);

                } else if (subOption == 2){ // top-up gift card
                    purchaseOrTopUp(currCustomerAccount, bankDb, subOption);

                } else if (subOption == 3){ // close gift card
                    closeGiftCardLogic(currCustomerAccount, bankDb);

                } else if (subOption == 4){ // purchase item
                    purchaseOrTopUp(currCustomerAccount, bankDb, subOption);

                } else if (subOption == 5){ // block cards
                    blockCardLogic(currCustomerAccount, bankDb);

                } else if (subOption == 6){ // unblock
                    unBLockLogic(currCustomerAccount, bankDb);

                } else if (subOption == 7){ // show all cards
                    displayAllCards(currCustomerAccount, bankDb);

                } else {
                    System.out.println("Please select a valid option!!!!");
                }

            } catch (Exception e){
                System.out.println("Please select from given option!!");
            }
        }
    }


    public static void displayAllCards(CustomerAccount currCustomerAccount, Bank bankDb) {
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        List<Integer> giftCards = bankDb.getAllGiftCardsForCustomer(currCustomerAccount);
        if (giftCards.isEmpty()){
            System.out.println("No cards at this moment!");

        } else {
            System.out.println("Card No   |     Pin     |    Balance   |   Status     |  BLocked?");
            for (Integer gi : giftCards){
                GiftCard gc = bankDb.getGiftCardByNumber(gi);
                System.out.println(" "+gc.getCardNumber()+"     |    "+gc.getPin()+"     |     "+gc.getGiftCardBalance()+"     |      "+gc.getStatusName()+"     |     "+gc.isBlocked());
            }
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }


    public static void purchaseOrTopUp(CustomerAccount currCustomerAccount, Bank bankDb, int val) {
        List<Integer> giftCards = bankDb.getActiveGiftCardForCustomerButNotBlocked(currCustomerAccount);

        if (giftCards.isEmpty()){
            System.out.println("Seems this customer has no gift cards!!");

        } else {
            while (true) {
                try {
                    System.out.println("Available gift Cards: ");
                    for (int i = 0; i < giftCards.size(); i++) {
                        System.out.println("\t" + (i + 1) + ". Card Number: " + giftCards.get(i));
                    }

                    System.out.print("Enter the Gift Card Number :(0 for back) ");

                    int cardNumber = Integer.parseInt(input.readLine());
                    if (cardNumber == 0) break;
                    else {
                        if (!giftCards.contains(cardNumber) && !bankDb.containsGiftCard(cardNumber)) {
                            System.out.println("Seems the card with this id is not available for you");

                        } else {
                            GiftCard giftCard = bankDb.getGiftCardByNumber(cardNumber);

                            System.out.print("Enter the pin of the gift card: ");
                            int pin = Integer.parseInt(input.readLine());

                            if (giftCard.getPin() != pin) {
                                System.out.println("Seems the pin is mismatching please try again!!!!");

                            } else {
                                if (val == 4) { // purchase
                                    System.out.print("Enter the amount for purchase: ");
                                    long amount = Long.parseLong(input.readLine());

                                    if (amount > giftCard.getGiftCardBalance()) {
                                        System.out.println("Your purchase amount is more than your gift card  balance!!");

                                    } else {
                                        giftCard.debit(amount);
                                        Transaction transaction = new Transaction(giftCard.getCardNumber(), amount);
                                        bankDb.addTransaction(transaction);

                                        System.out.println("Purchase Done!!!!");
                                        break;
                                    }
                                } else if (val == 2){ // top up

                                    System.out.print("Enter the amount to top up: ");
                                    long amount = Long.parseLong(input.readLine());

                                    if (amount > currCustomerAccount.getBalance()) {
                                        System.out.println("Your top up amount is more than your account balance!!");

                                    } else {
                                        currCustomerAccount.debit(amount);
                                        giftCard.credit(amount);

                                        System.out.println("Top-Up Successful!!!");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid input!!!!");
                }
            }
        }
    }


    public static void blockCardLogic(CustomerAccount currCustomerAccount, Bank bankDb) {
        System.out.println("Block gift card!!!");
        List<Integer> giftCards = bankDb.getActiveGiftCardForCustomerButNotBlocked(currCustomerAccount);

        if (giftCards.isEmpty()){
            System.out.println("Seems this customer has no non blocked gift cards!!");

        } else {
            while (true) {
                try {
                    System.out.println("Available gift Cards: ");
                    for (int i = 0; i < giftCards.size(); i++) {
                        System.out.println("\t" + (i + 1) + ". Card Number: " + giftCards.get(i));
                    }

                    System.out.print("Enter the Gift Card Number to block:(0 for back) ");
                    int cardNumber = Integer.parseInt(input.readLine());

                    if (cardNumber  == 0) break;
                    else {
                        if (!giftCards.contains(cardNumber) && !bankDb.containsGiftCard(cardNumber)) {
                            System.out.println("Seems the card with this id is not available");

                        } else {
                            GiftCard giftCard = bankDb.getGiftCardByNumber(cardNumber);

                            System.out.print("Enter the pin of the gift card: ");
                            int pin = Integer.parseInt(input.readLine());

                            if (giftCard.getPin() != pin) {
                                System.out.println("Seems the pin is mismatching please try again!!!!");
                            } else {
                                giftCard.setBlocked(true);
                                System.out.println("Gift card blocked successfully!!!!");
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid input!!!!");
                }
            }
        }
    }


    public static void unBLockLogic(CustomerAccount currCustomerAccount, Bank bankDb) {
        System.out.println("UnBlock gift card");
        List<Integer> giftCards = bankDb.getActiveBlockedGiftCardForCustomer(currCustomerAccount);

        if (giftCards.isEmpty()){
            System.out.println("Seems this customer has no non blocked gift cards!!");

        } else {
            while (true) {
                try {
                    System.out.println("Available gift Cards: ");
                    for (int i = 0; i < giftCards.size(); i++) {
                        System.out.println("\t" + (i + 1) + ". Card Number: " + giftCards.get(i));
                    }


                    System.out.print("Enter the Gift Card Number to block:(0 for back) ");
                    int cardNumber = Integer.parseInt(input.readLine());

                    if (cardNumber == 0) break;
                    else {
                        if (!giftCards.contains(cardNumber) && !bankDb.containsGiftCard(cardNumber)) {
                            System.out.println("Seems the card with this id is not available");

                        } else {
                            GiftCard giftCard = bankDb.getGiftCardByNumber(cardNumber);

                            System.out.print("Enter the pin of the gift card: ");
                            int pin = Integer.parseInt(input.readLine());

                            if (giftCard.getPin() != pin) {
                                System.out.println("Seems the pin is mismatching please try again!!!!");
                            } else {
                                giftCard.setBlocked(false);
                                System.out.println("Gift card unblocked successfully!!!!");
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid input!!!!");
                }
            }

        }
    }



    public static void closeGiftCardLogic(CustomerAccount currCustomerAccount, Bank bankDb) {
        System.out.println("Close gift card!!!");
        List<Integer> giftCards = bankDb.getActiveGiftCardForCustomer(currCustomerAccount);

        if (giftCards.isEmpty()){
            System.out.println("Seems this customer has no gift cards!!");

        } else {
            while (true) {
                try {
                    System.out.println("Available gift Cards: ");
                    for (int i = 0; i < giftCards.size(); i++) {
                        System.out.println("\t" + (i + 1) + ". Card Number: " + giftCards.get(i));
                    }

                    System.out.print("Enter the Gift Card Number to Close:(0 for back) ");
                    int cardNumber = Integer.parseInt(input.readLine());

                    if (cardNumber == 0) break;
                    else {
                        if (!giftCards.contains(cardNumber) && !bankDb.containsGiftCard(cardNumber)) {
                            System.out.println("Seems the card with this id is not available");

                        } else {
                            GiftCard giftCard = bankDb.getGiftCardByNumber(cardNumber);

                            System.out.print("Enter the pin of the gift card: ");
                            int pin = Integer.parseInt(input.readLine());

                            if (giftCard.getPin() != pin) {
                                System.out.println("Seems the pin is mismatching please try again!!!!");
                            } else {
                                long amt = giftCard.getGiftCardBalance();
                                giftCard.debit(amt);
                                giftCard.setStatus(false);
                                currCustomerAccount.credit(amt);

                                System.out.println("Gift card closed successfully!!!!");
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a valid input!!!!");
                }
            }

        }

    }

    public static void createGiftCardLogic(CustomerAccount currCustomerAccount, Bank bankDb) {
        System.out.println("Create New Gift Card!!!\n");
        while (true){
            try {
                System.out.print("Enter the Amount to credit to gift card: ");
                long amount = Long.parseLong(input.readLine());

                if (currCustomerAccount.getBalance() < amount){
                    System.out.println("Your account balance is less than your entered input!!!!");
                } else{
                    System.out.print("Please Enter a PIN for your gift card: ");
                    try {
                        int pin = Integer.parseInt(input.readLine());
                        if (countDigits(pin) != 4){
                            System.out.println("Pin should be in 4 digit");
                        } else {
                            currCustomerAccount.debit(amount);
                            GiftCard gc = new GiftCard(pin, amount, bankDb, currCustomerAccount.getCustomerId());

                            bankDb.addNewGiftCard(gc);
                            System.out.println("Gift card Created Successfully with card Number " + gc.getCardNumber() + "!!!\n");
                            break;
                        }
                    } catch (Exception e){
                        System.out.println("Please enter valid input!!!");
                    }
                }
            } catch (Exception e){
                System.out.println("Please enter a valid input!!!");
            }
        }
    }


    private static int countDigits(int num){
        int res=0;
        while (num>0){
            num = num/10;
            res++;
        }

        return res;
    }


    private final static Random random = new Random();

    public static Integer generateUniqueIdForCustomer(Bank db){
        int res;
        while (true){
            res = random.nextInt(9999);
            if (!db.containsCustomerId(res)) break;
        }
        return res;
    }

    public static Integer generateUniqueIdForGiftCard(Bank db){
        int res;
        while (true){
            res = random.nextInt(9999);
            if (!db.containsGiftCard(res)) break;
        }
        return res;
    }
}































//    public static void purchaseItem(CustomerAccount currCustomerAccount, Bank bankDb) {
//        System.out.println("Purchase Item!!!");
//        List<Integer> giftCards = bankDb.getActiveGiftCardForCustomerButNotBlocked(currCustomerAccount);
//
//        if (giftCards.isEmpty()){
//            System.out.println("Seems this customer has no gift cards!!");
//
//        } else {
//            while (true) {
//                try {
//                    System.out.println("Available gift Cards: ");
//                    for (int i = 0; i < giftCards.size(); i++) {
//                        System.out.println("\t" + (i + 1) + ". Card Number: " + giftCards.get(i));
//                    }
//
//                    System.out.print("Enter the Gift Card Number to Purchase:(0 for back) ");
//
//                    int cardNumber = Integer.parseInt(input.readLine());
//                    if (cardNumber == 0) break;
//                    else {
//                        if (!giftCards.contains(cardNumber) && !bankDb.containsGiftCard(cardNumber)) {
//                            System.out.println("Seems the card with this id is not available for you");
//
//                        } else {
//                            GiftCard giftCard = bankDb.getGiftCardByNumber(cardNumber);
//
//                            System.out.print("Enter the pin of the gift card: ");
//                            int pin = Integer.parseInt(input.readLine());
//
//                            if (giftCard.getPin() != pin) {
//                                System.out.println("Seems the pin is mismatching please try again!!!!");
//                            } else {
//                                System.out.print("Enter the amount purchase: ");
//                                long amount = Long.parseLong(input.readLine());
//
//                                if (amount > giftCard.getGiftCardBalance()) {
//                                    System.out.println("Your purchase amount is more than your gift card  balance!!");
//
//                                } else {
//                                    giftCard.debit(amount); // change
//                                    Transaction transaction = new Transaction(giftCard.getCardNumber(), amount);
//                                    bankDb.addTransaction(transaction);
//
//                                    System.out.println("Purchase Done!!!!");
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    System.out.println("Please enter a valid input!!!!");
//                }
//            }
//        }
//    }





//    public static void topUpGiftCardLogic(CustomerAccount currCustomerAccount, Bank bankDb) {
//        System.out.println("Top-Up Gift Card");
//        List<Integer> giftCards = bankDb.getActiveGiftCardForCustomerButNotBlocked(currCustomerAccount);
//
//        if (giftCards.isEmpty()){
//            System.out.println("Seems this customer has no gift cards!!");
//
//        } else {
//            while (true){
//                try {
//                    System.out.println("Available gift Cards: ");
//                    for (int i=0; i<giftCards.size(); i++){
//                        System.out.println("\t"+(i+1)+". Card Number: "+giftCards.get(i));
//                    }
//
//                    System.out.print("Enter the Gift Card Number to Top-Up:(0 for back) ");
//                    int cardNumber = Integer.parseInt(input.readLine());
//
//                    if (cardNumber == 0 ) break;
//                    else {
//                        if (!giftCards.contains(cardNumber) && !bankDb.containsGiftCard(cardNumber)) {
//                            System.out.println("Seems the card with this id is not available");
//                        } else {
//                            GiftCard giftCard = bankDb.getGiftCardByNumber(cardNumber);
//
//                            System.out.print("Enter the amount to top up: ");
//                            long amount = Long.parseLong(input.readLine());
//
//                            if (amount > currCustomerAccount.getBalance()) {
//                                System.out.println("Your top up amount is more than your account balance!!");
//
//                            } else {
//                                currCustomerAccount.debit(amount);
//                                giftCard.credit(amount);
//
//                                System.out.println("Top-Up Successful!!!");
//                                break;
//                            }
//                        }
//                    }
//                } catch (Exception e){
//                    System.out.println("Please Enter a valid input!!");
//                }
//            }
//        }
//    }
