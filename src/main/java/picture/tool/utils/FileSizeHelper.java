package picture.tool.utils;

import java.text.DecimalFormat;

public class FileSizeHelper {

  private static final String[] units = {"B", "KB", "MB", "GB"};

  private static final long carry = 1024;

  public static String format(final long size) {
    int index = 0;
    double num = size;
    while (num >= 1024 && index < units.length - 1) {
      num /= carry;
      index++;
    }
    DecimalFormat df = new DecimalFormat("#.##");
    return df.format(num) + " " + units[index];
  }
}
