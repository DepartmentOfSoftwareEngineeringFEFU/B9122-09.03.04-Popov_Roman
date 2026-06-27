package plugin.prep.auth;

import java.io.*;
import java.nio.file.*;

import lombok.*;

public class IoUtils {

    @SneakyThrows
    public static String readResource(String resourceName) {
        var cl = IoUtils.class.getClassLoader();
        try (var resource = cl.getResourceAsStream(resourceName)) {
            var content = resource.readAllBytes();
            var result = new String(content);
            return result.trim();
        }
    }

    @SneakyThrows
    public static void writeResource(String resourceName, byte[] bytes) {
        var absPath = "/absolute/path/to/src/test/resources/";
        var path = Path.of(absPath + resourceName);
        try (var fos = new FileOutputStream(path.toFile())) {
            fos.write(bytes);
        }
    }

}
