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
class Sector implements Comparable<Sector>{
    String name;
    int seats;
    HashMap<Integer, Integer> taken;
    HashSet<Integer> types;

    public Sector(String code, int seats) {
        this.name = code;
        this.seats = seats;
        taken = new HashMap<>();
        types = new HashSet<>();
    }
    int free() {
        return seats - taken.size();
    }
    @Override
    public int compareTo(Sector o) {
        if(free()==o.free()){
            return name.compareTo(o.name);
        }
        return o.free()-free();
    }
    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", name, free(), seats,
                (seats - free()) * 100.0 / seats);
    }
    public void takeSeat(int seat, int type) throws SeatNotAllowedException {
        if(type==1){
            if(types.contains(2)){
                throw new SeatNotAllowedException();
            }
        }
        else if(type==2){
            if(types.contains(1)){
                throw new SeatNotAllowedException();
            }
        }
        types.add(type);
        taken.put(seat, type);
    }
    public boolean isTaken(int seat) {
        return taken.containsKey(seat);
    }
}
class Stadium{
    String name;
    HashMap<String, Sector> sectors;
    public Stadium(String name){
        this.name=name;
        sectors=new HashMap<>();
    }
    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        for (int i = 0; i < sectorNames.length; ++i) {
            addSector(sectorNames[i], sectorSizes[i]);
        }
    }
    void addSector(String name, int size) {
        Sector sector = new Sector(name, size);
        sectors.put(name, sector);
    }
    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector temp = sectors.get(sectorName);
        if(temp.isTaken(seat)){
            throw new SeatTakenException();
        }
        temp.takeSeat(seat, type);
    }
    public void showSectors(){
        List<Sector> sectorsList = new ArrayList<>(sectors.values());
        Collections.sort(sectorsList);
        for (Sector sector : sectorsList) {
            System.out.println(sector);
        }
    }
}
class SeatNotAllowedException extends Exception{
    public SeatNotAllowedException() {
        super();
    }
}
class SeatTakenException extends Exception{
    public SeatTakenException() {
        super();
    }
}