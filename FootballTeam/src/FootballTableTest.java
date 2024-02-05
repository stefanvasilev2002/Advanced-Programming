import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}
class Team{
    String name;
    int wins;
    int draws;
    int losses;
    int goalDifference;
    public Team(String homeTeam) {
        this.name = homeTeam;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.goalDifference = 0;
    }
    public int getPoints(){
        return wins * 3 + draws;
    }

    public void setWins() {
        this.wins++;
    }

    public void setDraws() {
        this.draws++;
    }

    public void setLosses() {
        this.losses++;
    }

    public void setGoalDifference(int i) {
        goalDifference += i;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }
    public int getPlayed(){
        return wins + draws + losses;
    }
}
class FootballTable{
    Map<String, Team> teams;

    public FootballTable() {
        this.teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        teams.putIfAbsent(homeTeam, new Team(homeTeam));
        teams.putIfAbsent(awayTeam, new Team(awayTeam));
        Team home = teams.get(homeTeam);
        Team away = teams.get(awayTeam);

        if (homeGoals > awayGoals){
            home.setWins();
            away.setLosses();
        }
        else if(awayGoals > homeGoals){
            home.setLosses();
            away.setWins();
        }
        else {
            home.setDraws();
            away.setDraws();
        }
        home.setGoalDifference(homeGoals - awayGoals);
        away.setGoalDifference(awayGoals - homeGoals);
    }
    public void printTable(){
        List<Team> sorted = teams.values()
                .stream()
                .sorted(Comparator.comparing(Team::getPoints)
                        .thenComparing(Team::getGoalDifference)
                        .reversed()
                        .thenComparing(Team::getName))
                .collect(Collectors.toList());
        int i = 1;
        for (Team t : sorted){
            System.out.printf("%2d. %-15s%5s%5s%5s%5s%5s\n", i, t.name, t.getPlayed(), t.getWins(), t.getDraws(), t.getLosses(), t.getPoints());
            i++;
        }
    }
}