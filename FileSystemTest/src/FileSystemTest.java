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
class File implements Comparable<File>{
    char folder;
    String name;
    int size;
    LocalDateTime created;

    public File(char folder, String name, int size, LocalDateTime created) {
        this.folder = folder;
        this.name = name;
        this.size = size;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, created);
    }
    @Override
    public int compareTo(File o) {
        return Comparator.comparing(File::getCreated)
                .thenComparing(File::getName)
                .thenComparing(File::getSize)
                .compare(this, o);
    }
    public String toMonthDay() {
        return String.format("%s-%d", created.getMonth(),
                created.getDayOfMonth());
    }
}
class FileSystem{
    Map<Character, Set<File>> files;
    static final Comparator<File> FILE_COMPARATOR = Comparator.comparing(File::getCreated)
            .thenComparing(File::getName)
            .thenComparing(File::getSize);

    public FileSystem() {
        this.files = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        Set<File> computed = files.computeIfAbsent(folder, v -> new TreeSet<>(FILE_COMPARATOR));
        computed.add(new File(folder, name, size, createdAt));
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return files
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(i -> i.name.startsWith(".") && i.size < size)
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        return files
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(i -> folders.contains(i.folder))
                .mapToInt(i -> i.size)
                .sum();
    }
    public Map<Integer, Set<File>> byYear() {
        return files
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(
                        Collectors.groupingBy(
                                file -> file.getCreated().getYear(),
                                Collectors.toSet())
                );
    }
    public Map<String, Long> sizeByMonthAndDay(){
        return files
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        File::toMonthDay,
                        Collectors.summingLong(File::getSize)
                ));
    }
}