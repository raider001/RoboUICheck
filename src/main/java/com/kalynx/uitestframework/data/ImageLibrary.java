package com.kalynx.uitestframework.data;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Path;
import java.util.*;

public class ImageLibrary {
    private final Set<Path> imageLibraries = new HashSet<>();

    public void addLibrary(Path newLibrary) {
        imageLibraries.add(newLibrary);
    }

    public List<String> getLibraryPaths() {
        return imageLibraries.stream().map(Path::toString).toList();
    }

    public Result<Mat> findImage(Path imagePath) {
        List<Path> paths = imageLibraries.stream().filter(basePath ->  basePath.resolve(imagePath).toFile().exists()).toList();
        if(paths.isEmpty()) return new FailedResult<>("""
        "No Image in any given library called: '%s'
        Available Libraries:
        %s
        """.formatted(imagePath.toString(), Arrays.toString(imageLibraries.toArray())));
        if(paths.size() > 1) {
            return new FailedResult<>("""
                    Image with given path %s exists in multiple libraries. Found in:
                    %s
                    """.formatted(imagePath, Arrays.toString(paths.toArray())));
        }
        Path p = paths.get(0);
        return new SuccessfulResult<>(Optional.of(Imgcodecs.imread(p.resolve(imagePath).toAbsolutePath().toString(),Imgcodecs.IMREAD_COLOR)));
    }
}
