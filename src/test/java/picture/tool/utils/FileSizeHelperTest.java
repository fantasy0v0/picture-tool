package picture.tool.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileSizeHelperTest {

  @Test
  void format() {
    assertEquals("1 B", FileSizeHelper.format(1L));
    assertEquals("1 KB", FileSizeHelper.format(1024L));
    assertEquals("1 MB", FileSizeHelper.format(1024L * 1024));
    assertEquals("1 GB", FileSizeHelper.format(1024L * 1024 * 1024));
    assertEquals("1024 GB", FileSizeHelper.format(1024L * 1024 * 1024 * 1024));
    assertEquals("1.35 KB", FileSizeHelper.format(1378L));
    assertEquals("791 B", FileSizeHelper.format(791));
    assertEquals("13.35 KB", FileSizeHelper.format(13670));
    assertEquals("2.5 MB", FileSizeHelper.format(2621203));

  }
}
