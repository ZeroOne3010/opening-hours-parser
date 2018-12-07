package com.github.zeroone3010.openinghoursparser;

public final class ValidationResult {
  private final boolean valid;

  public ValidationResult(final boolean valid) {
    this.valid = valid;
  }

  public boolean isValid() {
    return valid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final ValidationResult that = (ValidationResult) o;
    return valid == that.valid;
  }

  @Override
  public int hashCode() {
    return (valid ? 1 : 0);
  }

  @Override
  public String toString() {
    return "ValidationResult{" + valid + '}';
  }
}
