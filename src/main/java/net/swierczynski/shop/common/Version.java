package net.swierczynski.shop.common;

public class Version {

    public static Version zero() {
        return new Version(0);
    }

    private final int version;

    public Version(int version) {
        if (version < 0) {
            throw new IllegalArgumentException("Version cannot be zero");
        }
        this.version = version;
    }

    public int value() {
        return version;
    }

}
