import static org.junit.jupiter.api.Assertions.*;
//
//import DefaultBusiness.DefaultBusinessBuilder;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//
//class DefaultBusinessTest {
//
//  DefaultBusiness b1 = new DefaultBusinessBuilder().setName("Pho Basil").
//      setType(BusinessType.RESTAURANT).setCategories("Thai;Asian").
//      setRating("4.5 stars").setHours("5:00 PM - 11:00 PM;5:00 PM - 11:00 PM;5:00 PM - 11:00 PM;"
//      + "5:00 PM - 11:00 PM;5:00 PM - 12:00 AM (Next day);10:30 AM - 12:00 AM (Next day);"
//      + "10:30 AM - 11:00 PM").setAddress("\"505 Washington St, Boston, MA 02111\"").build();
//  DefaultBusiness b2 = new DefaultBusinessBuilder().setName("Pho Basil").
//      setType(BusinessType.RESTAURANT).setCategories("Not Thai;Different").
//      setRating(null).setHours(null).setAddress("\"505 Washington St, Boston, MA 02111\"")
//      .build();
//  DefaultBusiness b3 = new DefaultBusinessBuilder().setName("Rocky Road").
//      setType(BusinessType.ACTIVITY).setCategories("Active;Rock Climbing").
//      setRating("4.5 stars").setHours("10:00 AM - 12:00 AM (Next day);"
//      + "6:30 AM - 12:00 AM (Next day);10:00 AM - 12:00 AM (Next day);"
//      + "6:30 AM - 12:00 AM (Next day);10:00 AM - 12:00 AM (Next day);"
//      + "10:00 AM - 12:00 AM (Next day);10:00 AM - 12:00 AM (Next day)")
//      .setAddress("\"101 Fake Address, Boston, MA 12345\"").build();
//
//  @Test
//  void testEquals() {
//    assertTrue(b1.equals(b2));
//    assertTrue(b2.equals(b1));
//    assertFalse(b3.equals(b1));
//    assertFalse(b2.equals(b3));
//    assertTrue(b2.equals(b2));
//  }
//
//  @Test
//  void testHashCode() {
//    assertTrue(b1.hashCode() == b2.hashCode());
//    assertTrue(b2.hashCode() == b1.hashCode());
//    assertTrue(b2.hashCode() == b2.hashCode());
//  }
//
//  @Test
//  void getName() {
//    assertEquals("Pho Basil", b1.getName());
//    assertEquals("Pho Basil", b2.getName());
//    assertEquals("Rocky Road", b3.getName());
//  }
//
//  @Test
//  void getType() {
//    assertEquals(BusinessType.RESTAURANT, b1.getType());
//    assertEquals(BusinessType.RESTAURANT, b2.getType());
//    assertEquals(BusinessType.ACTIVITY, b3.getType());
//  }
//
//  @Test
//  void getAddress() {
//    assertEquals("505 Washington St, Boston, MA 02111", b1.getAddress());
//    assertEquals("505 Washington St, Boston, MA 02111", b2.getAddress());
//    assertEquals("101 Fake Address, Boston, MA 12345", b3.getAddress());
//  }
//
//  @Test
//  void getCategories() {
//    List<String> expected1 = new ArrayList<>();
//    expected1.add("Thai");
//    expected1.add("Asian");
//    List<String> expected2 = new ArrayList<>();
//    expected2.add("Not Thai");
//    expected2.add("Different");
//    List<String> expected3 = new ArrayList<>();
//    expected3.add("Active");
//    expected3.add("Rock Climbing");
//    assertEquals(expected1, b1.getCategories());
//    assertEquals(expected2, b2.getCategories());
//    assertEquals(expected3, b3.getCategories());
//  }
//
//  @Test
//  void getHours() {
//  }
//
//  @Test
//  void checkRating() {
//  }
//
//  @Test
//  void announce() {
//  }
//
//  @Test
//  void addUniqueCategory() {
//  }
//}