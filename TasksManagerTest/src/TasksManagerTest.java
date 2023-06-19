import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class TasksManagerTest {

    public static void main(String[] args) throws DeadlineNotValidException {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
abstract class ITask{
    String category;
    String name;
    String description;
    int priority;
    LocalDateTime deadline;
    public abstract LocalDateTime getDeadline();
    public abstract int getPriority();
    public abstract String getCategory();
}
class Task extends ITask{
    public Task(String line) {
        String []parts = line.split(",");
        this.category = parts[0];
        this.name = parts[1];
        this.description = parts[2];
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
class TaskPriority extends  ITask{

    public TaskPriority(String line) {
        String []parts = line.split(",");
        this.category = parts[0];
        this.name = parts[1];
        this.description = parts[2];
        this.priority = Integer.parseInt(parts[3]);
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }
}
class TaskDeadline extends ITask{
    LocalDateTime deadline;

    public TaskDeadline(String line) throws DeadlineNotValidException {
        if (LocalDateTime.parse(line.split(",")[3]).isBefore(LocalDateTime.of(2020, Month.MARCH, 5, 12, 4))){
            throw new DeadlineNotValidException(line.split(",")[3]);
        }
        String []parts = line.split(",");
        this.category = parts[0];
        this.name = parts[1];
        this.description = parts[2];
        this.deadline = LocalDateTime.parse(parts[3]);
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                '}';
    }
}
class TaskPriorityDeadline extends ITask{
    int priority;
    LocalDateTime deadline;

    public TaskPriorityDeadline(String line) throws DeadlineNotValidException {
        if (LocalDateTime.parse(line.split(",")[3]).isBefore(LocalDateTime.of(2020, Month.MARCH, 5, 12, 4))){
            throw new DeadlineNotValidException(line.split(",")[3]);
        }
        String []parts = line.split(",");
        this.category = parts[0];
        this.name = parts[1];
        this.description = parts[2];
        this.deadline = LocalDateTime.parse(parts[3]);
        this.priority = Integer.parseInt(parts[4]);
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}
class TaskManager{
    List<ITask> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void readTasks (InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.split(",").length == 3){
                tasks.add(new Task(line));
            } else if (line.split(",").length == 4) {
                if (line.split(",")[3].split(":").length > 1){
                    try{
                        tasks.add(new TaskDeadline(line));
                    }catch (DeadlineNotValidException e){
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    tasks.add(new TaskPriority(line));
                }
            }
            else {

                try{
                    tasks.add(new TaskPriorityDeadline(line));
                }catch (DeadlineNotValidException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory){
        PrintWriter writer = new PrintWriter(os);

        /*Map<String, List<ITask>> category = tasks
                .stream()
                .collect(Collectors.groupingBy(ITask::getCategory,
                        (Supplier<Map<String, List<ITask>>>)new HashMap<>()),
                        )*/

        if (includePriority && !includeCategory){
            tasks
                    .stream()
                    .sorted(Comparator
                            .comparing(ITask::getPriority)
                            .thenComparing(x->Duration.between(LocalDateTime.now(), x.getDeadline())))
                    .forEach(writer::println);
        }
        else if(!includePriority && !includeCategory){
            tasks
                    .stream()
                    .sorted(Comparator
                            .comparing(x->Duration.between(LocalDateTime.now(), x.getDeadline())))
                    .forEach(writer::println);
        }
        else if(includePriority && includeCategory){
            tasks
                    .stream()
                    .sorted(Comparator
                            .comparing(x->Duration.between(LocalDateTime.now(), x.getDeadline())))
                    .forEach(writer::println);
        }
        writer.flush();
    }
}
class DeadlineNotValidException extends Exception{
    public DeadlineNotValidException(String time) {
        super(String.format("The deadline %s has already passed\n", time));
    }
}