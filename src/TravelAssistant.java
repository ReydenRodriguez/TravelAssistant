import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TravelAssistant extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("TravelAssistant");
        primaryStage.setScene(createHomeScene());
        primaryStage.show();
    }

    // home page for user residence
    private Scene createHomeScene() {
        // welcoming users
        Label welcomeLabel = new Label("Welcome to TravelAssistant!");
        welcomeLabel.setFont(new Font("Comic Sans MS", 26));
        welcomeLabel.setTextFill(Color.rgb(50, 50, 50));
        welcomeLabel.setEffect(new DropShadow(10, Color.GRAY));

        // user input
        // asking user
        Label residenceLabel = new Label("Where do you currently reside?");
        residenceLabel.setFont(new Font("Comic Sans MS", 18));
        residenceLabel.setTextFill(Color.rgb(100, 100, 100));

        // text field for user input
        TextField residenceField = new TextField();
        residenceField.setMaxWidth(300);
        residenceField.setPromptText("Enter your country");
        residenceField.setStyle("-fx-background-radius: 15; -fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 15;");

        // submit button
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;");
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #003f7f; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;"));
        submitButton.setEffect(new DropShadow(5, Color.GRAY));

        // action for button
        submitButton.setOnAction(e -> {
            String residence = residenceField.getText();
            String normalizedResidence = CountryNameNormalizer.normalizeCountryName(residence);
            System.out.println(normalizedResidence);
            if (normalizedResidence.equals("unknown")) {
                primaryStage.setScene(createHomeScene());
            }
            else {
                primaryStage.setScene(createDestinationScene(normalizedResidence));
            }
        });

        // arrangement of components
        VBox homeLayout = new VBox(15);
        homeLayout.getChildren().addAll(welcomeLabel, residenceLabel, residenceField, submitButton);
        homeLayout.setAlignment(Pos.CENTER);
        homeLayout.setStyle("-fx-background-color: #f4f4f9; -fx-padding: 20;");
        return new Scene(new StackPane(homeLayout), 1250, 900);
    }

    // second page for destination user input
    private Scene createDestinationScene(String normalizedResidence) {
        // label asking for user input
        Label destinationLabel = new Label("Where do you want to travel?");
        destinationLabel.setFont(new Font("Comic Sans MS", 20));
        destinationLabel.setTextFill(Color.rgb(100, 100, 100));

        // interactive map
        ImageView mapView = new ImageView("World_location_map_(equirectangular_180).png");
        mapView.setFitWidth(1100);
        mapView.setPreserveRatio(true);
        Label mapLabel = new Label("World_location_map_(equirectangular_180) - Wikipedia");
        mapLabel.setFont(new Font("Comic Sans MS", 12));
        mapLabel.setTextFill(Color.rgb(100, 100, 100));
        VBox mapPane = new VBox(5);
        mapPane.getChildren().addAll(mapView, mapLabel);
        mapPane.setAlignment(Pos.TOP_RIGHT);
        HBox mapPaneWrapper = new HBox();
        mapPaneWrapper.getChildren().add(mapPane);
        mapPaneWrapper.setAlignment(Pos.CENTER);

        // action for clicking with mouse on the map
        mapView.setOnMouseClicked(event -> {
            // getting the longitude/latitude
            double x = event.getX();
            double y = event.getY();
            double longitude = (x / mapView.getBoundsInParent().getWidth()) * 360 - 180;
            double latitude = 90 - (y / mapView.getBoundsInParent().getHeight()) * 180;

            ReverseGeocoding reverseGeocoding = new ReverseGeocoding();
            String countryCode = reverseGeocoding.getCountryCode(latitude, longitude);
            String destination = CountryCodeMapper.getCountryName(countryCode);

            primaryStage.setScene(createInformationScene(normalizedResidence, destination));
        });

        // back button to homepage
        Button backButton = new Button("<-Back");
        backButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #003f7f; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;"));
        backButton.setOnAction(e -> primaryStage.setScene(createHomeScene()));
        backButton.setEffect(new DropShadow(5, Color.GRAY));

        // arrangement of components
        VBox destinationLayout = new VBox(15);
        destinationLayout.getChildren().addAll(destinationLabel, mapPaneWrapper, backButton);
        destinationLayout.setAlignment(Pos.CENTER);
        destinationLayout.setStyle("-fx-background-color: #f4f4f9; -fx-padding: 20;");
        return new Scene(new StackPane(destinationLayout), 1250, 900);
    }

    // third page to display information about destination
    private Scene createInformationScene(String normalizedResidence, String destination) {
        // instances of classes and methods to get information
        TravelAdvisory Advisory = new TravelAdvisory();
        String advisoryMessage = Advisory.getTravelAdvisory(destination);
        String sourceCurrency = CountryCurrencyMapper.getCurrencyCode(normalizedResidence);
        String targetCurrency = CountryCurrencyMapper.getCurrencyCode(destination);
        CurrencyConverter Converter = new CurrencyConverter();
        double exchangeRate = Converter.getExchangeRate(sourceCurrency, targetCurrency);

        // labels for advisory message
        Label advisoryLabel = new Label("Travel Advisory for " + destination + ":");
        advisoryLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        Label messageLabel = new Label(advisoryMessage);
        messageLabel.setFont(new Font("Comic Sans MS", 16));

        // user input for amount of money
        Label moneyLabel = new Label("How much money do you plan to bring on this trip?");
        moneyLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        TextField moneyField = new TextField();
        moneyField.setPromptText("Enter amount");
        moneyField.setMaxWidth(200);

        // converted amount
        Label convertedAmountLabel = new Label();
        convertedAmountLabel.setFont(new Font("Comic Sans MS", 16));
        convertedAmountLabel.setTextFill(Color.rgb(50, 50, 50));
        moneyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || !newValue.matches("\\d+(\\.\\d{1,2})?")) {
                convertedAmountLabel.setText("Please enter a valid amount.");
            } else {
                double money = Double.parseDouble(newValue);
                double convertedAmount = Converter.convertCurrency(money, exchangeRate);
                convertedAmountLabel.setText(String.format("%.2f %s = %.2f %s", money, sourceCurrency, convertedAmount, targetCurrency));
            }
        });

        // back button to destination page
        Button backButton = new Button("<-Back");
        backButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #003f7f; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 16; -fx-background-radius: 12;"));
        backButton.setOnAction(e -> primaryStage.setScene(createDestinationScene(normalizedResidence)));
        backButton.setEffect(new DropShadow(5, Color.GRAY));

        // arrangement of components
        VBox informationLayout = new VBox(15);
        informationLayout.getChildren().addAll(advisoryLabel, messageLabel, moneyLabel, moneyField, convertedAmountLabel, backButton);
        VBox.setMargin(moneyLabel, new Insets(50, 0, 0, 0));
        informationLayout.setStyle("-fx-background-color: #f4f4f9; -fx-padding: 20;");
        informationLayout.setAlignment(Pos.CENTER);
        return new Scene(informationLayout, 1250, 900);
    }
}