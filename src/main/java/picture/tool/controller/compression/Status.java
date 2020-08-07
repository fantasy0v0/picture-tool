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
   * 压缩中
   */
  compressing,

  /**
   * 压缩完成
   */
  compressed,

  /**
   * 压缩失败
   */
  error
}
