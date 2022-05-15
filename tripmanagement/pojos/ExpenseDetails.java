package sudharsan.zoho_questions.tripmanagement.pojos;

public class ExpenseDetails {

    public String getPaidBy() {
        return paidBy;
    }


    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }
    public String getNameOfExpense() {
        return nameOfExpense;
    }

    public void setNameOfExpense(String nameOfExpense) {
        this.nameOfExpense = nameOfExpense;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    private String paidBy;
    private String nameOfExpense;
    private int expenseAmount;

    private String timeOfExpense;


    public ExpenseDetails(String paidBy, String nameOfExpense, int expenseAmount) {
        this.paidBy = paidBy;
        this.nameOfExpense = nameOfExpense;
        this.expenseAmount = expenseAmount;
    }

    @Override
    public String toString() {
        return "ExpenseDetails{" +
                "paidBy='" + paidBy + '\'' +
                ", nameOfExpense='" + nameOfExpense + '\'' +
                ", expenseAmount=" + expenseAmount +
                '}';
    }

}
