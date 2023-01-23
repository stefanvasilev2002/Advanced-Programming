import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

class FootballTable{
    Map<String, Team> teams;
    public FootballTable() {
        teams = new HashMap<>();
    }
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        Team home=teams.computeIfAbsent(homeTeam, key->new Team(homeTeam));
        Team away=teams.computeIfAbsent(awayTeam, key->new Team(awayTeam));
        home.scoredGoals+=homeGoals;
        home.concededGoals+=awayGoals;
        away.scoredGoals+=awayGoals;
        away.concededGoals+=homeGoals;
        if(homeGoals>awayGoals){
            home.wins++;
            away.loses++;
        }
        else if(homeGoals<awayGoals){
            home.loses++;
            away.wins++;
        }
        else {
            home.draws++;
            away.draws++;
        }
    }

    public void printTable() {
        List<Team> result=teams.values().stream()
                .sorted(Comparator.comparing(Team::getPoints)
                        .thenComparing(Team::goalDifference)
                        .reversed()
                        .thenComparing(Team::getName))
                .collect(Collectors.toList());
        IntStream.range(0,result.size())
                .forEach(i->System.out.printf("%2d. %s\n", i + 1, result.get(i)));
    }
}
class Team{
    String name;
    int wins;
    int loses;
    int draws;
    int scoredGoals;
    int concededGoals;

    public Team(String name) {
        this.name = name;
    }
    public int getPoints(){
        return wins*3+draws;
    }
    public int goalDifference() {
        return scoredGoals - concededGoals;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, wins+loses+draws, wins, draws, loses, getPoints());
    }
}