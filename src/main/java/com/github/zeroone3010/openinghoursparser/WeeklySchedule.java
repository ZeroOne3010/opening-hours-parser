package com.github.zeroone3010.openinghoursparser;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.LinkedHashMap;

public final class WeeklySchedule {
  private LinkedHashMap<DayOfWeek, DailySchedule> schedule;

  private WeeklySchedule(final LinkedHashMap<DayOfWeek, DailySchedule> schedule) {
    this.schedule = schedule;
  }

  public DailySchedule get(final DayOfWeek dayOfWeek) {
    return schedule.get(dayOfWeek);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private LinkedHashMap<DayOfWeek, DailySchedule> dailySchedules;

    public Builder() {
      this.dailySchedules = new LinkedHashMap<>();
      Arrays.asList(DayOfWeek.values()).forEach(day -> this.dailySchedules.put(day, DailySchedule.closed()));
    }


    public Builder add(final DayOfWeek dayOfWeek, final DailySchedule scheduleOfDay) {
      this.dailySchedules.put(dayOfWeek, scheduleOfDay);
      return this;
    }

    public WeeklySchedule build() {
      return new WeeklySchedule(dailySchedules);
    }
  }
}
