package kala.platform;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Platform {
    private final OperatingSystem operatingSystem;
    private final Architecture architecture;

    Platform(OperatingSystem operatingSystem, Architecture architecture) {
        this.architecture = architecture;
        this.operatingSystem = operatingSystem;
    }

    public static final Charset NATIVE_CHARSET;

    static {
        String nativeEncoding = System.getProperty("native.encoding", System.getProperty("sun.jnu.encoding"));
        Charset nativeCharset = Charset.defaultCharset();

        if (nativeEncoding != null) {
            try {
                nativeCharset = Charset.forName(nativeEncoding);
            } catch (UnsupportedCharsetException ignored) {
            }
        }
        NATIVE_CHARSET = nativeCharset;
    }

    public static final String CURRENT_SYSTEM_NAME;
    public static final String CURRENT_SYSTEM_VERSION;
    // public static final int CURRENT_SYSTEM_BUILD_NUMBER;
    public static final OperatingSystem CURRENT_SYSTEM;

    static {
        String osName = System.getProperty("os.name");
        OperatingSystem os = OperatingSystem.parseOperatingSystem(osName);

        if (os == OperatingSystem.DARWIN) {
            if ("robovm".equalsIgnoreCase(System.getProperty("java.vm.name"))) {
                os = OperatingSystem.IOS;
            }
        } else if (os == OperatingSystem.LINUX) {
            if ("dalvik".equalsIgnoreCase(System.getProperty("java.vm.name"))) {
                os = OperatingSystem.ANDROID;
            }
        }

        CURRENT_SYSTEM = os;

        /* too slow
        if (os == OperatingSystem.WINDOWS) {
            String versionNumber = null;
            int buildNumber = -1;

            try {
                Process process = Runtime.getRuntime().exec(new String[]{"cmd", "ver"});
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), NATIVE_CHARSET))) {
                    Matcher matcher = Pattern.compile("(?<version>[0-9]+\\.[0-9]+\\.(?<build>[0-9]+)(\\.[0-9]+)?)]$")
                            .matcher(reader.readLine().trim());

                    if (matcher.find()) {
                        versionNumber = matcher.group("version");
                        buildNumber = Integer.parseInt(matcher.group("build"));
                    }
                }
                process.destroy();
            } catch (Throwable ignored) {
            }

            if (versionNumber == null) {
                versionNumber = System.getProperty("os.version");
            }

            // Java 17 or earlier recognizes Windows 11 as Windows 10
            if (osName.equals("Windows 10") && buildNumber >= 22000) {
                osName = "Windows 11";
            }

            CURRENT_SYSTEM_NAME = osName;
            CURRENT_SYSTEM_VERSION = versionNumber;
            CURRENT_SYSTEM_BUILD_NUMBER = buildNumber;
        }
         */

        CURRENT_SYSTEM_NAME = osName;
        CURRENT_SYSTEM_VERSION = System.getProperty("os.version");
        // CURRENT_SYSTEM_BUILD_NUMBER = -1;
    }

    public static final String CURRENT_ARCH_NAME;
    public static final Architecture CURRENT_ARCH;

    public static final String SYSTEM_ARCH_NAME;
    public static final Architecture SYSTEM_ARCH;

    static {
        boolean isLE = "little".equalsIgnoreCase(System.getProperty("sun.cpu.endian"));

        CURRENT_ARCH_NAME = System.getProperty("os.arch");

        Architecture arch = Architecture.parseArch(CURRENT_ARCH_NAME);

        if (arch == Architecture.PPC && isLE) {
            arch = Architecture.PPCLE;
        } else if (arch == Architecture.PPC64 && isLE) {
            arch = Architecture.PPC64LE;
        }

        CURRENT_ARCH = arch;


        String sysArchName = null;
        if (CURRENT_SYSTEM == OperatingSystem.WINDOWS) {
            String processorIdentifier = System.getenv("PROCESSOR_IDENTIFIER");
            if (processorIdentifier != null) {
                int idx = processorIdentifier.indexOf(' ');
                if (idx > 0) {
                    sysArchName = processorIdentifier.substring(0, idx);
                }
            }
        } else {
            /* TODO
            try {
                Process process = Runtime.getRuntime().exec("/usr/bin/arch");
                if (process.waitFor(3, TimeUnit.SECONDS)) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), NATIVE_CHARSET))) {
                        sysArchName = reader.readLine().trim();
                    }
                }
            } catch (Throwable ignored) {
            }
             */
        }

        Architecture sysArch = Architecture.parseArch(sysArchName);
        if (sysArch == Architecture.UNKNOWN) {
            SYSTEM_ARCH_NAME = CURRENT_ARCH_NAME;
            SYSTEM_ARCH = CURRENT_ARCH;
        } else {
            if (sysArch == Architecture.PPC && isLE) {
                sysArch = Architecture.PPCLE;
            } else if (sysArch == Architecture.PPC64 && isLE) {
                sysArch = Architecture.PPC64LE;
            }
            SYSTEM_ARCH_NAME = sysArchName;
            SYSTEM_ARCH = sysArch;
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public DataModel getDataModel() {
        if (architecture.is64Bit()) {
            return operatingSystem == OperatingSystem.WINDOWS
                    ? DataModel.LLP64
                    : DataModel.LP64;
        } else {
            return DataModel.ILP32;
        }
    }

    public boolean is64Bit() {
        return architecture.is64Bit();
    }

    public int getShortSize() {
        return getDataModel().getShortSize();
    }

    public int getIntSize() {
        return getDataModel().getIntSize();
    }

    public int getLongSize() {
        return getDataModel().getLongSize();
    }

    public int getLongLongSize() {
        return getDataModel().getLongLongSize();
    }

    /**
     * The platform specific standard C library name
     *
     * @return The standard C library name
     */
    public String getCLibraryName() {
        if (operatingSystem == OperatingSystem.WINDOWS) {
            return "msvcrt";
        }
        if (operatingSystem == OperatingSystem.AIX) {
            return architecture.is64Bit() ? "libc.a(shr.o)" : "libc.a(shr_64.o)";
        }
        if (operatingSystem.isDarwin()) {
            return "System";
        }
        return "c";
    }

    @Override
    public int hashCode() {
        return architecture.hashCode() ^ operatingSystem.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Platform)) return false;
        Platform platform = (Platform) o;
        return architecture == platform.architecture && operatingSystem == platform.operatingSystem;
    }

    @Override
    public String toString() {
        return operatingSystem.getNormalizedName() + "-" + architecture.getNormalizedName();
    }

    public static final Platform CURRENT_PLATFORM = new Platform(CURRENT_SYSTEM, CURRENT_ARCH);
    public static final Platform SYSTEM_PLATFORM = (CURRENT_ARCH == SYSTEM_ARCH) ? CURRENT_PLATFORM : new Platform(CURRENT_SYSTEM, SYSTEM_ARCH);

    public Path getAppDataDirectory(String folder) {
        if (operatingSystem == OperatingSystem.WINDOWS) {
            String appdata = System.getenv("APPDATA");
            if (appdata != null) {
                try {
                    return Paths.get(appdata, folder);
                } catch (InvalidPathException ignored) {
                }
            }
            return Paths.get(System.getProperty("user.home"), "." + folder);
        } else if (operatingSystem == OperatingSystem.MACOS) {
            return Paths.get(System.getProperty("user.home"), "Library", "Application Support", folder);
        }

        String dataHome = System.getenv("XDG_DATA_HOME");
        if (dataHome != null) {
            try {
                return Paths.get(dataHome, folder);
            } catch (InvalidPathException ignored) {
            }
        }
        return Paths.get(System.getProperty("user.home"), ".local", "share", folder);
    }
}
