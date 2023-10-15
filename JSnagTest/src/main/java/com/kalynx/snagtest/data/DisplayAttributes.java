package com.kalynx.snagtest.data;

import java.awt.GraphicsDevice;

public record DisplayAttributes(int displayId, GraphicsDevice graphicsDevice, boolean primary, int x, int y, int width,
                                int height) {

}
