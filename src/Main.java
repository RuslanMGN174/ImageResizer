import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static String srcFolder = "C:/Users/knyazev.r/Desktop/Dexter";
    private static String dstFolder = "C:/Users/knyazev.r/Desktop/New";
    private static File srcDir = new File(srcFolder);
    private static File[] files = srcDir.listFiles();

    private static int coreCount = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {

        List<File[]> filesList = new ArrayList<>();

        int start = 0;
        int end = files.length / coreCount;
        int remains = files.length;

        for (int i = 0; i < coreCount; i++) {
            filesList.add(Arrays.copyOfRange(files, start, end));
            start = end;
            int arrayLength = filesList.get(i).length;
            remains -= arrayLength;
            if (remains > arrayLength && remains < arrayLength *2 ) {
                end += arrayLength + files.length % coreCount;
            } else {
                end += arrayLength;
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);

        filesList.forEach(files1 -> executor.execute(() -> imageResize(files1, dstFolder, 300)));
        executor.shutdown();

    }


    private static void imageResize(File[] files, String dstFolder, int targetSize) {

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
