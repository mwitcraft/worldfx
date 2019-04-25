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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Main extends Application {
    private              World         world;

    private Factbook factbook = new Factbook();


    @Override public void init() {
        world = WorldBuilder.create()
                            .resolution(Resolution.HI_RES)
                            .mousePressHandler(evt -> {
                                // Get click information
                                CountryPath countryPath = (CountryPath) evt.getSource();
                                Locale locale = countryPath.getLocale();
                                String countryName = locale.getDisplayCountry();
                               
                                // Intialize popup window
                                StackPane pane = new StackPane();
                                VBox root = new VBox();
                                Label label = new Label(countryName); //Country Name
                                root.getChildren().add(label);

                                // HBox h = new HBox();
                                // TextField tf = new TextField();
                                // Button submit = new Button("Submit");



                                Text t = new Text();
                                t.wrappingWidthProperty().bind(pane.widthProperty());
                                root.getChildren().add(t);

                                pane.getChildren().add(root);
                                Stage stage = new Stage(); 
                                stage.setTitle(locale.getDisplayCountry());
                                Scene scene = new Scene(pane, 550, 250);
                                stage.setScene(scene);
                                stage.show();

                            })
                            .zoomEnabled(true)
                            .selectionEnabled(true)
                            .build();
    }

    @Override public void start(Stage stage) {

        // Add search bar and search functionality
        // Setup layout
        BorderPane border = new BorderPane();
        HBox h = new HBox();
        h.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        h.setAlignment(Pos.CENTER);
        TextField tf = new TextField();
        Button submit = new Button("Search");
        h.getChildren().add(tf);
        h.getChildren().add(submit);
        border.setTop(h); // Add search bar and search button at top
        Map<String, Country> countries = new HashMap<>();
        for(Country c: Country.values()){
            Locale l = new Locale("", c.getName());
            countries.put(l.getDisplayCountry().replaceAll("\\s+", "").toLowerCase(), c);
        }

        submit.setOnAction(e -> {
            searchBar(countries, tf);
        });

        tf.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                searchBar(countries, tf);
        });
        // 



        StackPane pane = new StackPane(world);
        pane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        // My changes
        border.setCenter(pane);
        Scene scene = new Scene(border);
        // Scene scene = new Scene(pane);

        stage.setTitle("World Map");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void searchBar(Map<String, Country> countries, TextField tf){
        try{
                Country c = countries.get(tf.getText().replaceAll("\\s+", "").toLowerCase());
                for(CountryPath p : world.countryPaths.get(c.getName())){
                    p.setFill(Color.RED);
                }
        }
        catch(Exception error){
                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                VBox v = new VBox();
                Text t = new Text();
                if(tf.getText().isEmpty())
                    t.setText("Enter Search Term.");
                else
                    t.setText("No Country Found. Try Again.");
                v.getChildren().add(t);
                Scene dialogScene = new Scene(v, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
                System.out.println("No Country Found");
        }
        tf.setText("");
    }
}
