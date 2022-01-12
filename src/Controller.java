import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {

  private final Scanner scanner;
  private Model model;

  public Controller(Scanner scanner, Model model) {
    this.scanner = scanner;
    this.model = model;
  }

  public void run() {
    List<String> BusinessTypes = Stream.of(BusinessType.values())
        .map(Enum::toString).collect(Collectors.toList());
    String greeting =
        "Welcome to Bored In Boston! We'll figure out something fun for " +
            "you to do in Boston in no time.";
    String prompt1 =
        "What are you in interested in? Choose a category by entering the "
            + "corresponding number.\n" + generateList(BusinessTypes);
    String prompt2 =
        "Good choice! Do you want to filter further? Choose a category from the following list:\n";
    String prompt3 = "Sounds good! I'll recommend something in the %s category!";
    String prompt3Alt = "Okay, I won't filter the category further.";
    String prompt4 = "Businesses are rated on a 5-star scale, where a rating of 5"
        + " is the best.\nEnter the minimum business rating you're willing to accept.";
    String prompt5 = "I'll make sure to recommend something with at least a %s star rating.";
    String prompt6 = "Generating your recommendation...";
    String failureMessage = "Oh no! Either too many incompatible filters were applied and" +
        " we couldn't find you a match,\nor none of the filtered business choices were open. "
        + "Please try again with different selections!";

    System.out.println(greeting);
    System.out.println(prompt1);
    // user works with 1-based indices, requiring -1 here
    int businessTypeSelection = scanner.nextInt() - 1;
    if (businessTypeSelection < BusinessTypes.size()) {
      BusinessType selectedBusinessType = BusinessType.valueOf(BusinessTypes
          .get(businessTypeSelection));
      this.model.filterBusinesses(b -> b.getType() == selectedBusinessType);
    }
    List<String> categoryOptions = model.getAllUniqueCategories();
    System.out.println(prompt2 + generateList(categoryOptions));
    // user works with 1-based indices, requiring -1 here
    int categorySelection = scanner.nextInt() - 1;
    if (categorySelection < categoryOptions.size()) {
      String selectedCategory = categoryOptions.get(categorySelection);
      this.model.filterBusinesses(b -> b.getCategories().contains(selectedCategory));
      System.out.println(String.format(prompt3, selectedCategory.toLowerCase()));
    } else {
      System.out.println(prompt3Alt);
    }
    System.out.println(prompt4);
    double minRating = scanner.nextDouble();
    this.model.filterBusinesses(b -> b.checkRating(minRating));
    System.out.println(String.format(prompt5, new DecimalFormat("0.0").format(minRating)));
    System.out.println(prompt6);
    this.model.filterBusinesses(b -> {
      try {
        return b.getHours().isOpenNow();
      } catch (NullPointerException ne) {
        return b.getHours() == null;
      }
    });
    if (!model.anyBusinesses()) {
      System.out.println(failureMessage);
      return;
    }
    IBusiness result = this.model.chooseRandom();
    System.out.println(result.announce());
    scanner.close();
  }

  private String generateList(List list) {
    String result = "";
    //String newline = "";
    Iterator<Object> iterator = list.iterator();
    for (int i = 1; i <= list.size(); i++) {
      result += (i + ". " + iterator.next() + "\n");
      //newline = "\n";
    }
    result += (list.size() + 1) + ". ANY";
    return result;
  }
}
