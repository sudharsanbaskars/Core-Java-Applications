package sudharsan.zoho_questions.virtualgiftcardsystem.pojos;

import sudharsan.zoho_questions.virtualgiftcardsystem.Application;

public class GiftCard {
    private final Integer cardOwner;
    private final int cardNumber;
    private int pin;
    private long giftCardBalance;

    private boolean status;
    private boolean isBlocked = false;

    public GiftCard(int pin, long giftCardBalance, Bank db, Integer cardOwner){
            this.pin = pin;
            this.giftCardBalance = giftCardBalance;
            this.cardNumber = Application.generateUniqueIdForGiftCard(db);
            this.status = true;
            this.cardOwner = cardOwner;
    }

    public String getStatusName(){
        String res = status ? "Active" : "Close";
        return res;
    }

    public void credit(long amount){
        this.giftCardBalance += amount;
    }

    public void debit(long amount){
        this.giftCardBalance -= amount;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public long getGiftCardBalance() {
        return giftCardBalance;
    }

    public void setGiftCardBalance(long giftCardBalance) {
        this.giftCardBalance = giftCardBalance;
    }

    public boolean isActive() {
        return status;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public Integer getCardOwner() {
        return cardOwner;
    }
}
