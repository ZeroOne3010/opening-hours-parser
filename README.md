# Opening Hours Parser

A Java 8 library to parse opening hours, given in a string format, into proper Java objects.
Times and days are represented using the 
[java.time.LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html) 
and [java.time.DayOfWeek](https://docs.oracle.com/javase/8/docs/api/java/time/DayOfWeek.html) classes.

For example, the string `Mon-Wed 10:00-18:00, Thu-Sun 12:00-20:00` would be parsed into a 
`WeeklySchedule` object from whom one could query the schedule for any day of week: 
`weeklySchedule.get(DayOfWeek.TUESDAY)` would return a `DailySchedule` object with 
opening time `10:00` and closing time `18:00`.