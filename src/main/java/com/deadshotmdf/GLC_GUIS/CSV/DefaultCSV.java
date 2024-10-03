package com.deadshotmdf.GLC_GUIS.CSV;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DefaultCSV {
    private final static String filename = "default.csv";
    private final static String header = "Location, Item Name, price, lore";

    public final int lengthOfHeader = header.split(",").length;

    public static void generateDefaultCSV(File pluginLocation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pluginLocation, filename)))) {
            // Write the header
            writer.write(header);
            writer.newLine();


            System.out.println(filename + " created successfully with default data.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
