/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Modified by Group 22 World Explorer for a project in Human-Computer Interaction Spring 2019 at the University of Oklahoma. 
 * 
 * The init() method was modified to set country colors with the added method setCountryColors().
 * 
 * The mousePressHandler() was modified to send information from the world factbook JSON parser class Factbook to a window.
 * The added method populateCountryWindow() is called to get the country information.
 */


package eu.hansolo.fx.world;

import eu.hansolo.fx.world.World.Resolution;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Main extends Application {
	private static final Random RND = new Random();
    private World  world;
    private static Factbook  factbook = new Factbook();

    
    @Override public void init() {
    	
    	// Set the colors of the country
    	setCountryColors();
    	
        world = WorldBuilder.create()
                            .resolution(Resolution.HI_RES)
                            .mousePressHandler(evt -> {
                            	// Get the country that is currently clicked
                            	CountryPath countryPath = (CountryPath) evt.getSource();
                                Locale locale = countryPath.getLocale();
                                
                                // Make country information window
                                VBox vbox = populateCountryWindow(countryPath, locale); 
                                
                                // Add country information window to a scroll pane
                                ScrollPane scrollPane = new ScrollPane(vbox);
                                scrollPane.setFitToHeight(true);
                                
                                Scene scene = new Scene(scrollPane, 500, 1000);
                                Stage stage = new Stage(); 
                                stage.setScene(scene);
                                stage.setTitle(locale.getDisplayCountry());
                                
                                stage.show();
                            })
                            .zoomEnabled(true)
                            .selectionEnabled(true)
                            .build();
    }

    @Override
    public void start(Stage stage) {

        // Add search bar and search functionality
        // Setup layout
        BorderPane border = new BorderPane();
        HBox h = new HBox();
        h.setBackground(
                new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        h.setAlignment(Pos.CENTER);
        TextField tf = new TextField();
        h.getChildren().add(tf);
        border.setTop(h); // Add search bar at top
        // Create a map of countries so I can easily access it later
        Map<String, Country> countries = new HashMap<>();
        for (Country c : Country.values()) {
            Locale l = new Locale("", c.getName());
            countries.put(l.getDisplayCountry().toLowerCase().replaceAll("\\s+", "_"), c); // Replace whitespace with underscores
        }

        // Call the search bar function when any key is released
        tf.setOnKeyReleased(e -> {
            searchBar(countries, tf);
        });

        StackPane pane = new StackPane(world);
        pane.setBackground(
                new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        border.setCenter(pane);
        Scene scene = new Scene(border);

        stage.setTitle("World Explorer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Function to make the search bar work
    public void searchBar(Map<String, Country> countries, TextField tf) {
        // Empty search bar, revert everything back to original colors
        if(tf.getText().length() < 1){
            for(Country c : countries.values()){
                try {
                    for(CountryPath p : world.countryPaths.get(c.getName()))
                        p.setFill(c.getColor());
                } catch (Exception e) {
                    //TODO: handle exception
                    // For some reason country code "KV" throws error
                    // Fix Later
                }
            }
            return;
        }

        // Find countries that match what is being typed
        ArrayList<String> keys = new ArrayList<>();
        String entry = tf.getText().toLowerCase().replaceAll("\\s+", "_"); // Replace whitespace with underscores
        for (String key : countries.keySet()) {
            String keyTest = "";
            if(!(key.length() < entry.length())) // If the search term is longer than the country name, just skip it
                keyTest = key.substring(0, entry.length()); // If the first 'x' letters of the country name where 'x' is entry.length
            if (keyTest.equals(entry)) { // If the substring matches the search term
                keys.add(key); // Add it to the list
            }
        }
        for(String key : keys){ // Step through the list of matches
            Country c = countries.get(key); // Get the country corresponding to the key
            try {
                for(CountryPath p : world.countryPaths.get(c.getName())){ // Loop through the country paths (that's how you dynamically color)
                    p.setFill(Color.RED); // Color red
                }
            } catch (Exception e) {
                //TODO: handle exception
                // For some reason country code "KV" throws error
                // Fix Later
            }
        }

        // Reset everything else to their default colors
        for(String key : countries.keySet()){
            if(!keys.contains(key)){ // Skip matching terms
                Country c = countries.get(key);
                if(c != null){
                    try {
                        for(CountryPath p : world.countryPaths.get(c.getName())){
                            p.setFill(c.getColor()); // Reset color
                        }

                    } catch (Exception e) {
                        //TODO: handle exception
                        // For some reason country code "KV" throws error
                        // Fix Later
                    }
                }
            }
        }
    }
    
    public static void setCountryColors() {
    	ArrayList<Color> colors = new ArrayList<>();
	    
	    // Make the countries shades of blue    
    	colors.add(Color.web("#21618c",1.0));
    	colors.add(Color.web("#2874a6",1.0));
    	colors.add(Color.web("#2e86c1",1.0));
    	colors.add(Color.web("#3498db",1.0));
    	colors.add(Color.web("#5dade2",1.0));
    	colors.add(Color.web("#85c1e9",1.0));
    	colors.add(Color.web("#aed6f1",1.0));
    	
    	// Randomly assign the colors to countries
    	for (Country country : Country.values()) {
    		Color randomColor = colors.get(RND.nextInt(colors.size()));
    		country.setColor(randomColor);
    	}
    }
    
    public static VBox populateCountryWindow(CountryPath countryPath, Locale locale) {
    	VBox vbox = new VBox();             // Box to contain the flow panes with country information
    	
    	Text subHeader;                     // Sub headers for the data 
        Text text;                          // Text to go under the subheaders
        
        // Add government type data_array to the pane
        FlowPane govPane = new FlowPane();
        govPane.setHgap(4);
      
        subHeader = new Text("Government Type:");
        subHeader.setStyle("-fx-font-weight: bold");
        govPane.getChildren().add(subHeader);
        
        // Split on spaces so each word is added to the flow pane. Then, the words will move with pane resizing.
        String [] data_array = factbook.getGovType(locale.getDisplayCountry()).split(" ");
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		govPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	govPane.getChildren().add(text);
        }
       
        // Add head of state data_array to the pane
        FlowPane headPane = new FlowPane();
        headPane.setHgap(4);
        
        subHeader = new Text("Head of State:");
        subHeader.setStyle("-fx-font-weight: bold");
        headPane.getChildren().add(subHeader);
        
        // Split on spaces so each word is added to the flow pane
        data_array = factbook.getHeadOfState(locale.getDisplayCountry()).split(" ");
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		headPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	headPane.getChildren().add(text);
        }
        
        // Add language info to the pane
        VBox langPane = new VBox();
                        
        subHeader = new Text("Languages:");
        subHeader.setStyle("-fx-font-weight: bold");
        langPane.getChildren().add(subHeader);
        
        data_array = factbook.getLanguage(locale.getDisplayCountry());
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		langPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	langPane.getChildren().add(text);
        }
        
        // Add GDP info to the pane
        FlowPane gdpPane = new FlowPane();
        gdpPane.setHgap(4);
                        
        subHeader = new Text("GDP:");
        subHeader.setStyle("-fx-font-weight: bold");
        gdpPane.getChildren().add(subHeader);
        
        String data = factbook.getGdp(locale.getDisplayCountry());
        if (data != null) {
        	text = new Text("$" + data);
        	gdpPane.getChildren().add(text);
        }
        else {
        	text = new Text("No data");
        	gdpPane.getChildren().add(text);
        }
        
        // Add GDP per capita info to the pane
        FlowPane gdppcPane = new FlowPane();
        gdppcPane.setHgap(4);
                        
        subHeader = new Text("GDP per capita:");
        subHeader.setStyle("-fx-font-weight: bold");
        gdppcPane.getChildren().add(subHeader);
        
        data = factbook.getGdpPerCapita(locale.getDisplayCountry());
        if (data != null) {
        	text = new Text("$" + data);
        	gdppcPane.getChildren().add(text);
        }
        else {
        	text = new Text("No data");
        	gdppcPane.getChildren().add(text);
        }
        
        // Add exchange rate info to the pane
        FlowPane exchangePane = new FlowPane();
        exchangePane.setHgap(4);
                        
        subHeader = new Text("Exchange Rate:");
        subHeader.setStyle("-fx-font-weight: bold");
        exchangePane.getChildren().add(subHeader);
        
        // Split on spaces so each word is added to the flow pane
        data_array = factbook.getExchangeRate(locale.getDisplayCountry()); 
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		exchangePane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	exchangePane.getChildren().add(text);
        }
        
        // Add capital info to the pane
        FlowPane capitalPane = new FlowPane();
        capitalPane.setHgap(4);
                        
        subHeader = new Text("Capital:");
        subHeader.setStyle("-fx-font-weight: bold");
        capitalPane.getChildren().add(subHeader);
        
        data = factbook.getCapital(locale.getDisplayCountry());
        if (data != null) {
        	text = new Text(data);
        	capitalPane.getChildren().add(text);
        }
        else {
        	text = new Text("No data");
        	capitalPane.getChildren().add(text);
        }
        
        // Add pop info to the pane
        FlowPane popPane = new FlowPane();
        popPane.setHgap(4);
                        
        subHeader = new Text("Population:");
        subHeader.setStyle("-fx-font-weight: bold");
        popPane.getChildren().add(subHeader);
        
        data = factbook.getPopulation(locale.getDisplayCountry());
        if (data != null) {
        	text = new Text(data);
        	popPane.getChildren().add(text);
        }
        else {
        	text = new Text("No data");
        	popPane.getChildren().add(text);
        }
        
        // Add city info to the pane
        VBox cityPane = new VBox();
                        
        subHeader = new Text("Largest Cities (by population):");
        subHeader.setStyle("-fx-font-weight: bold");
        cityPane.getChildren().add(subHeader);
        
        data_array = factbook.getCities(locale.getDisplayCountry());
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		cityPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	cityPane.getChildren().add(text);
        }
        
        // Add religion info to the pane
        VBox religionPane = new VBox();
                        
        subHeader = new Text("Religions:");
        subHeader.setStyle("-fx-font-weight: bold");
        religionPane.getChildren().add(subHeader);
        
        data_array = factbook.getReligions(locale.getDisplayCountry());
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		religionPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	religionPane.getChildren().add(text);
        }
        
        // Add imports info to the pane
        VBox importsPane = new VBox();
                        
        subHeader = new Text("Imports:");
        subHeader.setStyle("-fx-font-weight: bold");
        importsPane.getChildren().add(subHeader);
        
        data_array = factbook.getImports(locale.getDisplayCountry());
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		importsPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	importsPane.getChildren().add(text);
        }
        
        // Add imports info to the pane
        VBox exportsPane = new VBox();
                        
        subHeader = new Text("Exports:");
        subHeader.setStyle("-fx-font-weight: bold");
        exportsPane.getChildren().add(subHeader);
        
        data_array = factbook.getImports(locale.getDisplayCountry());
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		exportsPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	exportsPane.getChildren().add(text);
        }
        
        // Add history info to the pane
        FlowPane histPane = new FlowPane();
        histPane.setHgap(4);
        histPane.setVgap(4);
                        
        subHeader = new Text("History:");
        subHeader.setStyle("-fx-font-weight: bold");
        histPane.getChildren().add(subHeader);
        
        // Split on spaces so each word is added to the flow pane
        data_array = factbook.getHistory(locale.getDisplayCountry()).split(" ");
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		histPane.getChildren().add(text);
        	}
        }
        else {
        	text = new Text("No data");
        	histPane.getChildren().add(text);
        }
      
        // Add flow panes to the vertical box/*
        vbox.getChildren().addAll(govPane, headPane, langPane, gdpPane, gdppcPane, exchangePane, capitalPane,
        		popPane, cityPane, religionPane, importsPane, exportsPane, histPane);
             
        return vbox;
    }
}

