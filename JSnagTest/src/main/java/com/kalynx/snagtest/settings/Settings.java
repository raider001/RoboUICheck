package com.kalynx.snagtest.settings;

import com.kalynx.snagtest.modelobserver.NotifyController;
import com.kalynx.snagtest.modelobserver.NotifyKey;

import java.util.function.Consumer;

public class Settings {
    private final NotifyController notifyController = new NotifyController();
    private final NotifyKey<Double> MIN_SIMILARITY_KEY = new NotifyKey<>();
    private final NotifyKey<Integer> OBSERVE_MIN_PIXELS_KEY = new NotifyKey<>();

    private double minSimilarity = 0.95;
    private int observeMinPixels = 50;

    public void setMinSimilarity(double minSimilarity) {
        if(this.minSimilarity == minSimilarity)
            return;
        notifyController.notify(MIN_SIMILARITY_KEY, minSimilarity);
        this.minSimilarity = minSimilarity;
    }

    public double getMinSimilarity() {
        return this.minSimilarity;
    }

    public void addMinSimilarityListener(Consumer<Double> listener) {
        notifyController.addListener(MIN_SIMILARITY_KEY, listener);
    }

    public void removeMinSimilarityListener(Consumer<Double> listener) {
        notifyController.removeListener(MIN_SIMILARITY_KEY, listener);
    }

    public void setMinObservePixels(int observeMinPixels) {
        if(this.observeMinPixels == observeMinPixels)
            return;
        notifyController.notify(OBSERVE_MIN_PIXELS_KEY, observeMinPixels);
        this.observeMinPixels = observeMinPixels;
    }

    public double getMinObservePixels() {
        return this.minSimilarity;
    }

    public void addMinObservePixelsListener(Consumer<Integer> listener) {
        notifyController.addListener(OBSERVE_MIN_PIXELS_KEY, listener);
    }

    public void removeMinObservePixelsListener(Consumer<Integer> listener) {
        notifyController.removeListener(OBSERVE_MIN_PIXELS_KEY, listener);
    }
}
