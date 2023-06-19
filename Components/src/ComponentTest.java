import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.printf("=== CHANGED COLOR (%d, %s) ===%n", weight, color);
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.printf("=== SWITCHED COMPONENTS %d <-> %d ===%n", pos1, pos2);
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}
class Component implements Comparable<Component>{
    String color;
    int weight;
    Set<Component> components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.components = new TreeSet<>();
    }
    public void addComponent(Component component){
        components.add(component);
        Window.addComponentToAll(component);
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Component o) {
        return Comparator
                .comparing(Component::getWeight)
                .thenComparing(Component::getColor)
                .compare(this, o);
    }

    public String toString(Map.Entry<Integer,Component> entry, String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d:%d:%s\n", entry.getKey(), weight, color));
        for (Component c : components){
            sb.append(c.toString(s + "---"));
        }
        return sb.toString();
    }

    public String toString(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%d:%s\n", s, weight, color));
        for (Component c : components){
            sb.append(c.toString(s + "---"));
        }
        return sb.toString();
    }
    public void changeColor(String color){
        this.color = color;
    }
}
class Window{
    String name;
    Map<Integer, Component> components;
    static List<Component> allComponents;
    public Window(String name) {
        this.name = name;
        this.components = new TreeMap<>();
        allComponents = new ArrayList<>();
    }

    public static void addComponentToAll(Component component) {
        allComponents.add(component);
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if(components.containsKey(position)){
            throw new InvalidPositionException(position);
        }
        components.put(position, component);
        allComponents.add(component);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("WINDOW ").append(name).append("\n");
        for (Map.Entry<Integer, Component> entry : components.entrySet()){
            sb.append(entry.getValue().toString(entry, ""));
        }
        return sb.toString();
    }
    public void changeColor(int weight, String color){
        /*allComponents
                .stream()
                .filter(x-> x.getWeight() < weight)
                .forEach(x-> x.changeColor(color));*/
        for(Component c : allComponents){
            if (c.getWeight() < weight){
                c.changeColor(color);
            }
        }
    }
    public void switchComponents(int pos1, int pos2){
        Component a = components.remove(pos1);
        Component b = components.remove(pos2);
        components.put(pos2, a);
        components.put(pos1, b);
    }
}
class InvalidPositionException extends Exception{

    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, alredy taken!", position));
    }
}