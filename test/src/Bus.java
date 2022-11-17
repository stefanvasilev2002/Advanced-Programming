import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Bus {

    public static void main(String[] args) throws Exception {
        int i,j,k;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int vozrasni = Integer.parseInt(br.readLine());
        int deca = Integer.parseInt(br.readLine());

        br.close();

        int min=0, max=0;
        if(deca<vozrasni){
            min=vozrasni*100;
        }
        else {
            min=vozrasni*100+(deca-vozrasni)*100;
        }
        max=vozrasni*100+(deca-1)*100;
        System.out.println(min);
        System.out.println(max);
    }

}