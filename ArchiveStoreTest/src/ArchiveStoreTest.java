import java.text.spi.BreakIteratorProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

class ArchiveStore{
    List<Archive> archives;
    List<String> logs;
    public ArchiveStore(){
        archives=new ArrayList<Archive>();
        logs=new ArrayList<String>();
    }
    public void archiveItem(Archive item, Date date){
        item.setDateArchived(date);
        archives.add(item);
        logs.add(String.format("Item %d archived at %s",item.getId(), date));
    }
    public void openItem(int id, Date date) throws NonExistingItemException {
        Archive temp;
        for(int i=0; i<archives.size(); i++){
            temp=archives.get(i);
            if(temp.getId()==id){
                if(temp instanceof LockedArchive){
                    LockedArchive locked=(LockedArchive) temp;
                    if(locked.getDateToOpen().before(date)){
                        logs.add(String.format("Item %d opened at %s",locked.getId(), date));
                    }
                    else {
                        logs.add(String.format("Item %d cannot be opened before %s",locked.getId(), locked.getDateToOpen()));
                    }
                }
                else if (temp instanceof SpecialArchive){
                    SpecialArchive special=(SpecialArchive) temp;
                    if(special.getTimesOpened()<special.getMaxOpen()){
                        special.incrementTimesOpened();
                        logs.add(String.format("Item %d opened at %s",special.getId(), date));
                    }
                    else {
                        logs.add(String.format("Item %d cannot be opened more than %d times",special.getId(), special.getMaxOpen()));
                    }
                }
                return;
            }
        }
        throw new NonExistingItemException(id);
    }
    public String getLog(){
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<logs.size(); i++){
            String replaced= logs.get(i).replace("GMT", "UTC");
            sb.append(replaced);
            sb.append("\n");
        }
        return sb.toString();
    }
}
class NonExistingItemException extends Throwable {
    int id;
    public NonExistingItemException(int id){
        this.id=id;
    }
    public String getMessage(){

        return String.format("Item with id %d doesn't exist", id);
    }
}
class Archive{
    private int id;
    private Date dateArchived;

    public Archive(int id) {
        this.id=id;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public int getId() {
        return id;
    }
}
class LockedArchive extends Archive{
    private Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }
}
class SpecialArchive extends Archive{
    private int maxOpen;
    private int timesOpened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen=maxOpen;
        timesOpened=0;
    }
    public void incrementTimesOpened(){
        timesOpened++;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public int getTimesOpened() {
        return timesOpened;
    }
}