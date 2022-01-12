import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Model {

  List<IBusiness> businesses;

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

  public List<IBusiness> getBusinesses() {
    return this.businesses;
  }

  public void filterBusinesses(Predicate<IBusiness> pred) {
    this.businesses = businesses.stream().filter(pred).collect(Collectors.toList());
  }

  /**
   * Method that accepts a list of businesses and returns a list of all unique categories
   * present in the businesses.
   * @return list of all unique categories
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

  public boolean anyBusinesses() {
    return this.businesses.size() > 0;
  }

  public IBusiness chooseRandom() {
    return this.businesses.get(new Random().nextInt(this.businesses.size()));
  }

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
