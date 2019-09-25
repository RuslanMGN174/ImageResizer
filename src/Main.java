import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    private static String srcFolder = "C:/Users/knyazev.r/Desktop/Dexter";
    private static String dstFolder = "C:/Users/knyazev.r/Desktop/New";
    private static int processors = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(processors);
        executor.execute(() -> {
            imageResize(srcFolder, dstFolder, 300);
        });
        executor.shutdown();
    }

    private static void imageResize(String srcFolder, String dstFolder, int targetSize) {
        File srcDir = new File(srcFolder);
        File[] files = srcDir.listFiles();

        try {
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }

                BufferedImage newImage = Scalr.resize(image, Scalr.Method.SPEED, targetSize * 3);
                newImage = Scalr.resize(newImage, Scalr.Method.ULTRA_QUALITY, targetSize, Scalr.OP_ANTIALIAS);

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
