package sudharsan.zoho_questions.virtualgiftcardsystem.pojos;

import java.util.ArrayList;
import java.util.HashMap;

public class Bank {

    private final HashMap<Integer, CustomerAccount> customerAccounts = new HashMap<>();
    private final HashMap<Integer, GiftCard> giftCards = new HashMap<>();
    private final HashMap<Integer, Transaction> transactions = new HashMap<>();

    public void addNewCustomer(CustomerAccount customerAccount){
        this.customerAccounts.put(customerAccount.getCustomerId(), customerAccount);
    }

    public void addTransaction(Transaction t){
        this.transactions.put(t.getId(), t);
    }

    public void displayTransactionSummary(){
        if (transactions.isEmpty()){
            System.out.println("No Transaction Done");

        } else {
            System.out.println("Transaction  Summary");
            System.out.println("Transaction No.     |     Card No      |      Amount    ");

            for (Transaction t : this.transactions.values()) {
                System.out.println("      " + t.getId() + "          |     " + t.getCardNumber() + "         |     " + t.getAmount());
            }
        }
    }

    public ArrayList<Integer> getCustomerAccounts(){
        return new ArrayList<>(this.customerAccounts.keySet());
    }

    public CustomerAccount getCustomerAccountById(int id){
        return this.customerAccounts.get((Integer) id);
    }

    public ArrayList<Integer> getActiveGiftCardForCustomerButNotBlocked(CustomerAccount acc){
        ArrayList<Integer> list = new ArrayList<>();
        for (GiftCard gc : this.giftCards.values()){
            if (!gc.isBlocked() && gc.isActive() && gc.getCardOwner().equals(acc.getCustomerId())){
                list.add(gc.getCardNumber());
            }
        }
        return list;
    }

    public ArrayList<Integer> getActiveBlockedGiftCardForCustomer(CustomerAccount acc){
        ArrayList<Integer> list = new ArrayList<>();
        for (GiftCard gc : this.giftCards.values()){
            if (gc.isBlocked() && gc.isActive() && gc.getCardOwner().equals(acc.getCustomerId())){
                list.add(gc.getCardNumber());
            }
        }
        return list;
    }

    public ArrayList<Integer> getActiveGiftCardForCustomer(CustomerAccount acc){
        ArrayList<Integer> list = new ArrayList<>();
        for (GiftCard gc : this.giftCards.values()){
            if (gc.isActive() && gc.getCardOwner().equals(acc.getCustomerId())){
                list.add(gc.getCardNumber());
            }
        }
        return list;
    }

    public ArrayList<Integer> getAllGiftCardsForCustomer(CustomerAccount acc){
        ArrayList<Integer> list = new ArrayList<>();
        for (GiftCard gc : this.giftCards.values()){
            if (gc.getCardOwner().equals(acc.getCustomerId())){
                list.add(gc.getCardNumber());
            }
        }
        return list;
    }

    public ArrayList<Integer> getAllGiftCardForCustomerButNotBlocked(CustomerAccount acc){
        ArrayList<Integer> list = new ArrayList<>();
        for (GiftCard gc : this.giftCards.values()){
            if (!gc.isBlocked() && gc.getCardOwner().equals(acc.getCustomerId())){
                list.add(gc.getCardNumber());
            }
        }
        return list;
    }

    public boolean containsGiftCard(int number){
        return this.giftCards.containsKey(number);
    }

    public GiftCard getGiftCardByNumber(int cardNumber){
        return this.giftCards.get(cardNumber);
    }

    public void displayEachCustomerBalance(){
        System.out.println("Account Summary");
        System.out.println("Customer ID    |   Customer Name    |    Balance    ");
        for (CustomerAccount acc : this.customerAccounts.values()){
            System.out.println(acc.getCustomerId()+"           |     "+acc.getCustomerName()+"          |      "+ acc.getBalance());
        }
    }

    public void addNewGiftCard(GiftCard gc){
        this.giftCards.put(gc.getCardNumber(), gc);
    }

    public void displayGiftCardSummary(){
        System.out.println("Gift Card Summary");
        System.out.println(" Card Number    |  Customer ID  |   Customer Name   |   PIN    |    Gift Card Balance   | Status");
        for (GiftCard gc : this.giftCards.values()){
            System.out.println("     "+gc.getCardNumber() + "        |    "+gc.getCardOwner()+"      |     "+this.customerAccounts.get(gc.getCardOwner()).getCustomerName()+"    |    "+gc.getPin()+"    |    "+gc.getGiftCardBalance()+"    |    "+gc.getStatusName());
        }
    }

    public boolean containsCustomerId(int id){
        return this.customerAccounts.containsKey((Integer) id);
    }



}
