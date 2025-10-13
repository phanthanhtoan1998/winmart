package com.winmart.common.file.excel;

/*
 * Helper class to hold image metadata and data.
 */
public class ImageData {
    private final int row;
    private final int col;
    private final byte[] data;
    private final String extension;

    public ImageData(int row, int col, byte[] data, String extension) {
        this.row = row;
        this.col = col;
        this.data = data;
        this.extension = extension;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public byte[] getData() { return data; }
    public String getExtension() { return extension; }
}