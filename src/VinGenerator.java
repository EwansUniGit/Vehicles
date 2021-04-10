import java.util.Random;

public class VinGenerator {
    Random r = new Random();
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVXYZ0123456789";
    String vin = "";
    public String generate() {
        for (int i = 0; i < 17; i++) {
            vin += alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return vin;
    }

}
