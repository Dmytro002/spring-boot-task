package com.example.demo.service;

import com.example.demo.models.Match;
import com.example.demo.models.Team;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class MatchService {
    private List<Match> upcomingMatches = new ArrayList<>();
    private List<Match> playedMatches = new ArrayList<>();

     int a = 2;

    private final TeamService teamService;

    public MatchService(TeamService teamService) {
        this.teamService = teamService;
    }


    public  Match playMatch() {
        Match match = upcomingMatches.get(0);
        String winnerName = new Random().nextBoolean()?match.getTeam1():match.getTeam2();
        Team winner =  teamService.getByName(winnerName);

        winner.setWins(winner.getWins() + 1);

        match.setResult(winner.getName() + " " +"wins");
        upcomingMatches.remove(0);
        playedMatches.add(match);

        if(winner.getWins() >= a) {
            System.out.println("Tournament is over!!!");
            System.out.println("Glory to " + winner.getName());
            System.exit(0);
        }
        createNewMatchIfPossible(winner);
        return match;
    }
    private  void createNewMatchIfPossible(Team winner) {
        Optional<Team> opponent = teamService.getRandomAvailableTeam(winner.getWins());
        if(opponent.isPresent()){
            Match match = new Match();
            match.setTeam1(winner.getName());
            match.setTeam2(winner.getName());
            upcomingMatches.add(match);
            teamService.deleteAvailableTeam(winner);
        }else {
            teamService.addAvailableTeam(winner);
        }
    }

    public List<Match> tossTeams() {
        while (teamService.hasAvailableTeam()) {
            Match match = new Match();

           Team team1 = teamService.getRandomAvailableTeam(0).get();
           match.setTeam1(team1.getName());
           teamService.deleteAvailableTeam(team1);

            Team team2 = teamService.getRandomAvailableTeam(0).get();
            match.setTeam2(team2.getName());
            teamService.deleteAvailableTeam(team2);

            upcomingMatches.add(match);
        }
        return upcomingMatches;
    }
}