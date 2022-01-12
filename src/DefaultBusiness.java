import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultBusiness implements IBusiness {

  private String name;
  private Hours hours;
  private double rating;
  private BusinessType type;
  protected List<String> categories;
  protected String tags;
  private String address;

  protected DefaultBusiness(DefaultBusinessBuilder builder) {
    this.name = builder.name;
    this.hours = builder.hours;
    this.rating = builder.rating;
    this.type = builder.type;
    this.categories = builder.categories;
    this.address = builder.address;
    this.tags = builder.tags;
  }

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

  @Override
  public final int hashCode() {
    return this.name.hashCode() + this.address.hashCode() + this.type.hashCode();
  }

  public String getName() {
    return this.name;
  }

  public BusinessType getType() {
    return this.type;
  }

  public String getAddress() {
    return this.address;
  }

  public List<String> getCategories() {
    return this.categories;
  }

  public Hours getHours() {
    return this.hours;
  }

  public boolean checkRating(double minRating) {
    return this.rating >= minRating;
  }

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

  public void addUniqueCategory(List<String> categories) {
    for (String category : this.categories) {
      if (!categories.contains(category)) {
        categories.add(category);
      }
    }
  }

  public static class DefaultBusinessBuilder { // might need static here?
    private String name;
    private Hours hours;
    private double rating;
    private BusinessType type;
    protected List<String> categories;
    protected String tags;
    private String address;

    public DefaultBusinessBuilder() {
      //this.tags = amenities;
    }

    public DefaultBusiness build() {
      return new DefaultBusiness(this);
    }

    public DefaultBusinessBuilder setType(BusinessType type) {
      this.type = type;
      return this;
    }

    public DefaultBusinessBuilder setHours(String hoursList) {
      if (hoursList == null || hoursList.isEmpty()) {
        return this;
      }
      String[] hours = hoursList.split(";");
      this.hours = new Hours(hours[0], hours[1], hours[2], hours[3], hours[4], hours[5], hours[6]);
      return this;
    }

    public DefaultBusinessBuilder setRating(String rating) {
      try {
        this.rating = Double.parseDouble(rating.split(" ")[0]);
      } catch (Exception e) {
        this.rating = 0.0;
      }
      return this;
    }

    public DefaultBusinessBuilder setName(String name) {
      this.name = name;
      return this;
    }

    public DefaultBusinessBuilder setCategories(String categories) {
      this.categories = Arrays.asList(categories.split(";")).stream().map(c -> c = c.replace("\"", "")).collect(
          Collectors.toList());
      return this;
    }

    public DefaultBusinessBuilder setAddress(String address1, String address2, String address3) {
      String addr = String.join(", ", address1, address2, address3);
      this.address = addr.substring(1, addr.length() - 2);
      return this;
    }

    public DefaultBusinessBuilder setAddress(String address) {
      this.address = address.replace("\"", "");
      return this;
    }
  }
}
