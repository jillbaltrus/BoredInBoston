import java.util.List;

public interface IBusiness {

  String getName();

  String getAddress();

  BusinessType getType();

  List<String> getCategories();

  Hours getHours();

  String announce();

  void addUniqueCategory(List<String> categories);

  boolean checkRating(double minRating);
}
