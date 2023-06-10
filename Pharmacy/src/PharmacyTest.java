import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PharmacyTest {
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bf.readLine());
        CBHT<String, List<Lek>> tbl = new CBHT<String, List<Lek>>(2*n);
        for(int i=0;i<n;i++){
            String[] split = bf.readLine().split("\\s+");
            Lek k = new Lek(split[0].toUpperCase(),Integer.parseInt(split[1]),Integer.parseInt(split[2]),Integer.parseInt(split[3]));
            if(tbl.search(split[0].substring(0,3)) == null){
                List<Lek> lekovi = new ArrayList<Lek>();
                lekovi.add(k);
                tbl.insert(split[0].substring(0,3).toUpperCase(),lekovi);
            }
            else{
                List<Lek> lekovi = tbl.search(split[0].substring(0,3)).element.value;
                lekovi.add(k);
                tbl.insert(split[0].substring(0,3).toUpperCase(),lekovi);
            }
        }
        while (true){
            String line = bf.readLine();
            if(line.equals("KRAJ")) break;
            String lek = line.toUpperCase();
            if(tbl.search(lek.substring(0,3)) == null){
                System.out.println("Nema takov lek");
                bf.readLine();
                continue;
            }
            boolean found = false;
            for (Lek l : tbl.search(lek.substring(0, 3)).element.value) {
                if(l.getName().equals(lek)){
                    found = true;
                    int broj = Integer.parseInt(bf.readLine());
                    System.out.println(l);
                    if(l.getBroj()<broj){
                        System.out.println("Nema dovolno lekovi");
                        continue;
                    }
                    l.setBroj(l.getBroj()-broj);
                    System.out.println("Napravena naracka");
                    break;
                }
            }
            if(!found){
                bf.readLine();
                System.out.println("Nema takov lek");
            }
        }
    }
}

class Lek{
    private String name;
    private int poz;
    private int cena;
    private int broj;

    public void setBroj(int broj) {
        this.broj = broj;
    }

    public Lek(String name, int poz, int cena, int broj) {
        this.name = name;
        this.poz = poz;
        this.cena = cena;
        this.broj = broj;
    }

    public String getName() {
        return name;
    }

    public int getPoz() {
        return poz;
    }

    public int getCena() {
        return cena;
    }

    public int getBroj() {
        return broj;
    }

    @Override
    public String toString() {
        return name+"\n"+(poz==0?"NEG":"POZ")+"\n"+cena+"\n"+broj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lek lek = (Lek) o;
        return poz == lek.poz &&
                cena == lek.cena &&
                broj == lek.broj &&
                name.equals(lek.name);
    }

    @Override
    public int hashCode() {
        return (29 * (29 * (29 * 0 + name.charAt(0)) + name.charAt(1)) % 102780 + name.charAt(2));
    }
}

class CBHT<K extends Comparable<K>, E> {

    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
          int b = hash(targetKey);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (targetKey.equals(((MapEntry<K, E>) curr.element).key))
                return curr;
        }
        return null;
    }

    public void insert(K key, E val) {
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                curr.element = newEntry;
                return;
            }
        }
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(((MapEntry<K,E>) curr.element).key)) {
                if (pred == null)
                    buckets[b] = curr.succ;
                else
                    pred.succ = curr.succ;
                return;
            }
        }
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            for (SLLNode<MapEntry<K,E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                temp += curr.element.toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

}
class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {
    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "<" + key + "," + value + ">";
    }
}
class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}