package com.github.zeroone3010.openinghoursparser;

import java.time.LocalTime;
import java.util.Objects;

public final class DailySchedule {
  final LocalTime open;
  final LocalTime close;
  final boolean closed;

  public DailySchedule(final LocalTime open, final LocalTime close) {
    this.open = open;
    this.close = close;
    this.closed = false;
  }

  private DailySchedule(final boolean closed) {
    this.open = null;
    this.close = null;
    this.closed = closed;
  }

  public static DailySchedule closed() {
    return new DailySchedule(true);
  }

  public LocalTime getOpen() {
    return open;
  }

  public LocalTime getClose() {
    return close;
  }

  @Override
  public String toString() {
    if (closed) {
      return "Closed";
    }
    return "Open " + open + "-" + close;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final DailySchedule that = (DailySchedule) o;
    return Objects.equals(getOpen(), that.getOpen()) &&
        Objects.equals(getClose(), that.getClose());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getOpen(), getClose());
  }
}
