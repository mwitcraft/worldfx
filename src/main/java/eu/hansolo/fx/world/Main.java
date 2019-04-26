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
 */

package eu.hansolo.fx.world;

import eu.hansolo.fx.world.World.Resolution;
import javafx.application.Application;
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

public class Main extends Application {
    private World world;

    private Factbook factbook = new Factbook();

    @Override
    public void init() {
        for (Country c : Country.values())
            c.setColor(Color.WHITE);

        world = WorldBuilder.create().resolution(Resolution.HI_RES).mousePressHandler(evt -> {
            // Get click information
            CountryPath countryPath = (CountryPath) evt.getSource();
            Locale locale = countryPath.getLocale();
            String countryName = locale.getDisplayCountry();

            // Intialize popup window
            StackPane pane = new StackPane();
            VBox root = new VBox();
            Label label = new Label(countryName); // Country Name
            root.getChildren().add(label);

            Text t = new Text();
            t.wrappingWidthProperty().bind(pane.widthProperty());
            root.getChildren().add(t);

            pane.getChildren().add(root);
            Stage stage = new Stage();
            stage.setTitle(locale.getDisplayCountry());
            Scene scene = new Scene(pane, 550, 250);
            stage.setScene(scene);
            stage.show();

        }).zoomEnabled(true).selectionEnabled(true).build();
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

        stage.setTitle("World Map");
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
}