package sudharsan.zoho_questions.tripmanagement.pojos;

import sudharsan.zoho_questions.tripmanagement.dao.DataBase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Trip {

    private static int TRIP_COUNT = 0; // total number of trips

    //private int tripID = 1;
    private String tripName;
    private String startDate;
    private String endDate;
    private int numOfMembers;
    private String addedAt;

    private int tripExpensesAmount = 0;

    private ArrayList<ExpenseDetails> expenses = new ArrayList<>();
    private ArrayList<Member> membersInTrip = new ArrayList<>();
    private ArrayList<Member> availableMembers = new ArrayList<>();
    private ArrayList<Owe> owes = new ArrayList<>();
    private ArrayList<String> owesSettleHistory = new ArrayList<>();

    private LinkedHashSet<String> pendingOwesNames = new LinkedHashSet<>();

    public HashMap<String, Double> memberExpenseMap = new HashMap<>();
    public HashMap<String, Double> membersOwesMap = new HashMap<>();


    public Set<String> getPendingOwes(){
        return this.pendingOwesNames;
    }


    //HashMap<String, ArrayList<String>> oweMap = new HashMap<>();



    public Trip(String tripName, String startDate, String endDate, int numOfMembers){
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numOfMembers = numOfMembers;
        //for(String name :  DataBase.users)
        this.addedAt = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Trip.TRIP_COUNT += 1;


       // List<Member> list = ;
//        for (Member m: DataBase.users){
//            if (m.isFree(startDate, endDate)){
//                this.availableMembers.add(m);
//
//            }
//        }
       this.availableMembers.addAll(DataBase.users);
    }

    public void putExpenseInMap(String name, double amount){
        if (this.memberExpenseMap.containsKey(name)){
            this.memberExpenseMap.replace(name, this.memberExpenseMap.get(name)+amount);
        } else {
            this.memberExpenseMap.put(name, amount);
        }
    }

    public void putOweInMap(Owe owe){
        if (membersOwesMap.containsKey(owe.getOweFrom())){
            this.membersOwesMap.replace(owe.getOweFrom(), this.membersOwesMap.get(owe.getOweFrom()) + owe.getAmount());
        } else {
            this.membersOwesMap.put(owe.getOweFrom(), owe.getAmount());
        }
    }

    public void printExpenseDetails(){
        System.out.println("Expense Name          |      Spent By            |        Amount Spent  ");
        System.out.println("-------------------------------------------------------------------------");
        for (ExpenseDetails exp : this.expenses){
            System.out.println(exp.getNameOfExpense() + "         |         " + exp.getPaidBy() + "                |              "+exp.getExpenseAmount());
        }
    }

    public ArrayList<ExpenseDetails> getAllTripExpenses(){
        return this.expenses;
    }

    public void addExpense(ExpenseDetails expense){
        this.tripExpensesAmount += expense.getExpenseAmount();
        expenses.add(expense);
        putExpenseInMap(expense.getPaidBy(), expense.getExpenseAmount());
    }

    public ArrayList<Member> getAvailableMembers() {
        return availableMembers;
    }

    public Member getAvailableMemberByIndex(int idx){
        return availableMembers.get(idx);
    }

    public void removeAvailableMembers(int idx){
        this.availableMembers.remove(idx);
    }
    public void setAvailableMembers(ArrayList<Member> members) {
       this.availableMembers = members;
    }
    public void addAvailableMembers(Member mem){
        this.availableMembers.add(mem);
    }

    // methods for members in trip
//    public void addMembersInTrip(String name){
//        this.membersInTrip.add(name);
//    }


    public String getTripName() {
        return tripName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public int getTripExpenses() {
        return tripExpensesAmount;
    }

//    public void addExpenses(int value){
//        this.tripExpensesAmount += value;
//    }

    public double getIndividualShareTillNow(){
        return (long)(this.tripExpensesAmount / this.numOfMembers);
    }

    public int getTripSize(){
        return this.numOfMembers;
    }

    // methods for members in trip
    public void addMember(String name){
        Member m = new Member(name);
        m.addTripsIn(this);
        this.membersInTrip.add(m);
    }

    public List<Member> getMembersInTrip(){
        return this.membersInTrip;
    }

    public Member getMembersInTripByIndex(int idx){
        return this.membersInTrip.get(idx);
    }

    // simplify owes////////////////////////////////////////
    public void simplifyOwes(){
        for (int i=0; i<owes.size()-1; i++){
            Owe firstOwe = owes.get(i);
            for (int j=i+1; j<owes.size(); j++){
                Owe secondOwe = owes.get(j);
                if ((firstOwe.getOweFrom()).equals(secondOwe.getOweTo()) && (firstOwe.getOweTo().equals(secondOwe.getOweFrom())) ){
                     doSimplify(firstOwe, secondOwe, i, j);
                }
            }
        }
    }

    private void doSimplify(Owe o1, Owe o2, int o1Idx, int o2Idx){
        double diff;
        if (o1.getAmount() > o2.getAmount()){
            diff = o1.getAmount() - o2.getAmount();
            o1.setAmount(diff);
            this.owes.remove(o2Idx);
        } else if (o1.getAmount() == o2.getAmount()){
            this.owes.remove(o1);
            this.owes.remove(o2);
        } else {
            diff = o2.getAmount() - o1.getAmount();
            o2.setAmount(diff);
            this.owes.remove(o1Idx);
        }
    }

///////////////////////////////////////////////////////

    // methods for owe
    public void createOwes(ExpenseDetails expense){
        double expenseSplit = (double)Math.round((double)(expense.getExpenseAmount()/this.getMembersInTrip().size()));

        for (Member name : this.getMembersInTrip()){

           if (!name.getName().equals(expense.getPaidBy())){
               Owe newOwe = new Owe(name.getName(), expense.getPaidBy(),expenseSplit);
               if (owes.contains(newOwe)){
                   int idx = this.owes.indexOf(newOwe);
                   this.owes.get(idx).setAmount(newOwe.getAmount() + this.owes.get(idx).getAmount());
               } else {
                   this.owes.add(newOwe);
               }
               putOweInMap(newOwe);// put in map
               pendingOwesNames.add(newOwe.getOweFrom());
           }
       }
        simplifyOwes();
    }

    public void settleUpOwe(String name){
        for (Owe owe : owes){
            if (owe.getOweFrom().equals(name)){
                String oweMessage = owe.getOweFrom() + " paid " + owe.getAmount() + " to " + owe.getOweTo()+ " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                this.membersOwesMap.replace(name, this.membersOwesMap.get(name) - owe.getAmount()); // for the owe map
                System.out.println(oweMessage);
                this.owesSettleHistory.add(oweMessage);
                System.out.println("List in settleupowe: " + this.owesSettleHistory);
                owes.remove(owe);
            }
        }
    }

    public void addOweHistory(String message){
        this.owesSettleHistory.add(message);
    }
    public void displayOwes(){
        System.out.println("--------------------------------------------------------------");
        System.out.println("Showing Owes");
        for (Owe owe : this.owes){
            owe.printOweMessage();
        }
        System.out.println("--------------------------------------------------------------");

    }

    public List<String> getOwesHistory(){
//        System.out.println("Inside getOwesHistory");
//        System.out.println(this.owesSettleHistory);
        return this.owesSettleHistory;
    }

    public List<Owe> getAllOwes(){
        return this.owes;
    }

    public void deleteOweByIndex(int idx){
        this.owes.remove(idx);
    }

    public void setOweByIndex(int idx, double amt){
        Owe updatedOwe = this.owes.get(idx);
        updatedOwe.setAmount(amt);
        this.owes.set(idx, updatedOwe);
    }

    // delete a  user
    public void removeMemberFromTrip(int idx){
        System.out.println("User " + this.membersInTrip.get(idx) + " removed from the Trip successfully!!!");
        this.membersInTrip.remove(idx);
    }


    public void showMembers(){
        System.out.println("----------------------------------------------------------------");
        System.out.println("Total members in this Trip are: " + this.membersInTrip.size());
        System.out.println("Members in the Trip: ");
        for(Member member : this.getMembersInTrip()){
            System.out.println("->" + member);
        }
        System.out.println("----------------------------------------------------------------");

    }

    public void showTripDetails(){
        System.out.println("Trip Name: " + this.tripName);
        System.out.println("Start Date: " + this.startDate);
        System.out.println("End Date: " + this.endDate);
        System.out.println("Number of Members: " + this.numOfMembers);
        System.out.println("Total Expenses: " + this.tripExpensesAmount);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripName='" + tripName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", numOfMembers=" + numOfMembers +
                '}';
    }

}
