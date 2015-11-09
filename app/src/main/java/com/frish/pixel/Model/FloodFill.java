package com.frish.pixel.Model;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.LinkedList;
import java.util.Queue;

public class FloodFill {
    static public Bitmap floodFill(Bitmap image, Point node, int targetColor,
                          int replacementColor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int target = targetColor;
        int replacement = replacementColor;
        if (target != replacement) {
            Queue<Point> queue = new LinkedList<>();
            do {
                int x = node.x;
                int y = node.y;
                while (x > 0 && image.getPixel(x - 1, y) == target) {
                    x--;
                }
                boolean spanUp = false;
                boolean spanDown = false;
                while (x < width && image.getPixel(x, y) == target) {
                    image.setPixel(x, y, replacement);
                    if (!spanUp && y > 0 && image.getPixel(x, y - 1) == target) {
                        queue.add(new Point(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0
                            && image.getPixel(x, y - 1) != target) {
                        spanUp = false;
                    }
                    if (!spanDown && y < height - 1
                            && image.getPixel(x, y + 1) == target) {
                        queue.add(new Point(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1
                            && image.getPixel(x, y + 1) != target) {
                        spanDown = false;
                    }
                    x++;
                }
            } while ((node = queue.poll()) != null);
        }
        return image;
    }



    static public int[][] floodFill(int[][] pixels, int width, int height, Point node, int target,
                                   int replacement) {
        if (target != replacement) {
            Queue<Point> queue = new LinkedList<>();
            do {
                int x = node.x;
                int y = node.y;
                while (x > 0 && pixels[x-1][y] == target) {
                    x--;
                }
                boolean spanUp = false;
                boolean spanDown = false;
                while (x < width && pixels[x][y] == target) {
                    pixels[x][y] = replacement;
                    if (!spanUp && y > 0 && pixels[x][y-1] == target) {
                        queue.add(new Point(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0
                            && pixels[x][y-1] != target) {
                        spanUp = false;
                    }
                    if (!spanDown && y < height - 1
                            && pixels[x][y+1] == target) {
                        queue.add(new Point(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1
                            && pixels[x][y+1] != target) {
                        spanDown = false;
                    }
                    x++;
                }
            } while ((node = queue.poll()) != null);
        }
        return pixels;
    }
}