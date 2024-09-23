package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {
    private Map<String, String> countryAlpha2Codes;
    private Map<String, String> countryAlpha3Codes;
    private Map<Integer, String> countryNumericCode;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            countryAlpha2Codes = new HashMap<>();
            countryAlpha3Codes = new HashMap<>();
            countryNumericCode = new HashMap<>();
            for (String line : lines) {
                String[] split = line.split(",");
                countryAlpha2Codes.put(split[1], split[0]);
                countryAlpha3Codes.put(split[2], split[0]);
                countryNumericCode.put(Integer.parseInt(split[3]), split[0]);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        return this.countryAlpha3Codes.get(code);
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        for (String i : this.countryAlpha3Codes.keySet()) {
            if (this.countryAlpha3Codes.get(i).equals(country)) {
                return this.countryAlpha3Codes.get(i);
            }
        }
        return "";
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return countryNumericCode.size();
    }
}
