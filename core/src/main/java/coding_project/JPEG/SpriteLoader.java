package coding_project.JPEG;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class SpriteLoader {

    private SpriteLoader() {}

    public static BufferedImage load(String path) {
        try (var stream =
                     SpriteLoader.class.getClassLoader().getResourceAsStream(path)) {

            if (stream == null) {
                throw new RuntimeException("Resource not found on classpath: " + path);
            }

            return ImageIO.read(stream);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite: " + path, e);
        }
    }
}