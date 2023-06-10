import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
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
class FileSystem{
    Map<Character, Set<File>> folders;

    public FileSystem() {
        folders=new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        Set<File> files=folders.get(folder);
        if(files==null){
            files=new TreeSet<>();
        }
        files.add(new File(name,size,createdAt));
        folders.put(folder,files);
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int sizze){
        return folders.get('.')
                .stream()
                .filter(i->i.size<sizze)
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        //todo
        return folders.stream()
                .mapToInt(folder -> this.folders.get(folder)
                        .stream().mapToInt(i->i.size)
                        .sum())
                .sum();
    }
    public Map<Integer, Set<File>> byYear(){
        return folders.values().stream()
                .flatMap(Collection::stream)
                .collect(
                        Collectors.groupingBy(
                                file -> file.date.getYear(),
                                Collectors.toSet())
                );
    }
    public Map<String, Long> sizeByMonthAndDay(){
        return folders.values().stream()
                .flatMap(Collection::stream)
                .collect(
                        Collectors.groupingBy(
                                File::toMonthDay,
                                Collectors.summingLong(i->i.size))
                );
    }
}
class File implements Comparable<File>{
    String name;
    int size;
    LocalDateTime date;

    public File(String name, int size, LocalDateTime date) {
        this.name = name;
        this.size = size;
        this.date = date;
    }

    @Override
    public int compareTo(File o) {
        int d=date.compareTo(o.date);
        if(d==0){
            int n=name.compareTo(o.name);
            if(n==0){
                return Integer.compare(size,o.size);
            }
            return name.compareTo(o.name);
        }
        return date.compareTo(o.date);
    }
    public String toMonthDay() {
        return String.format("%s-%d", date.getMonth(),
                date.getDayOfMonth());
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, date);
    }
}