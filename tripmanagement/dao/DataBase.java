package sudharsan.zoho_questions.tripmanagement.dao;

import sudharsan.zoho_questions.tripmanagement.pojos.Member;
import sudharsan.zoho_questions.tripmanagement.pojos.Trip;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;


public class DataBase {

    public static ArrayList<Member> users = new ArrayList<>();
    private ArrayList<Trip> trips = new ArrayList<>();

    public DataBase(){
    }

    public int getExpense(Trip trip){
        return trip.getTripExpenses();
    }

    public void addTrip(Trip trip){
        this.trips.add(trip);
    }

    public void addUser(String name){
        Member member = new Member(name);
        for (Trip t : this.trips){
            t.addAvailableMembers(member);
        }
        this.users.add(member);
    }

    public List<String> getALlTrips(){
        List<String> tripNames = new ArrayList<>();

        trips.forEach(i -> tripNames.add(i.getTripName()));
        return tripNames;
    }

    public List<Member> getAllUsers(){
        return users;
    }

    public void addUserToTrip(Trip trip, Member name){
        int indexOfTrip = trips.indexOf(trip);
        trips.get(indexOfTrip).addMember(name.getName());
    }

    public Trip getTripByIndex(int idx){
        return trips.get(idx);
    }
}
