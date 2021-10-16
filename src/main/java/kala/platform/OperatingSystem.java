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
     * UNIX or Unix-like OS
     */
    UNIX,

    /**
     * IBM AIX
     */
    AIX(UNIX),

    /**
     * Oracle Solaris
     */
    SOLARIS(UNIX),

    /**
     * GNU/Linux OS
     */
    LINUX(UNIX),

    /**
     * Android
     */
    ANDROID(LINUX),

    /**
     * BSD-based OS
     */
    BSD(UNIX),

    /**
     * FreeBSD
     */
    FREEBSD(BSD),

    /**
     * OpenBSD
     */
    OPENBSD(BSD),

    /**
     * GNU/kFreeBSD
     */
    KFREEBSD(BSD),
    /**
     * NetBSD
     */
    NETBSD(BSD),

    /**
     * DragonFly BSD
     */
    DRAGONFLY(BSD),

    /**
     * Darwin OS
     */
    DARWIN(BSD),
    /**
     * Mac OS
     */
    MACOS(DARWIN),

    /**
     * IOS
     */
    IOS(DARWIN),

    /**
     * Windows
     */
    WINDOWS
    ;

    //public static final OperatingSystem CURRENT_OS = detectOS();

    private final String normalizedName = this.name().toLowerCase();
    final OperatingSystem base;

    OperatingSystem() {
        this(null);
    }

    OperatingSystem(OperatingSystem base) {
        this.base = base;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public OperatingSystem getBaseOS() {
        return base;
    }

    public boolean is(OperatingSystem os) {
        if (os == null) {
            return false;
        }

        OperatingSystem o = this;
        while (o != null) {
            if (o == os) {
                return true;
            }
            o = o.base;
        }
        return false;
    }

    public boolean isDarwin() {
        return this == DARWIN || this == MACOS || this == IOS;
    }

    public boolean isBSD() {
        return is(BSD);
    }

    public boolean isUnixLike() {
        return is(UNIX);
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
