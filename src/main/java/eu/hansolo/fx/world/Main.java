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
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.util.Locale;

public class Main extends Application {
    private              World         world;

    private Factbook factbook = new Factbook();


    @Override public void init() {
        world = WorldBuilder.create()
                            .resolution(Resolution.HI_RES)
                            .mousePressHandler(evt -> {
                                CountryPath countryPath = (CountryPath) evt.getSource();
                                Locale locale = countryPath.getLocale();
                                
                                VBox root = new VBox();                                

                                StackPane pane = new StackPane();
                                
                                Label label = new Label(locale.getDisplayCountry());

                                pane.getChildren().add(label);

                                root.getChildren().add(pane);

                                Scene scene = new Scene(root, 550, 250);

                                Stage stage = new Stage(); 
                                stage.setTitle(locale.getDisplayCountry());
                                stage.setScene(scene);
                                stage.show();

                                // CountryPath countryPath = (CountryPath) evt.getSource();
                                // Locale      locale      = countryPath.getLocale();
                                // System.out.println(locale.getDisplayCountry() + " (" + locale.getISO3Country() + ")");

                                // System.out.println("Gov Type: ");
                                // System.out.println("\t" + factbook.getGovType(locale.getDisplayCountry()));

                                // System.out.println("Head of State: ");
                                // System.out.println("\t" + factbook.getHeadOfState(locale.getDisplayCountry()));

                                // System.out.println("Languages:");
                                // String[] a = factbook.getLanguage(locale.getDisplayCountry());
                                // if(a != null){
                                //     for(int i = 0; i < a.length; ++i){
                                //         System.out.println("\t" + a[i]);
                                //     }
                                // } else {
                                //     System.out.println("\tnull");
                                // }

                                // System.out.println("GDP:");
                                // System.out.println("\t$" + factbook.getGdp(locale.getDisplayCountry()));

                                // System.out.println("Per Capita GDP:");
                                // System.out.println("\t$" + factbook.getGdpPerCapita(locale.getDisplayCountry()));

                                // System.out.println("Exchange Rate:");
                                // String[] rates = factbook.getExchangeRate(locale.getDisplayCountry());
                                // if(rates != null){
                                //     System.out.println("\t" + rates[1]);
                                //     System.out.println("\t" + rates[0]);
                                // }

                                // System.out.println("Capital:");
                                // System.out.println("\t" + factbook.getCapital(locale.getDisplayCountry()));

                                // System.out.println("Population:");
                                // System.out.println("\t" + factbook.getPopulation(locale.getDisplayCountry()));

                                // System.out.println("Cities:");
                                // String[] cities = factbook.getCities(locale.getDisplayCountry());
                                // if(cities != null){
                                //     for(int i = 0; i < cities.length; ++i){
                                //         System.out.println("\t" + cities[i]);
                                //     }
                                // } else {
                                //     System.out.println("\tnull");
                                // }

                                // System.out.println("Religions:");
                                // String[] religions = factbook.getReligions(locale.getDisplayCountry());
                                // if(religions != null){
                                //     for(int i = 0; i < religions.length; ++i){
                                //         System.out.println("\t" + religions[i] + "%");
                                //     } 
                                // } else {
                                //     System.out.println("\tnull");
                                // }

                                // System.out.println("Imports:");
                                // String[] imports = factbook.getImports(locale.getDisplayCountry());
                                // if(imports != null){
                                //     for(int i = 0; i < imports.length; ++i){
                                //         System.out.println("\t" + imports[i]);
                                //     }
                                // } else {
                                //     System.out.println("\tnull");
                                // }

                                // System.out.println("Exports:");
                                // String[] exports = factbook.getExports(locale.getDisplayCountry());
                                // if(exports != null){
                                //     for(int i = 0; i < exports.length; ++i){
                                //         System.out.println("\t" + exports[i]);
                                //     }
                                // } else {
                                //     System.out.println("\tnull");
                                // }

                                // System.out.println("History: ");
                                // System.out.println("\t" + factbook.getHistory(locale.getDisplayCountry()));

                            })
                            .zoomEnabled(true)
                            .selectionEnabled(true)
                            .build();
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(world);
        pane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);

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
}
