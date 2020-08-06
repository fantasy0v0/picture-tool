package picture.tool.controller.compression;

/**
 * 任务状态
 */
enum Status {

  /**
   * 初始状态
   */
  initial,

  /**
   * 读取中
   */
  reading,

  /**
   * 读取完成
   */
  read,

  /**
   * 压缩中
   */
  compressing,

  /**
   * 压缩完成
   */
  compressed
}
