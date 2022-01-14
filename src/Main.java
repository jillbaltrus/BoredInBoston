import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Entry point for the program
 */
public class Main {

  /**
   * Runs the project; creates an instance of the model and controller and then passes control off
   * to the Controller
   * @param args
   * @throws FileNotFoundException if csv files with business data cannot be found
   */
    public static void main(String[] args) throws FileNotFoundException {
      Model model = new Model();
      Controller controller = new Controller(new Scanner(System.in), model);
      controller.run();
    }
}
