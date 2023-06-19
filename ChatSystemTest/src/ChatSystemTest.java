import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            boolean flag = false;
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ){
                    System.out.println(cr.hasUser(jin.next()));
                }

            }
            if (n > 0){
                System.out.println();
            }

            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String[] params = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        try{
                            m.invoke(cs, (Object[]) params );
                        }catch (Exception ignored){

                        }
                    }
                }
            }
        }
    }

}
class ChatRoom implements Comparable<ChatRoom> {
    String name;
    Set<String> users;
    public ChatRoom(String name){
        this.name = name;
        this.users = new TreeSet<>();
    }
    public boolean hasUser(String username){
        return users.contains(username);
    }
    public void addUser(String username){
        users.add(username);
    }
    public void removeUser(String username){
        if (hasUser(username)){
            users.remove(username);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name).append("\n");
        if (numUsers() > 0){
            for(String user : users){
                sb.append(user).append("\n");
            }
        }
        else {
            sb.append("EMPTY\n");
        }
        return sb.toString();
    }
    public int numUsers(){
        return users.size();
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ChatRoom o) {
        return Comparator.comparing(ChatRoom::getName).compare(this, o);
    }
}
class ChatSystem  {
    Map<String, ChatRoom> chatRooms;
    Set<String> users;
    public ChatSystem() {
        chatRooms = new TreeMap<>();
        users = new HashSet<>();
    }
    public void addRoom(String roomName){
        chatRooms.put(roomName, new ChatRoom(roomName));
    }
    public void removeRoom(String roomName){
        chatRooms.remove(roomName);
    }
    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (chatRooms.containsKey(roomName)){
            return chatRooms.get(roomName);
        }
        throw new NoSuchRoomException(roomName);
    }
    public void register(String userName){
        users.add(userName);
        List<ChatRoom>tmp = chatRooms
                .values()
                .stream()
                .sorted(Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getName))
                .collect(Collectors.toList());
        tmp.get(0).addUser(userName);
    }
    public void registerAndJoin(String userName, String roomName){
        users.add(userName);
        chatRooms.get(roomName).addUser(userName);
    }
    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        ChatRoom tmp = getRoom(roomName);
        if(!users.contains(userName)){
            throw new NoSuchUserException(userName);
        }
        tmp.addUser(userName);
    }
    public void leaveRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        ChatRoom tmp = getRoom(roomName);
        if(!users.contains(userName)){
            throw new NoSuchUserException(userName);
        }
        tmp.removeUser(userName);
    }
    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if(!users.contains(friend_username)){
            throw new NoSuchUserException(friend_username);
        }
        for (ChatRoom room : chatRooms.values()){
            if(room.hasUser(friend_username)){
                room.addUser(username);
            }
        }
    }
}
class NoSuchRoomException extends Exception{
    public NoSuchRoomException(String message) {
        super(String.format("No such room %s", message));
    }
}
class NoSuchUserException extends Exception{

    public NoSuchUserException(String userName) {
        super(String.format("No such username %s", userName));

    }
}