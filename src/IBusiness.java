import java.util.List;

/**
 * Interface to represent a Business.
 */
public interface IBusiness {

  /**
   * @return the business' name
   */
  String getName();

  /**
   * @return the business' address
   */
  String getAddress();

  /**
   * @return the business' type
   */
  BusinessType getType();

  /**
   * @return the business' categories
   */
  List<String> getCategories();

  /**
   * @return the business' hours
   */
  Hours getHours();

  /**
   * @return a string that announces a business as a final recommendation and reports its relevant
   * information
   */
  String announce();

  /**
   * Checks that a business rating meets a minimum
   * @param minRating the minimum rating
   * @return true if the business' rating is at least the minimum
   */
  boolean checkRating(double minRating);
}
