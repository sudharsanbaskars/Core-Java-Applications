package sudharsan.zoho_questions.tripmanagement.pojos;

public class Owe {

    private String oweFrom;
    private String oweTo;
    static int k=0;

    public String getOweFrom() {
        return oweFrom;
    }

    public void setOweFrom(String oweFrom) {
        this.oweFrom = oweFrom;
    }

    public String getOweTo() {
        return oweTo;
    }

    public void setOweTo(String oweTo) {
        this.oweTo = oweTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owe)) return false;
        Owe owe = (Owe) o;
        return this.oweFrom.equals(owe.oweFrom) && this.oweTo.equals(owe.oweTo);
    }

    @Override
    public int hashCode() {
        return k++;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private double amount;

    public Owe(String oweFrom, String oweTo, double amount) {
        this.oweFrom = oweFrom;
        this.oweTo = oweTo;
        this.amount = amount;
    }

    public void printOweMessage(){
        System.out.println(this.oweFrom + " owes " + this.amount + " to " + this.oweTo);
    }


}
