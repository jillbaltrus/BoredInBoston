import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class to represent all the eligible business recommendations and operate on them.
 */
public class Model {

  List<IBusiness> businesses;

  /**
   * Creates an instance of a Model. Reads in csv files for each business category and creates a list
   * of corresponding businesses.
   * @throws FileNotFoundException if csv files cannot be found.
   */
  public Model() throws FileNotFoundException {
    this.businesses = new ArrayList<>();
    readBusinesses(new File("restaurants.csv"),
        BusinessType.RESTAURANT);
    readBusinesses(new File("arts_entertainment.csv"),
        BusinessType.CULTURAL);
    readBusinesses(new File("cafes.csv"),
        BusinessType.CAFE);
    readBusinesses(new File("active.csv"),
        BusinessType.ACTIVITY);
    readBusinesses(new File("beauty_spas.csv"),
        BusinessType.SPA);
    readBusinesses(new File("nightlife.csv"),
        BusinessType.NIGHTLIFE);
  }

  /**
   * Gets the businesses represented by this model
   * @return this model's list of businesses
   */
  public List<IBusiness> getBusinesses() {
    return this.businesses;
  }

  /**
   * Filters the list of businesses so that the remaining businesses meet the provided requirement
   * @param pred function to fitler by. Businesses that would return false for the predicate are
   *             removed.
   */
  public void filterBusinesses(Predicate<IBusiness> pred) {
    this.businesses = businesses.stream().filter(pred).collect(Collectors.toList());
  }

  /**
   * Creates a non-repeating list of categories represented by this model's businesses.
   * @return list of all this model's businesses' unique categories
   */
  public List<String> getAllUniqueCategories() {
    List<List<String>> allCategories =
        this.businesses.stream().map(b -> b.getCategories()).collect(Collectors.toList());
    List<String> flattened =
        allCategories.stream().flatMap(List::stream).distinct().
            filter(c -> !c.isEmpty()).collect(Collectors.toList());
    flattened.sort(String::compareToIgnoreCase);
    return flattened;
  }

  /**
   * Checks that there are businesses in this model
   * @return true if this model's list of businesses is not empty
   */
  public boolean anyBusinesses() {
    return this.businesses.size() > 0;
  }

  /**
   * Selects a random business in this model's list of businesses
   * @return a randomly selected business
   */
  public IBusiness chooseRandom() {
    return this.businesses.get(new Random().nextInt(this.businesses.size()));
  }

  /**
   * Parses the given CSV file and creates a list of businesses from the data, using the business
   * builder class. Adds the parsed businesses to this model's list of businesses.
   * @param csv file to read
   * @param type the business type, used for assigning the type field of a business
   * @throws FileNotFoundException if the given csv file cannot be found
   */
  private void readBusinesses(File csv, BusinessType type) throws FileNotFoundException {
    // read in data
    Scanner csvScanner = new Scanner(csv);
    csvScanner.useDelimiter("\n");
    csvScanner.next(); //skip first line
    DefaultBusiness currentBusiness;
    while (csvScanner.hasNext())
    {
      String[] fields = csvScanner.next().split(",");
      try {
        currentBusiness = new DefaultBusiness.DefaultBusinessBuilder()
            .setName(fields[0]).setType(type).setRating(fields[1]).setHours(fields[2])
            .setCategories(fields[3]).setAddress(fields[4], fields[5], fields[6])
            .build();
      } catch (IndexOutOfBoundsException e) {
        currentBusiness = new DefaultBusiness.DefaultBusinessBuilder()
            .setName(fields[0]).setType(type).setRating(fields[1]).setHours(fields[2])
            .setCategories(fields[3]).setAddress(fields[4])
            .build();
      }
      if (!this.businesses.contains(currentBusiness)) {
        this.businesses.add(currentBusiness);
      }
    }
    csvScanner.close();
  }
}
