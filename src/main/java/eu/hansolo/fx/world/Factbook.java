package eu.hansolo.fx.world;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


import java.io.*;

class Factbook{ 

private String factbookPath = "/home/mason/Dropbox/School/Semester6/HCI/Project/SourceFiles/worldfx/src/main/java/eu/hansolo/fx/world/factbook.json";

private JSONObject countryData;

    public Factbook(){
        JSONParser parser = new JSONParser();
        FileReader f = null;
        try {
            f =  new FileReader(factbookPath);
        } catch (Exception e) {
            System.out.println("Error");
        }
        Object obj = null;
        try {
            obj = parser.parse(f);
        } catch (Exception e) {
            System.out.println("Error");
        }

        JSONObject jsonObj = (JSONObject)obj;
        countryData = (JSONObject)jsonObj.get("countries");
    }

    public String getGovType(String countryName){
        try{
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);

            country = (JSONObject)country.get("data");
            country = (JSONObject)country.get("government");
            String govType = (String)country.get("government_type");

            return govType;
        } catch(Exception e){
            return null;
        }
    }

    public String getHeadOfState(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get head_of_government for the country
            country = (JSONObject)country.get("government");
            country = (JSONObject)country.get("executive_branch");
            String headOfGov = (String)country.get("chief_of_state");

            return headOfGov;
        } catch(Exception e){
            return null;
        }
    }

    public String[] getLanguage(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get languages for the country
            country = (JSONObject)country.get("people");
            country = (JSONObject)country.get("languages");

            //Loops through the json array and stores the top languages and percentage breakdown in 'languages'
            JSONArray langs = (JSONArray)country.get("language");
            String[] languages = new String[langs.size()];
            for(int i = 0; i < langs.size(); ++i){
                JSONObject o = (JSONObject)langs.get(i);
                languages[i] = (String)o.get("name") + ":"; //Name is always a string
                //If no percentage data is supplied, indicate
                if(o.get("percent") == null){
                    languages[i] += "No percentage data";
                } else {
                    languages[i] += String.valueOf(o.get("percent")) + "%"; //Percent is sometimes double sometimes int
                }
            }

            return languages;
        } catch(Exception e){
            //Handle the outcome of this later
            return null;
        }
    }

    public String getGdp(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get gdp for the country
            country = (JSONObject)country.get("economy");
            country = (JSONObject)country.get("gdp");
            country = (JSONObject)country.get("purchasing_power_parity");

            //GDP values are stored in a JSONArray - we only want the latest value
            JSONArray values = (JSONArray)country.get("annual_values");
            country = (JSONObject)values.get(0); //Gets the latest gdp value
            String gdp = String.valueOf(country.get("value"));

            return gdp;
        } catch(Exception e){
            return null;
        }
    }

    public String getGdpPerCapita(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get gdp for the country
            country = (JSONObject)country.get("economy");
            country = (JSONObject)country.get("gdp");
            country = (JSONObject)country.get("per_capita_purchasing_power_parity");

            //GDP values are stored in a JSONArray - we only want the latest value
            JSONArray values = (JSONArray)country.get("annual_values");
            country = (JSONObject)values.get(0); //Gets the latest gdp value
            String per_gdp = String.valueOf(country.get("value"));

            return per_gdp;
        } catch(Exception e){
            return null;
        }
    }

    public String[] getExchangeRate(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get the exchange rate for the country
            country = (JSONObject)country.get("economy");
            country = (JSONObject)country.get("exchange_rates");

            String[] rates = new String[2];
            //The 'note' tells what the exchange rate is compared to
            rates[0] = (String)country.get("note");

            //Exchange rates are stored in a JSONArray, but we only want latest values
            JSONArray values = (JSONArray)country.get("annual_values");
            country = (JSONObject)values.get(0);
            rates[1] = String.valueOf(country.get("value"));

            return rates;
        } catch(Exception e){
            return null;
        }
    }

    public String getCapital(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get capital for the country
            country = (JSONObject)country.get("government");
            country = (JSONObject)country.get("capital");
            String capital = (String)country.get("name");

            return capital;
        } catch(Exception e){
            return null;
        }
    }

    public String getPopulation(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get population for the country
            country = (JSONObject)country.get("people");
            country = (JSONObject)country.get("population");
            String pop = String.valueOf(country.get("total"));

            return pop;
        } catch(Exception e){
            return null;
        }
    }

    public String[] getCities(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get JSONObject that contains the JSONArray of the cities for the country
            country = (JSONObject)country.get("people");
            country = (JSONObject)country.get("major_urban_areas");

            //Loops through the json array and stores the cities and population breakdown in 'cities'
            JSONArray areas = (JSONArray)country.get("places");
            String[] cities = new String[areas.size()];
            for(int i = 0; i < areas.size(); ++i){
                JSONObject o = (JSONObject)areas.get(i);
                cities[i] = (String)o.get("place") + ":"; //Name is always a string
                //If no population data is supplied, indicate
                if(o.get("population") == null){
                    cities[i] += "No population data";
                } else {
                    cities[i] += String.valueOf(o.get("population")); //Population is not string, so convert
                }
            }

            return cities;
        } catch(Exception e){
            //Handle the outcome of this later
            return null;
        }
    }

    public String[] getReligions(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get JSONObject that contains the JSONArray of the religions for the country
            country = (JSONObject)country.get("people");
            country = (JSONObject)country.get("religions");

            //Loops through the json array and stores the religions and their percentage breakdown in 'religions'
            JSONArray r = (JSONArray)country.get("religion");
            String[] religions = new String[r.size()];
            for(int i = 0; i < r.size(); ++i){
                JSONObject o = (JSONObject)r.get(i);
                religions[i] = (String)o.get("name") + ":"; //Name is always a string
                //If no percent data is supplied, indicate
                if(o.get("percent") == null){
                    religions[i] += "No percent data";
                } else {
                    religions[i] += String.valueOf(o.get("percent")); //Population is not string, so convert
                }
            }

            return religions;
        } catch(Exception e){
            //Handle the outcome of this later
            return null;
        }
    }

    public String[] getImports(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get JSONObject that contains the JSONArray of the imports for the country
            country = (JSONObject)country.get("economy");
            country = (JSONObject)country.get("imports");
            country = (JSONObject)country.get("commodities");

            //Loops through the json array and stores the imports in 'imports'
            JSONArray imps = (JSONArray)country.get("by_commodity");
            String[] imports = new String[imps.size()];
            for(int i = 0; i < imps.size(); ++i){
                imports[i] = (String)imps.get(i); //Name is always a string
            }

            return imports;
        } catch(Exception e){
            //Handle the outcome of this later
            return null;
        }
    }

    public String[] getExports(String countryName){
        try{
            //Get the data for the country
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);
            country = (JSONObject)country.get("data");

            //Get JSONObject that contains the JSONArray of the exports for the country
            country = (JSONObject)country.get("economy");
            country = (JSONObject)country.get("exports");
            country = (JSONObject)country.get("commodities");

            //Loops through the json array and stores the exports in 'exports'
            JSONArray exps = (JSONArray)country.get("by_commodity");
            String[] exports = new String[exps.size()];
            for(int i = 0; i < exps.size(); ++i){
                exports[i] = (String)exps.get(i); //Name is always a string
            }

            return exports;
        } catch(Exception e){
            //Handle the outcome of this later
            return null;
        }
    }

    public String getHistory(String countryName){
        try{
            countryName = countryName.replace(" ", "_").toLowerCase();
            JSONObject country = (JSONObject)countryData.get(countryName);

            country = (JSONObject)country.get("data");
            country = (JSONObject)country.get("introduction");
            String history = (String)country.get("background");

            return history;
        } catch(Exception e){
            return null;
        }
    }
}