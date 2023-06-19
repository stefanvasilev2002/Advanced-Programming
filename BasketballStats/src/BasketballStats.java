import java.util.*;
public class BasketballStats {
    public static List<String> readInput() {
        final Scanner scanner = new Scanner(System.in);
        final List<String> items = new ArrayList<>();
        while(scanner.hasNextLine()){
            items.add(scanner.nextLine());
        }
        return items;
    }
    public static void main(String[] args) {
        final List<String> lines = readInput();
        BasketballTeam team = new BasketballTeam();

        for(String game : lines.get(0).split(",")){
            String[]parts = game.split(":");
            team.computeGame(parts[0], parts[1]);
        }
        for(String game : lines.get(1).split(",")){
            String[]parts = game.split(":");
            team.computeGame(parts[1], parts[0]);
        }
        System.out.println(team);
    }
}
class BasketballTeam{
    private int wins;
    private int losses;
    private int draws;
    private  int maxDiffLoss;
    private int maxDiffWin;

    public BasketballTeam() {
        wins = 0;
        losses = 0;
        draws = 0;
        maxDiffLoss = 0;
        maxDiffWin = 0;
    }
    public void computeGame(String homeTeam, String awayTeam){
        int homeTeamPoints = Integer.parseInt(homeTeam);
        int awayTeamPoints = Integer.parseInt(awayTeam);

        if (homeTeamPoints > awayTeamPoints){
            wins++;
            if (homeTeamPoints - awayTeamPoints > maxDiffWin){
                maxDiffWin = homeTeamPoints - awayTeamPoints;
            }
        }
        else if(awayTeamPoints > homeTeamPoints){
            losses++;
            if (awayTeamPoints - homeTeamPoints > maxDiffLoss){
                maxDiffLoss = awayTeamPoints - homeTeamPoints;
            }
        }
        else {
            draws++;
        }
    }
    @Override
    public String toString() {
        return  "wins=" + wins +
                ", losses=" + losses +
                ", draws=" + draws +
                ", Max win=" + maxDiffWin +
                ", Max loss=" + maxDiffLoss;
    }
}