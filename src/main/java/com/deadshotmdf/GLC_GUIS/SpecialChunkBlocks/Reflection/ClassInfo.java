package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Reflection;

import org.bukkit.Bukkit;

public class ClassInfo
{
    private final String className;
    private final PackageType packageType;

    public static Class<?>[] findClasses(final ClassInfo[] classInfos) {
        final Class<?>[] classes = (Class<?>[])new Class[classInfos.length];
        for (int i = 0; i < classes.length; ++i) {
            classes[i] = classInfos[i].findClass();
        }
        return classes;
    }

    public ClassInfo(final String className, final PackageType packageType) {
        this.className = className;
        this.packageType = packageType;
    }

    public Class<?> findClass() {
        try {
            return this.packageType.findClass(this.className);
        }
        catch (ClassNotFoundException error) {
            error.printStackTrace();
            return null;
        }
    }

    public enum PackageType
    {
        NMS {
            private final boolean isLegacyNMSPackageFormat;

            {
                this.isLegacyNMSPackageFormat = this.isLegacyNMSPackageFormat();
            }

            @Override
            Class<?> findClass(final String className) throws ClassNotFoundException {
                final StringBuilder fullNMSPackage = new StringBuilder("net.minecraft.");
                if (this.isLegacyNMSPackageFormat) {
                    fullNMSPackage.append(".server.").append(ServerVersion.getVersion());
                }
                fullNMSPackage.append(".").append(className);
                return Class.forName(fullNMSPackage.toString());
            }

            private boolean isLegacyNMSPackageFormat() {
                try {
                    final String version = ServerVersion.getVersion();
                    Class.forName("net.minecraft.server." + version + ".WorldServer");
                    return true;
                }
                catch (Exception error) {
                    return false;
                }
            }
        },
        CRAFTBUKKIT {
            private final String bukkitPackage;

            {
                this.bukkitPackage = Bukkit.getServer().getClass().getPackage().getName();
            }

            @Override
            Class<?> findClass(final String className) throws ClassNotFoundException {
                return Class.forName(this.bukkitPackage + "." + className);
            }
        },
        UNKNOWN {
            @Override
            Class<?> findClass(final String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        };

        abstract Class<?> findClass(final String p0) throws ClassNotFoundException;
    }
}