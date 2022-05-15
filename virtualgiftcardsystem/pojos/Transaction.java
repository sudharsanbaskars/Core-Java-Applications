package sudharsan.zoho_questions.virtualgiftcardsystem.pojos;

public class Transaction {
    private final int id;
    private final Integer cardNumber;
    private final long amount;

    static int counter=0;
    public Transaction(Integer cardNumber, long amount) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.id = ++counter;
    }

    public int getId() {
        return id;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public long getAmount() {
        return amount;
    }

}
