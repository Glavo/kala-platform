package kala.platform;

import java.util.Locale;

/**
 * The supported CPU architectures.
 */
public enum Architecture {

    /**
     * Unknown Arch
     */
    UNKNOWN(false, "Unknown"),

    /**
     * X86
     */
    X86(false, "x86"),

    /**
     * X86_64
     */
    X86_64(true, "x86-64"),

    IA64(true, "IA-64"),

    /**
     * ARM 32 bit
     */
    ARM(false),

    /**
     * ARM 64 bit (AARCH64)
     */
    AARCH64(true, "AArch64"),

    /**
     * Power PC 32 bit
     */
    PPC(false, "PowerPC"),

    /**
     * Power PC 32 bit
     */
    PPCLE(false, "PowerPC (Little-Endian)"),

    /**
     * Power PC 64 bit
     */
    PPC64(true, "PowerPC-64"),

    /**
     * Power PC 64 bit little endian
     */
    PPC64LE(true, "PowerPC-64 (Little-Endian)"),

    /**
     * IBM zSeries S/390 32 bit
     */
    S390(false),

    /**
     * IBM zSeries S/390 64 bit
     */
    S390X(true, "S390x"),

    /**
     * Sun sparc 32 bit
     */
    SPARC(false),

    /**
     * Sun sparc 64 bit
     */
    SPARCV9(true, "SPARC V9"),

    /**
     * MIPS 32 bit
     */
    MIPS(false),

    /**
     * MIPS 64 bit
     */
    MIPS64(true),

    /**
     * MIPS 32 bit little endian
     */
    MIPSEL(false, "MIPSel"),

    /**
     * MIPS 64 bit little endian
     */
    MIPS64EL(true, "MIPS64el"),

    /**
     * RISC-V
     */
    RISCV(true, "RISC-V"),

    /**
     * LoongArch64
     */
    LOONG_ARCH64(true, "LoongArch64")
    ;

    private final String normalizedName = name().toLowerCase(Locale.ROOT);
    private final String displayName;
    private final boolean is64bit;

    Architecture(boolean is64bit) {
        this.is64bit = is64bit;
        this.displayName = this.toString();
    }

    Architecture(boolean is64bit, String displayName) {
        this.is64bit = is64bit;
        this.displayName = displayName;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean is64Bit() {
        return is64bit;
    }

    @Override
    public String toString() {
        return getNormalizedName();
    }

    public static Architecture parseArch(String name) {
        if (name == null) {
            return UNKNOWN;
        }
        name = name.toLowerCase(Locale.ROOT).trim();

        switch (name) {
            case "x86":
            case "x86-32":
            case "x86_32":
            case "x8632":
            case "i86pc":
            case "i386":
            case "i486":
            case "i586":
            case "i686":
            case "ia32":
            case "x32":
                return X86;
            case "x8664":
            case "x86-64":
            case "x86_64":
            case "amd64":
            case "x64":
            case "ia32e":
            case "em64t":
                return X86_64;
            case "arm":
            case "arm32":
            case "aarch32":
                return ARM;
            case "arm64":
            case "aarch64":
                return AARCH64;
            case "loongarch64":

            case "ppc":
            case "powerpc":
            case "ppc32":
            case "powerpc32":
                return PPC;
            case "ppcle":
            case "powerpcle":
            case "ppc32le":
            case "powerpc32le":
                return PPCLE;
            case "ppc64":
            case "powerpc64":
                return PPC64;
            case "ppc64le":
            case "powerpc64le":
                return PPC64LE;
            case "s390":
                return S390;
            case "s390x":
                return S390X;
            case "sparc":
                return SPARC;
            case "sparcv9":
                return SPARCV9;
            case "mips":
            case "mips32":
                return MIPS;
            case "mipsel":
            case "mips32el":
                return MIPSEL;
            case "mips64":
                return MIPS64;
            case "mips64el":
                return MIPS64EL;
        }

        if (name.startsWith("armv7")) {
            return ARM;
        }
        if (name.startsWith("armv8") || name.startsWith("armv9")) {
            return AARCH64;
        }

        return UNKNOWN;
    }
}
