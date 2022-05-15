package sudharsan.zoho_questions.tripmanagement.pojos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Member {
    private String name;
    public String getName() {
        return name;
    }

    public ArrayList<Trip> getTripsIn() {
        return this.tripsIn;
    }


    private ArrayList<Trip> tripsIn = new ArrayList<>();

    public void addTripsIn(Trip trip){
        this.tripsIn.add(trip);
    }

    public Member(String name) {
        this.name = name;
    }

    public boolean isFree(String startDateString, String endDateString){
        LocalDate startDate = convertStringToDate(startDateString);
        LocalDate endDate = convertStringToDate(endDateString);

        for (int i=0; i<this.tripsIn.size(); i++){
            LocalDate sd = convertStringToDate(this.tripsIn.get(i).getStartDate());
            LocalDate ed = convertStringToDate(this.tripsIn.get(i).getEndDate());

            if ( !((startDate.isBefore(sd) && endDate.isBefore(sd)) || (startDate.isAfter(ed) && endDate.isAfter(ed))) )
                return false;
        }
        return true;
    }

    private static LocalDate convertStringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    @Override
    public String toString() {
        return this.name;
    }

//    public static void main(String[] args) {
//        String startDate = "7/01/2020";
//        String endDate = "10/01/2020";
//
//        System.out.println(isFree(startDate, endDate));
//    }

}
