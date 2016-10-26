package info.cameronlund.scout.objects;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import info.cameronlund.scout.MainActivity;

public class Team {
    private String teamNumber = "";
    private String teamName = "";
    private int bestRobotScore = 0;
    private int bestProgrammingScore = 0;
    private float rating = 0.0F;
    private int bestRank = 0;
    private int averageRank = 0;
    private ArrayList<Award> awards = new ArrayList<>();

    public Team(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Team(JsonObject json) {
        populateFromJson(json);
    }

    public Team(DataSnapshot ref) {
        if (!ref.getKey().equals(teamNumber))
            Log.e("Scout","Setting team "+teamNumber+" to non team number key! ("+ref.getKey()+")");
        teamName = (String) ref.child("teamName").getValue();
        bestRobotScore = (int) ref.child("bestRobotScore").getValue();
        bestProgrammingScore = (int) ref.child("bestProgrammingScore").getValue();
        rating = (float) ref.child("rating").getValue();
        bestRank = (int) ref.child("bestRank").getValue();
        averageRank = (int) ref.child("averageRank").getValue();
        // TODO Handle awards
    }

    public void saveToFirebase(DatabaseReference ref) {
        if (!ref.getKey().equals(teamNumber))
            Log.e("Scout","Setting team "+teamNumber+" to non team number key! ("+ref.getKey()+")");
        ref.child("teamName").setValue(teamName);
        ref.child("bestRobotScore").setValue(bestRobotScore);
        ref.child("bestProgrammingScore").setValue(bestProgrammingScore);
        ref.child("rating").setValue(rating);
        ref.child("bestRank").setValue(bestRank);
        ref.child("averageRank").setValue(averageRank);
        // TODO Handle awards
    }

    public void populateFromJson(JsonObject json) {
        teamNumber = json.get("number").getAsString();
        teamName = json.get("team_name").getAsString();
    }

    public int getBestRobotScore() {
        return bestRobotScore;
    }

    public int getBestProgrammingScore() {
        return bestProgrammingScore;
    }

    public float getRating() {
        return rating;
    }

    public int getBestRank() {
        return bestRank;
    }

    public int getAverageRank() {
        return averageRank;
    }

    public ArrayList<Award> getAwards() {
        Collections.sort(awards);
        return awards;
    }

    public void setBestRobotScore(int score) {
        bestRobotScore = score;
    }

    public void setBestProgrammingScore(int score) {
        bestProgrammingScore = score;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setBestRank(int rank) {
        bestRank = rank;
    }

    public void setAverageRank(int rank) {
        averageRank = rank;
    }

    public void setAwards(ArrayList<Award> awards) {
        this.awards = awards;
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public String getTeamName() {
        return teamName;
    }
}
