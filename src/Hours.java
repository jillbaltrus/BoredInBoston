import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class to represent the hours schedule for a business
 */
public class Hours {

  private Map<DayOfWeek, LocalTime[]> hours;

  /** Constructs an instance of an Hours object. Hours are represented as a hash map where the key
   * is a weekday and the value is an array of times to represent opening and closing times.
   * @param mon the string to represent hours on Monday
   * @param tues the string to represent hours on Tuesday
   * @param wed the string to represent hours on Wednesday
   * @param thurs the string to represent hours on Thursday
   * @param fri the string to represent hours on Friday
   * @param sat the string to represent hours on Saturday
   * @param sun the string to represent hours on Sunday
   */
  public Hours(String mon, String tues, String wed, String thurs, String fri, String sat,
      String sun) {
    // convert raw csv data to hours object
    Map<DayOfWeek, LocalTime[]> result = new HashMap<DayOfWeek, LocalTime[]>();
    result.put(DayOfWeek.MONDAY, this.parseHours(mon));
    result.put(DayOfWeek.TUESDAY, this.parseHours(tues));
    result.put(DayOfWeek.WEDNESDAY, this.parseHours(wed));
    result.put(DayOfWeek.THURSDAY, this.parseHours(thurs));
    result.put(DayOfWeek.FRIDAY, this.parseHours(fri));
    result.put(DayOfWeek.SATURDAY, this.parseHours(sat));
    result.put(DayOfWeek.SUNDAY, this.parseHours(sun));
    this.hours = result;
  }

  /**
   * Converts a string that represents hours for a certain day into an array of times that
   * represents that day's hours. Array[0] is the opening time and Array[1] is the closing time.
   * If a business closes after midnight, Array[1] is set to midnight and Array[2] is set to the
   * time on the following day when the business closes.
   * @param hours the string that represents the hours for a certain day
   * @return an array that represents the hours for a certain day
   */
  private LocalTime[] parseHours(String hours) {
    if (hours.equals("Closed")) {
      return new LocalTime[] {LocalTime.MIN, LocalTime.MIN};
    }
    if (hours.equals("Open 24 hours")) {
      return new LocalTime[] {LocalTime.MIN, LocalTime.MAX};
    }
    LocalTime[] result = new LocalTime[2];
    String[] splitHours = hours.split(" ");
    DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseInsensitive()
        .appendPattern("h:m a").toFormatter(Locale.ENGLISH);
    result[0] = LocalTime.parse(splitHours[0] + " " + splitHours[1], dtf); // open
    if (splitHours.length == 6) {
      if (splitHours[5].equalsIgnoreCase("(Next")
          && splitHours[6].equalsIgnoreCase("day)")) {
        result[1] = LocalTime.MAX;
        result[2] = LocalTime.parse(splitHours[3] + " " + splitHours[4], dtf);
      }
    } else {
      result[1] = LocalTime.parse(splitHours[3] + " " + splitHours[4], dtf); // close
    }
    return result;
  }

  /**
   * Determines if this business is open right now.
   * @return true if open
   */
  public boolean isOpenNow() {
    LocalTime currentTime = LocalTime.now();
    DayOfWeek day = LocalDate.now().getDayOfWeek();
    LocalTime open = hours.get(day)[0];
    LocalTime close = hours.get(day)[1];
    boolean yesterdayHoursOverflow = hours.get(day.minus(1)).length == 3;
    boolean stillOpenFromYesterday = false;
    if (yesterdayHoursOverflow) {
      stillOpenFromYesterday = currentTime.isBefore(hours.get(day.minus(1))[3]);
    }
    return (currentTime.isAfter(open) && currentTime.isBefore(close)) || stillOpenFromYesterday;
    }

  /**
   * Converts the array of today's hours into a readable string representation to be presented to
   * the user.
   * @return a string to represent today's hours.
   */
  public String formatHours() {
    if (hours.get(LocalDate.now().getDayOfWeek())[0] == LocalTime.MIN
        && hours.get(LocalDate.now().getDayOfWeek())[1] == LocalTime.MIN) {
      return String.format("Unfortunately, it is closed on %tAs.\n",
          LocalDate.now().getDayOfWeek());
    }
    String result = String.format("Its %tA hours are from %tr to ",
        LocalDate.now().getDayOfWeek(),
        hours.get(LocalDate.now().getDayOfWeek())[0]);
    if (hours.get(LocalDate.now().getDayOfWeek()).length == 2) {
      return result + String.format("%tr.\n", hours.get(LocalDate.now().getDayOfWeek())[1]);
    }
    return result + String.format("%tr tomorrow.\n",
        hours.get(LocalDate.now().getDayOfWeek())[3]);
  }
}
