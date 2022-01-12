import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Hours {

  /*
  private Map<DayOfWeek, LocalTime> mondayOpen;
  private Map<DayOfWeek, LocalTime> mondayClose;
  private Map<DayOfWeek, LocalTime> tuesdayOpen;
  private Map<DayOfWeek, LocalTime> tuesdayClose;
  private Map<DayOfWeek, LocalTime> wednesdayOpen;
  private Map<DayOfWeek, LocalTime> wednesdayClose;
  private Map<DayOfWeek, LocalTime> thursdayOpen;
  private Map<DayOfWeek, LocalTime> thursdayClose;
  private Map<DayOfWeek, LocalTime> fridayOpen;
  private Map<DayOfWeek, LocalTime> fridayClose;
  private Map<DayOfWeek, LocalTime> saturdayOpen;
  private Map<DayOfWeek, LocalTime> saturdayClose;
  private Map<DayOfWeek, LocalTime> sundayOpen;
  private Map<DayOfWeek, LocalTime> sundayClose;
  */

  private Map<DayOfWeek, LocalTime[]> hours;

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
