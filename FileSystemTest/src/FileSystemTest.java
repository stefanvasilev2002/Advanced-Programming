import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}
class File{
    String name;
    int size;
    LocalDateTime created;
    public File(String name, int size, LocalDateTime created) {
        this.name = name;
        this.size = size;
        this.created = created;
    }
    public boolean isHidden(){
        return name.charAt(0) == '.';
    }
}
class Folder{
    char name;
    List<File> files;
    public Folder(char name) {
        this.name = name;
        files  = new ArrayList<>();
    }
    public void addFile(String name, int size, LocalDateTime createdAt){
        files.add(new File(name, size, createdAt));
    }
    public int getSize(){
        return files.stream().mapToInt(x -> x.size).sum();
    }
}
class FileSystem {
    List<Folder> folders;
    public FileSystem() {
        folders = new ArrayList<>();
    }
    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        for(Folder f : folders){
            if(f.name == folder){
                f.addFile(name, size, createdAt);
                return;
            }
        }
        Folder f  =new Folder(folder);
        f.addFile(name, size, createdAt);
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return folders.stream().flatMap(f -> f.files
                        .stream()
                        .filter(File::isHidden)
                        .filter(x -> x.size < size))
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        int s = 0;
        for(Character c : folders){
            for(Folder f : this.folders){
                if(f.name == c){
                    s+=f.getSize();
                }
            }
        }
        return s;
    }
    public Map<Integer, Set<File>> byYear(){
        folders.stream().flatMap(x -> x.files.stream().collect(Collectors.groupingBy()))
    }
}