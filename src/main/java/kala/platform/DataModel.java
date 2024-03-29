package kala.platform;

public enum DataModel {

    ILP32(16, 32, 32, 64),
    LLP64(16, 32, 32, 64),
    LP64(16, 32, 64, 64),
    ;

    final int shortSize;
    final int intSize;
    final int longSize;
    final int longLongSize;

    DataModel(int shortSize, int intSize, int longSize, int longLongSize) {
        this.shortSize = shortSize;
        this.intSize = intSize;
        this.longSize = longSize;
        this.longLongSize = longLongSize;
    }

    public int getShortSize() {
        return shortSize;
    }

    public int getIntSize() {
        return intSize;
    }

    public int getLongSize() {
        return longSize;
    }

    public int getLongLongSize() {
        return longLongSize;
    }
}
