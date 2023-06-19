import java.util.*;

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
class Sector{
    String code;
    int numSeats;
    int seats;
    boolean []seatTaken;
    int type;
    public Sector(String sectorName, int size) {
        code = sectorName;
        numSeats = size;
        seats = size;
        seatTaken = new boolean[numSeats + 1];
        Arrays.fill(seatTaken, false);
        type = 0;
    }

    public String getCode() {
        return code;
    }

    public int getNumSeats() {
        return numSeats;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, numSeats, seats, (double)(seats - numSeats) / seats * 100.0);
    }
}
class Stadium{
    String name;
    Map<String, Sector> sectors;
    public Stadium(String name){
        this.name = name;
        sectors = new HashMap<>();
    }
    public void createSectors(String[] sectorNames, int[] sizes){
        for(int i = 0; i < sectorNames.length; i++){
            sectors.put(sectorNames[i], new Sector(sectorNames[i], sizes[i]));
        }
    }
    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector sector = sectors.get(sectorName);
        if(sector.seatTaken[seat]) {
            throw new SeatTakenException();
        }
        else if(type == 1 && sector.type == 2){
            throw new SeatNotAllowedException();
        }
        else if(type == 2 && sector.type == 1){
            throw new SeatNotAllowedException();
        }
        else {
            sector.seatTaken[seat] = true;
            if(sector.type == 0){
                sector.type = type;
            }
            sector.numSeats--;
        }
    }
    public void showSectors(){
        sectors
                .values()
                .stream()
                .sorted(Comparator.comparing(Sector::getNumSeats).reversed().thenComparing(Sector::getCode))
                .forEach(System.out::println);
    }
}
class SeatTakenException extends Exception{

}
class SeatNotAllowedException extends Exception{

}