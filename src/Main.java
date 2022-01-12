import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

      Model model = new Model();
      Controller controller = new Controller(new Scanner(System.in), model);
      controller.run();
    }
}
