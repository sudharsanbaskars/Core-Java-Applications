package sudharsan.zoho_questions.virtualgiftcardsystem.pojos;

import sudharsan.zoho_questions.virtualgiftcardsystem.Application;

public class CustomerAccount {

    private Integer customerId;
    private String customerName;
    private long balance;
    //private ArrayList<Integer> giftCards = new ArrayList<>();

    public CustomerAccount(String customerName, long balance, Bank db) {
        this.customerName = customerName;
        this.balance = balance;
        this.customerId = Application.generateUniqueIdForCustomer(db);
    }

    public void debit(long amount){
        this.balance -= amount;
    }

    public void credit(long amount){
        this.balance += amount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public CustomerAccount(String customerName, long balance){
        this.customerName = customerName;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "CustomerAccount{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", balance=" + balance +
                '}';
    }

}
