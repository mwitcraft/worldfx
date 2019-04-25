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
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Main extends Application {
	private static final Random RND = new Random();
    private              World         world;

    //private static factbook = new Factbook();


    @Override public void init() {
    	ArrayList<Color> colors = new ArrayList<>();
    	colors.add(Color.ALICEBLUE);
    	colors.add(Color.AZURE);
    	colors.add(Color.LIGHTCYAN);
    	colors.add(Color.LIGHTSTEELBLUE);
    	colors.add(Color.LIGHTSLATEGRAY);
    	colors.add(Color.SILVER);
    	
    	for (Country country : Country.values()) {
    		/*
    		double r = RND.nextDouble();
    		double b = RND.nextDouble();
    		double g = RND.nextDouble();
    		*/
    		Color randomColor = colors.get(RND.nextInt(colors.size()));
    		country.setColor(randomColor);
    	}
    	
        world = WorldBuilder.create()
                            .resolution(Resolution.HI_RES)
                            .mousePressHandler(evt -> {
                                CountryPath countryPath = (CountryPath) evt.getSource();
                                Locale locale = countryPath.getLocale();
                                
                                // Populate the country pane
                                VBox root = populateCountryPane(countryPath, locale); 
                                Scene scene = new Scene(root, 600, 800);

                                Stage stage = new Stage(); 
                                stage.setTitle(locale.getDisplayCountry());
                                stage.setScene(scene);
                                stage.show();
                            })
                            .zoomEnabled(true)
                            .selectionEnabled(true)
                            .build();
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(world);
        pane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);

        stage.setTitle("World Explorer");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static VBox populateCountryPane(CountryPath countryPath, Locale locale) {
    	Factbook factbook = new Factbook();
    	VBox vbox = new VBox();
    	Text subHeader;
        Text text;
        
        // Add government type data_array to the pane
        FlowPane govPane = new FlowPane();
        govPane.setHgap(4);
        govPane.setVgap(4);
      
        subHeader = new Text("Government Type:");
        subHeader.setStyle("-fx-font-weight: bold");
        govPane.getChildren().add(subHeader);
        
        String [] data_array = factbook.getGovType(locale.getDisplayCountry()).split(" ");
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		govPane.getChildren().add(text);
        	}
        }
       
        // Add head of state data_array to the pane
        FlowPane headPane = new FlowPane();
        headPane.setPrefWrapLength(300); 
        headPane.setHgap(4);
        headPane.setVgap(4);
        
        subHeader = new Text("Head of State:");
        subHeader.setStyle("-fx-font-weight: bold");
        headPane.getChildren().add(subHeader);
        
        data_array = factbook.getHeadOfState(locale.getDisplayCountry()).split(" ");
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		headPane.getChildren().add(text);
        	}
        }
        
        // Add language info to the pane
        FlowPane langPane = new FlowPane();
        langPane.setPrefWrapLength(300); 
        langPane.setHgap(4);
        langPane.setVgap(4);
                        
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
        
        // Add GDP info to the pane
        FlowPane gdpPane = new FlowPane();
        gdpPane.setHgap(4);
        gdpPane.setVgap(4);
                        
        subHeader = new Text("GDP:");
        subHeader.setStyle("-fx-font-weight: bold");
        gdpPane.getChildren().add(subHeader);
        
        String data = factbook.getGdp(locale.getDisplayCountry());
        text = new Text("$" + data);
        gdpPane.getChildren().add(text);
        
        // Add GDP per capita info to the pane
        FlowPane gdppcPane = new FlowPane();
        gdppcPane.setHgap(4);
        gdppcPane.setVgap(4);
                        
        subHeader = new Text("GDP per capita:");
        subHeader.setStyle("-fx-font-weight: bold");
        gdppcPane.getChildren().add(subHeader);
        
        data = factbook.getGdpPerCapita(locale.getDisplayCountry());
        text = new Text("$" + data);
        gdppcPane.getChildren().add(text);
        
        // Add exchange rate info to the pane
        FlowPane exchangePane = new FlowPane();
        exchangePane.setHgap(4);
        exchangePane.setVgap(4);
                        
        subHeader = new Text("Exchange Rate:");
        subHeader.setStyle("-fx-font-weight: bold");
        exchangePane.getChildren().add(subHeader);
        
        data_array = factbook.getExchangeRate(locale.getDisplayCountry()); 
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		exchangePane.getChildren().add(text);
        	}
        }
        
        // Add capital info to the pane
        FlowPane capitalPane = new FlowPane();
        capitalPane.setHgap(4);
        capitalPane.setVgap(4);
                        
        subHeader = new Text("Capital:");
        subHeader.setStyle("-fx-font-weight: bold");
        capitalPane.getChildren().add(subHeader);
        
        data = factbook.getCapital(locale.getDisplayCountry());
        text = new Text(data);
        capitalPane.getChildren().add(text);
        
        // Add pop info to the pane
        FlowPane popPane = new FlowPane();
        popPane.setHgap(4);
        popPane.setVgap(4);
                        
        subHeader = new Text("Population:");
        subHeader.setStyle("-fx-font-weight: bold");
        popPane.getChildren().add(subHeader);
        
        data = factbook.getPopulation(locale.getDisplayCountry());
        text = new Text(data);
        popPane.getChildren().add(text);
        
        // Add city info to the pane
        FlowPane cityPane = new FlowPane();
        cityPane.setHgap(4);
        cityPane.setVgap(4);
                        
        subHeader = new Text("Cities:");
        subHeader.setStyle("-fx-font-weight: bold");
        cityPane.getChildren().add(subHeader);
        
        data_array = factbook.getCities(locale.getDisplayCountry());
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		cityPane.getChildren().add(text);
        	}
        }
        
        // Add religion info to the pane
        FlowPane religionPane = new FlowPane();
        religionPane.setHgap(4);
        religionPane.setVgap(4);
                        
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
        
        // Add imports info to the pane
        FlowPane importsPane = new FlowPane();
        importsPane.setHgap(4);
        importsPane.setVgap(4);
                        
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
        
        // Add imports info to the pane
        FlowPane exportsPane = new FlowPane();
        exportsPane.setHgap(4);
        exportsPane.setVgap(4);
                        
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
        
        // Add history info to the pane
        FlowPane histPane = new FlowPane();
        histPane.setHgap(4);
        histPane.setVgap(4);
                        
        subHeader = new Text("History:");
        subHeader.setStyle("-fx-font-weight: bold");
        histPane.getChildren().add(subHeader);
        
        data_array = factbook.getHistory(locale.getDisplayCountry()).split(" ");
        if (data_array != null) {
        	for(String elem:data_array) {
        		text = new Text(elem);
        		histPane.getChildren().add(text);
        	}
        }
        
       
        // Add flow panes to the vertical box
        vbox.getChildren().addAll(govPane, headPane, langPane, gdpPane, gdppcPane, exchangePane, capitalPane,
        		popPane, cityPane, religionPane, importsPane, exportsPane, histPane);
    	
        return vbox;
    }
}
