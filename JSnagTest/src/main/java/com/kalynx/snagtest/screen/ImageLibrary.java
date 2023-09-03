package com.kalynx.snagtest.screen;

import com.kalynx.snagtest.data.FailedResult;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.data.SuccessfulResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ImageLibrary {
    private final Set<Path> imageLibraries = new HashSet<>();

    public void addLibrary(Path newLibrary) {
        imageLibraries.add(newLibrary);
    }

    public Result<BufferedImage> findImage(Path imagePath) {
        List<Path> paths = imageLibraries.stream().filter(basePath ->  basePath.resolve(imagePath).toFile().exists()).toList();
        if(paths.isEmpty()) return new FailedResult<>("No Image in any given library called" + imagePath.toString());
        if(paths.size() > 1) {
            return new FailedResult<>("""
                    Image with given path %s exists in multiple libraries. Found in:
                    %s
                    """.formatted(imagePath, Arrays.toString(paths.toArray())));
        }
        Path p = paths.get(0);

        try {
            return new SuccessfulResult<>(Optional.of(ImageIO.read(p.toFile())));
        } catch (IOException e) {
            return new FailedResult<>("Ïmage must be either jpg or png. Given Image: " + p);
        }
    }
}
