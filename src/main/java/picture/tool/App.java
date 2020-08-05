/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package picture.tool;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        File input = new File("07b0231d2c4d4809852e09bb1b8ac9df.jpg");
        BufferedImage image = ImageIO.read(input);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.1f);  // Change the quality value you prefer
        writer.write(null, new IIOImage(image, null, null), param);
        os.close();
        ios.close();
        writer.dispose();
        System.out.println("size:" + os.size() + "k.");
    }
}
