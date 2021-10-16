package kala.platform;

import java.util.Locale;

/**
 * The supported operating systems.
 */
public enum OperatingSystem {
    /**
     * Unknown OS
     */
    UNKNOWN,

    /**
     * IBM AIX
     */
    AIX,

    /**
     * Oracle Solaris
     */
    SOLARIS,

    /**
     * GNU/Linux OS
     */
    LINUX,

    /**
     * Android
     */
    ANDROID,

    /**
     * FreeBSD
     */
    FREEBSD,

    /**
     * OpenBSD
     */
    OPENBSD,

    /**
     * GNU/kFreeBSD
     */
    KFREEBSD,
    /**
     * NetBSD
     */
    NETBSD,

    /**
     * DragonFly BSD
     */
    DRAGONFLY,

    /**
     * Darwin OS
     */
    DARWIN,
    /**
     * Mac OS
     */
    MACOS,

    /**
     * IOS
     */
    IOS,

    /**
     * Windows
     */
    WINDOWS;

    private final String normalizedName = this.toString().toLowerCase();

    public String getNormalizedName() {
        return normalizedName;
    }

    public boolean isDarwin() {
        return this == DARWIN || this == MACOS || this == IOS;
    }

    public boolean isBSD() {
        return this == FREEBSD
                || this == OPENBSD
                || this == KFREEBSD
                || this == NETBSD
                || this == DRAGONFLY
                || isDarwin();
    }

    public boolean isUnixLike() {
        return this == LINUX
                || this == ANDROID
                || this == AIX
                || this == SOLARIS
                || isBSD();
    }

    @Override
    public String toString() {
        return getNormalizedName();
    }

    public static OperatingSystem parseOperatingSystem(String name) {
        if (name == null) {
            return UNKNOWN;
        }
        name = name.toLowerCase(Locale.ROOT).trim();

        if (name.startsWith("windows")) {
            return WINDOWS;
        }
        if (name.startsWith("mac")) {
            return MACOS;
        }
        if (name.startsWith("darwin")) {
            return DARWIN;
        }

        if (name.startsWith("linux") || name.equals("gnu")) {
            return LINUX;
        }

        if (name.startsWith("aix")) {
            return AIX;
        }
        if (name.startsWith("solaris") || name.startsWith("sunos")) {
            return SOLARIS;
        }
        if (name.startsWith("freebsd")) {
            return FREEBSD;
        }
        if (name.startsWith("openbsd")) {
            return OPENBSD;
        }
        if (name.startsWith("netbsd")) {
            return NETBSD;
        }
        if (name.startsWith("dragonfly")) {
            return DRAGONFLY;
        }
        if (name.equals("gnu/kfreebsd")) {
            return KFREEBSD;
        }

        return UNKNOWN;
    }
}
