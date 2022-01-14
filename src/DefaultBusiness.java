import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representation of a single business and its relevant information.
 */
public class DefaultBusiness implements IBusiness {

  private String name;
  private Hours hours;
  private double rating;
  private BusinessType type;
  protected List<String> categories;
  protected String tags;
  private String address;

  /**
   * Creates an instance of a business based off the provided builder; sets all the business' field
   * to be equal to the corresponding fields of the builder.
   * @param builder the builder used to base this DefaultBusiness off of; copies all fields over.
   */
  private DefaultBusiness(DefaultBusinessBuilder builder) {
    this.name = builder.name;
    this.hours = builder.hours;
    this.rating = builder.rating;
    this.type = builder.type;
    this.categories = builder.categories;
    this.address = builder.address;
    this.tags = builder.tags;
  }

  /**
   * Determines whether this DefaultBusiness is equivalent to some other object. Two businesses
   * are considered equal if they have the same name, address and type.
   * @param other the other object to determine equality against.
   * @return true if the objects are equal
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof IBusiness)) {
      return false;
    }
    IBusiness castedOther = (IBusiness) other;
    return (this.name.equals(castedOther.getName()) &&
        this.address.equals(castedOther.getAddress()) &&
        this.type.equals(castedOther.getType()));
  }

  /**
   * Generates a specific integer code for this instance. Two equal businesses will have the same
   * hashcode.
   * @return the hashcode
   */
  @Override
  public final int hashCode() {
    return this.name.hashCode() + this.address.hashCode() + this.type.hashCode();
  }

  /**
   * Get the name of this business.
   * @return this business' name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Get the type of this business.
   * @return this business' type
   */
  public BusinessType getType() {
    return this.type;
  }

  /**
   * Get the address of this business.
   * @return this business' address
   */
  public String getAddress() {
    return this.address;
  }

  /**
   * Get the list of categories this business is classified as.
   * @return this business' category list
   */
  public List<String> getCategories() {
    return this.categories;
  }

  /**
   * Get the hours of this business.
   * @return this business' hours
   */
  public Hours getHours() {
    return this.hours;
  }

  /**
   * Checks whether this business' rating is at least as high as the provided rating.
   * @return true if this business' rating is equal to or greater than the provided rating.
   */
  public boolean checkRating(double minRating) {
    return this.rating >= minRating;
  }

  /**
   * Assumes this business is the selected recommendation. Formats the recommendation announcement
   * and this business' information into a string to be presented to the user.
   * @return the formatted string about this business to show the user
   */
  public String announce() {
    String result = "Your final recommendation is: " + this.name + "!\n";
    if (this.categories.size() > 0) {
      result += String.format("%s is in the %s and %s categories.\n", this.name,
          String.join(" , ", this.categories).toLowerCase(),
          this.type.toString().toLowerCase());
    } else {
      result += String.format("%s is categorized as a %s.\n", this.name,
          this.type.toString().toLowerCase());
    }
    if (this.rating == 0) {
      result += "There was no rating found on Yelp.\n";
    } else {
      result += String.format("It's rated %s stars.\n",
          new DecimalFormat("0.0").format(this.rating));
    }
    if (this.hours == null) {
      result += "There was no hours information found on Yelp.\n";
    } else {
      result += this.hours.formatHours();
    }
    if (this.address.equals("N/A")) {
      result += "There was no address information found on Yelp.\n";
    } else {
      result += String.format("Its address is: %s.\n", this.address);
    }
    result += "We hope you enjoy your recommendation and won't be bored in Boston anymore!";
    return result;
  }

  /**
   * Builder class responsible for configuring a builder object to eventually be built into
   * a business object.
   */
  public static class DefaultBusinessBuilder { // might need static here?
    private String name;
    private Hours hours;
    private double rating;
    private BusinessType type;
    protected List<String> categories;
    protected String tags;
    private String address;

    /**
     * Creates an instance of a DefaultBusinessBuilder with all fields null.
     */
    public DefaultBusinessBuilder() {
    }

    /**
     * Creates a DefaultBusiness object from this builder; all the business' fields will be equal
     * to the fields of this builder.
     * @return the new DefaultBusiness object
     */
    public DefaultBusiness build() {
      return new DefaultBusiness(this);
    }

    /**
     * Sets the type field of this builder to be the provided type
     * @return the updated DefaultBusinessBuilder object
     */
    public DefaultBusinessBuilder setType(BusinessType type) {
      this.type = type;
      return this;
    }

    /**
     * Sets the hours field of this builder to the provided hours. If no hours are provided, the
     * hours field is left null. Uses the Hours constructor to translate a list of indivual hours
     * to an Hours object.
     * @return the updated DefaultBusinessBuilder object
     */
    public DefaultBusinessBuilder setHours(String hoursList) {
      if (hoursList == null || hoursList.isEmpty()) {
        return this;
      }
      String[] hours = hoursList.split(";");
      this.hours = new Hours(hours[0], hours[1], hours[2], hours[3], hours[4], hours[5], hours[6]);
      return this;
    }

    /**
     * Sets the rating field of this builder to the provided rating. If no valid rating is provided,
     * the rating is set to 0 stars.
     * @return the updated DefaultBusinessBuilder object
     */
    public DefaultBusinessBuilder setRating(String rating) {
      try {
        this.rating = Double.parseDouble(rating.split(" ")[0]);
      } catch (Exception e) {
        this.rating = 0.0;
      }
      return this;
    }

    /**
     * Sets the name field of this builder to the provided name.
     * @return the updated DefaultBusinessBuilder object
     */
    public DefaultBusinessBuilder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * Sets the categories field of this builder to the provided categories. Categories are provided
     * as a single string that is split into its semicolon-separated individual categories.
     * @return the updated DefaultBusinessBuilder object
     */
    public DefaultBusinessBuilder setCategories(String categories) {
      this.categories = Arrays.asList(categories.split(";")).stream().
          map(c -> c = c.replace("\"", "")).collect(
          Collectors.toList());
      return this;
    }

    /** Sets the address field of this builder to the provided address. The address is passed in as
     * 3 components and strung together. The first and last characters are removed from the final
     * string, since the address is wrapped in quotation marks when it is read in.
     * @param address1 the first address component, ex: "123 Main Street"
     * @param address2 the second address component, ex: "Boston"
     * @param address3 the third address component, ex: "MA 02115"
     * @return the updated DefaultBusinessBuilder object
     */
    public DefaultBusinessBuilder setAddress(String address1, String address2, String address3) {
      String addr = String.join(", ", address1, address2, address3);
      this.address = addr.substring(1, addr.length() - 2);
      return this;
    }

    /** An alternative method for setting the address field; used for businesses that either have
     * no address or a shorter address (i.e., simply a street address and no town/state/zip info)
     * @param address the available address string
     * @return the updated DefaultBusinessBuilder object
     */
    public DefaultBusinessBuilder setAddress(String address) {
      this.address = address.replace("\"", "");
      return this;
    }
  }
}
