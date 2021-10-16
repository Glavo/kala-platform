package kala.platform;

/**
 * The supported CPU architectures.
 */
public enum Arch {

    /**
     * Unknown Arch
     */
    UNKNOWN(false),

    /**
     * X86
     */
    X86(false),

    /**
     * X86_64
     */
    X86_64(true),

    /**
     * ARM 32 bit
     */
    ARM(false),

    /**
     * ARM 64 bit (AARCH64)
     */
    ARM64(true),

    /**
     * Power PC 32 bit
     */
    PPC(false),

    /**
     * Power PC 64 bit
     */
    PPC64(true),

    /**
     * Power PC 64 bit little endian
     */
    PPC64LE(true),

    /**
     * IBM zSeries S/390 64 bit
     */
    S390X(true),

    /**
     * Sun sparc 32 bit
     */
    SPARC(false),

    /**
     * Sun sparc 64 bit
     */
    SPARCV9(true),

    /**
     * MIPS 32 bit
     */
    MIPS(false),

    /**
     * MIPS 64 bit
     */
    MIPS64(true),

    /**
     * MIPS 32 bit big endian
     */
    MIPSEL(false),

    /**
     * MIPS 64 bit big endian
     */
    MIPS64EL(true),
    ;
    public static final Arch CURRENT = detectArch();

    private final String archName = name().toLowerCase();
    private final boolean is64bit;

    Arch(boolean is64bit) {
        this.is64bit = is64bit;
    }

    public String getArchName() {
        return archName;
    }

    public boolean is64Bit() {
        return is64bit;
    }

    @Override
    public String toString() {
        return getArchName();
    }

    private static Arch detectArch() {
        String arch = System.getProperty("os.arch").toLowerCase().trim();
        if ("x86".equals(arch) || "i386".equals(arch) || "i486".equals(arch) || "i586".equals(arch) || "i686".equals(arch)) {
            return X86;
        } else if ("x64".equals(arch) || "x86-64".equals(arch) || "x86_64".equals(arch) || "amd64".equals(arch)) {
            return X86_64;
        } else if ("ppc".equals(arch) || "powerpc".equals(arch)) {
            return PPC;
        } else if ("ppc64".equals(arch) || "powerpc64".equals(arch)) {
            if ("little".equals(System.getProperty("sun.cpu.endian"))) {
                return PPC64LE;
            }
            return PPC64;
        } else if ("ppc64le".equals(arch) || "powerpc64le".equals(arch)) {
            return PPC64LE;
        } else if ("s390".equals(arch) || "s390x".equals(arch)) {
            return S390X;
        } else if ("sparc".equals(arch)) {
            return SPARC;
        } else if ("sparv9c".equals(arch)) {
            return SPARCV9;
        } else if ("mips".equals(arch)) {
            return MIPS;
        } else if ("mips64".equals(arch)) {
            return MIPS64;
        } else if ("mipsel".equals(arch)) {
            return MIPSEL;
        } else if ("mips64el".equals(arch)) {
            return MIPS64EL;
        }

        if (arch.startsWith("aarch64") || arch.startsWith("armv8") || arch.startsWith("arm64")) {
            return ARM64;
        } else if (arch.startsWith("arm")) {
            return ARM;
        }

        return UNKNOWN;
    }
}
