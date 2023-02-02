package com.ai;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Dataset {
    private static final int LABEL_MAGIC_NUMBER = 2049;
    private static final int IMAGE_MAGIC_NUMBER = 2051;

    public static int[][] readImages(String fileName) throws IOException {
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            int magic = input.readInt();
            if (magic != IMAGE_MAGIC_NUMBER) {
                throw new IOException("Invalid magic number in file");
            }

            int numImages = input.readInt();
            int numRows = input.readInt();
            int numColums = input.readInt();
            int[][] images = new int[numImages][numRows * numColums];
            for (int i = 0; i < numImages; i++) {
                for (int j = 0; j < numRows * numColums; j++) {
                    images[i][j] = input.readUnsignedByte();
                }
            }

            return images;
        }
    }

    public static int[] readLabels(String fileName) throws IOException {
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            int magic = input.readInt();
            if (magic != LABEL_MAGIC_NUMBER) {
                throw new IOException("Invalid magic number in file!");
            }

            int numLabels = input.readInt();
            int[] labels = new int[numLabels];
            for (int i = 0; i < numLabels; i++) {
                labels[i] = input.readByte();
            }

            return labels;
        }
    }
}
