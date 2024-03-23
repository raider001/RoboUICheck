package com.kalynx.uitestframework.data;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

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

    public Result<TemplateContainer> findImage(Path imagePath) {
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
        Mat template = Imgcodecs.imread(p.resolve(imagePath).toAbsolutePath().toString(),Imgcodecs.IMREAD_COLOR);
        Mat mask = Imgcodecs.imread(p.resolve(imagePath).toAbsolutePath().toString(),Imgcodecs.IMREAD_COLOR);
        Imgproc.cvtColor(mask,mask,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(mask,mask,0,255,Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite("mask.png",mask);
        return new SuccessfulResult<>(Optional.of(new TemplateContainer(template, mask)), "Image Found");
    }

    public class TemplateContainer {
        public final Mat template;
        public final Mat mask;

        private TemplateContainer(Mat template, Mat mask) {
            this.template = template;
            this.mask = mask;
        }
    }
}
