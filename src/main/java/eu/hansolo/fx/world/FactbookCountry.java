package eu.hansolo.fx.world;

import org.json.simple.JSONObject;

class FactbookCountry{

    public FactbookCountry(Factbook book, String countryName){
        countryName = countryName.replace(" ", "_");
        JSONObject countries = book.getCountries();
        JSONObject country = (JSONObject)countries.get(countryName.toLowerCase());
        JSONObject data = (JSONObject)country.get("data");
        JSONObject gov = (JSONObject)data.get("government");
        String govType = (String)gov.get("government_type");
        System.out.println("\n\n Country: " + countryName + "\n" + "Government Type: " + govType + "\n\n");
    }

}