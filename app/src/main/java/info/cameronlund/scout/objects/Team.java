package info.cameronlund.scout.objects;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;

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
