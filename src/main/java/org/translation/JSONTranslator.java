package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    private Map<String, Map<String, String>> map;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            map = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, String> languageMap = new HashMap<>();
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (!Objects.equals(key, "id") && !Objects.equals(key, "alpha2")
                            && !Objects.equals(key, "alpha3")) {
                        languageMap.put(key, jsonObject.getString(key));
                    }
                }
                map.put(jsonObject.getString("alpha3"), languageMap);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        if (country == null){
            return new ArrayList<>();
        }
        Map<String, String> languageMap = map.get(country.toLowerCase());
        Set<String> languageSet = languageMap.keySet();
        return new ArrayList<>(languageSet);
    }

    @Override
    public List<String> getCountries() {
        Set<String> countrySet = map.keySet();
        return new ArrayList<>(countrySet);
    }

    @Override
    public String translate(String country, String language) {
        if (country == null){
            return null;
        }
        Map<String, String> languageMap = map.get(country.toLowerCase());
        return languageMap.get(language);
    }
}
