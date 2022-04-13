import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;

/**
 * This class defines the game Jordle. A word-guessing game based on the game Wordle.
 * @author Michael Hammond
 * @version 1.32
 */
public class Jordle extends Application {
    final Color defaultColor = Color.rgb(18, 18, 19);
    final Color incorrectColor = Color.rgb(86, 87, 88);
    final Color correctColor = Color.rgb(83, 141, 78);
    final Color includedColor = Color.rgb(181, 159, 59);

    final Color hcCorrectColor = Color.rgb(245, 121, 58);
    final Color hcIncludedColor = Color.rgb(133, 192, 249);

    boolean highContrast = false;
    boolean darkMode = true;
    int row = 0;
    int column = 0;
    int lettersInRow = 0;

    String guess = "";
    String correctWord = Words.list.get((int) (Math.random() * Words.list.size())).toUpperCase();

    /**
     * This method starts the game window. It overrides the start method from Application.
     * @param primaryStage Main stage that the game window is displayed on.
     */
    @Override
    public void start(Stage primaryStage) {

        // Main Stage
        primaryStage.setTitle("Jordle");
        primaryStage.getIcons().add(new Image("https://i.imgur.com/V2WmpOw.png"));

        BorderPane mainPane = new BorderPane();
        mainPane.setStyle(darkMode ? "-fx-background-color: rgb(18, 18, 19);" : "-fx-background-color: WHITE;");
        GridPane grid = new GridPane(); // Center
        HBox headerBox = new HBox();

        Scene scene = new Scene(mainPane, 960, 960);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Header
        mainPane.setTop(headerBox);
        headerBox.setStyle("-fx-border-color: transparent transparent rgb(58, 58, 60) transparent;");
        headerBox.setSpacing(50);

        // Instructions Button
        Button instrButton = new Button("Instructions");
        instrButton.setStyle("-fx-background-color: #909098; -fx-border: none; -fx-text-fill: white;");
        instrButton.setOnMouseEntered(e -> {
            instrButton.setStyle("-fx-hover: #3b3b3b; -fx-border: none; -fx-text-fill: black;");
        });
        instrButton.setOnMouseExited(e -> {
            instrButton.setStyle("-fx-background-color: #909098; -fx-border: none; -fx-text-fill: white;");
        });
        headerBox.getChildren().add(instrButton);
        instrButton.setFocusTraversable(false);

        // Instructions Stage
        Stage instructionStage = new Stage();
        instructionStage.getIcons().add(new Image("https://i.imgur.com/V2WmpOw.png"));
        instructionStage.setTitle("Instructions");
        instructionStage.setResizable(false);

        VBox instructionsText = new VBox();
        instructionsText.setPadding(new Insets(10, 5, 30, 5));
        instructionsText.setSpacing(10);

        Scene instructionScene = new Scene(instructionsText, 505, 505);
        instructionsText.setStyle(darkMode ? "-fx-background-color: rgb(18, 18, 19);" : "-fx-background-color: WHITE");

        HBox howToPlayBox = new HBox();
        howToPlayBox.setPadding(new Insets(0, 5, 0, 5));
        howToPlayBox.setSpacing(10);
        howToPlayBox.setAlignment(Pos.CENTER);


        Text howToPlay = new Text("HOW TO PLAY");
        howToPlay.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 18));
        howToPlay.setFill(darkMode ? Color.WHITE : Color.BLACK);
        howToPlayBox.getChildren().add(howToPlay);
        instructionsText.getChildren().add(howToPlayBox);

        Text instructionsWords1 = new Text("Guess the JORDLE in six tries.");
        instructionsWords1.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        instructionsWords1.setFill(darkMode ? Color.WHITE : Color.BLACK);

        Text instructionsWords2 = new Text(
                "Each guess must be a valid five-letter word. Hit the enter button to submit.");
        instructionsWords2.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        instructionsWords2.setFill(darkMode ? Color.WHITE : Color.BLACK);


        Text instructionsWords3 = new Text(
                "After each guess, the color of the tiles will change to show how close your\nguess was to the word.");
        instructionsWords3.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        instructionsWords3.setFill(darkMode ? Color.WHITE : Color.BLACK);

        instructionsText.getChildren().addAll(instructionsWords1, instructionsWords2, instructionsWords3);

        Line spacer = new Line(0, 250, 495, 250);
        spacer.setStroke(incorrectColor);

        Text examples = new Text("Examples");
        examples.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 15));
        examples.setFill(darkMode ? Color.WHITE : Color.BLACK);

        Text wearyText = new Text("The Letter W is in the word and in the correct spot.");
        wearyText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        wearyText.setFill(darkMode ? Color.WHITE : Color.BLACK);

        Text pillsText = new Text("The letter I is in the word but in the wrong spot.");
        pillsText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        pillsText.setFill(darkMode ? Color.WHITE : Color.BLACK);

        Text vagueText = new Text("None of the letters are in the word.");
        vagueText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        vagueText.setFill(darkMode ? Color.WHITE : Color.BLACK);

        instructionsText.getChildren().addAll(spacer, examples,
                newWord("WEARY", new int[] {1, 2, 3, 4}, new int[] {0}, new int[] {}), wearyText,
                newWord("PILLS", new int[] {0, 2, 3, 4}, new int[] {}, new int[] {1}), pillsText,
                newWord("VAGUE", new int[] {0, 1, 2, 3, 4}, new int[] {}, new int[] {}), vagueText);

        // Anonymous Inner Class for Instructions Button
        instrButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                instructionStage.setScene(instructionScene);
                instructionStage.show();
            }
        });

        instrButton.disableProperty().bind(instructionStage.showingProperty());

        // Header Title
        Text headerText = new Text("Jordle");
        headerText.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 50));
        headerText.setFill(darkMode ? Color.WHITE : Color.BLACK);
        headerBox.getChildren().add(headerText);
        headerBox.setAlignment(Pos.CENTER);

        //Settings Button
        Button settingsButton = new Button("Settings");
        settingsButton.setStyle("-fx-background-color: #909098; -fx-border: none; -fx-text-fill: white;");
        settingsButton.setOnMouseEntered(e -> {
            settingsButton.setStyle("-fx-hover: #3b3b3b; -fx-border: none; -fx-text-fill: black;");
        });
        settingsButton.setOnMouseExited(e -> {
            settingsButton.setStyle("-fx-background-color: #909098; -fx-border: none; -fx-text-fill: white;");
        });
        headerBox.getChildren().add(settingsButton);
        settingsButton.setFocusTraversable(false);

        // Settings Stage
        Stage settingsStage = new Stage();
        settingsStage.getIcons().add(new Image("https://i.imgur.com/yUk0qmP.png"));
        settingsStage.setTitle("Settings");
        settingsStage.setResizable(false);

        VBox mainSettingsBox = new VBox();
        mainSettingsBox.setPadding(new Insets(10, 5, 30, 5));
        mainSettingsBox.setSpacing(10);

        Scene settingsScene = new Scene(mainSettingsBox, 210, 200);
        mainSettingsBox.setStyle(darkMode ? "-fx-background-color: rgb(18, 18, 19);" : "-fx-background-color: WHITE");
        settingsStage.setScene(settingsScene);

        HBox lightModeBox = new HBox();
        lightModeBox.setPadding(new Insets(10, 5, 30, 5));
        lightModeBox.setSpacing(10);
        RadioButton lightModeButton = new RadioButton();
        lightModeButton.setFocusTraversable(false);
        lightModeButton.setSelected(!darkMode);
        Text darkModeText = new Text("Light Mode");
        darkModeText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        darkModeText.setFill(darkMode ? Color.WHITE : Color.BLACK);
        lightModeBox.getChildren().addAll(lightModeButton, darkModeText);

        mainSettingsBox.getChildren().add(lightModeBox);

        HBox highContrastBox = new HBox();
        highContrastBox.setPadding(new Insets(10, 5, 30, 5));
        highContrastBox.setSpacing(10);
        RadioButton highContrastButton = new RadioButton();
        highContrastButton.setFocusTraversable(false);
        highContrastButton.setSelected(highContrast);
        Text highContrastText = new Text("High Contrast Mode");
        highContrastText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        highContrastText.setFill(darkMode ? Color.WHITE : Color.BLACK);
        highContrastBox.getChildren().addAll(highContrastButton, highContrastText);

        mainSettingsBox.getChildren().add(highContrastBox);

        HBox warningBox = new HBox();
        Text warning = new Text("Selecting these settings will\nRESTART the current game.");
        warning.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        warning.setFill(Color.RED);
        warningBox.getChildren().add(warning);
        warningBox.setAlignment(Pos.CENTER_LEFT);

        mainSettingsBox.getChildren().add(warning);

        // Lambda Expression for Settings Button
        settingsButton.setOnAction(e -> {
            settingsStage.setScene(settingsScene);
            settingsStage.show();
        });

        settingsButton.disableProperty().bind(settingsStage.showingProperty());

        // Lambda Expression for Darkmode Button
        lightModeButton.setOnAction(e -> {
            primaryStage.hide();
            settingsStage.hide();
            instructionStage.hide();
            darkMode = !darkMode;
            reset();
            start(primaryStage);
        });

        // Lambda Expression for High Contrast Button
        highContrastButton.setOnAction(e -> {
            primaryStage.hide();
            settingsStage.hide();
            instructionStage.hide();
            highContrast = !highContrast;
            reset();
            start(primaryStage);
        });

        // Grid setup
        mainPane.setCenter(grid);
        grid.setAlignment(Pos.CENTER); // Position grid in the center of the center node of BorderPane.
        for (int c = 0; c < 5; c++) {
            for (int r = 0; r < 6; r++) {
                grid.add(newRectangle(1, '\u0000'), c, r);
            }
        }
        grid.setHgap(5.0);
        grid.setVgap(5.0);

        // Bottom of Primary Stage
        VBox bottomBox = new VBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(0, 20, 100, 20));
        bottomBox.setSpacing(100);

        Button resetButton = new Button("Restart");
        resetButton.setStyle("-fx-background-color: #909098; -fx-border: none; -fx-text-fill: white;");
        resetButton.setOnMouseEntered(e -> {
            resetButton.setStyle("-fx-hover: #3b3b3b; -fx-border: none; -fx-text-fill: black;");
        });
        resetButton.setOnMouseExited(e -> {
            resetButton.setStyle("-fx-background-color: #909098; -fx-border: none; -fx-text-fill: white;");
        });
        bottomBox.getChildren().add(resetButton);
        resetButton.setFocusTraversable(false);

        resetButton.setOnAction(e -> {
            primaryStage.hide();
            instructionStage.hide();
            settingsStage.hide();

            reset();

            start(primaryStage);

        });

        Text tryMessage = new Text("Try guessing a word!");
        tryMessage.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        tryMessage.setFill(darkMode ? Color.WHITE : Color.BLACK);
        bottomBox.getChildren().add(tryMessage);

        mainPane.setBottom(bottomBox);

        // Game Functionality
        scene.setOnKeyPressed(e -> {
            char letterGuess = e.getCode().getName().charAt(0);
            if (lettersInRow < 5 && e.getCode().isLetterKey()) { // Typing Letters
                grid.add(newRectangle(1, letterGuess), column, row);
                guess += letterGuess;
                ++lettersInRow;
                ++column;
            } else if (lettersInRow > 0 && lettersInRow <= 5 && e.getCode() == KeyCode.BACK_SPACE) { //Deleting Letters
                grid.add(newRectangle(1, '\u0000'), column - 1, row);
                guess = guess.substring(0, guess.length() - 1);
                --lettersInRow;
                --column;
            } else if (e.getCode() == KeyCode.ENTER) { // Guessing Words
                if (lettersInRow == 5) { // Valid Guess
                    if (guess.equals(correctWord)) { // Correct Guess
                        grid.add(newRectangle(3, correctWord.charAt(0)), 0, row);
                        grid.add(newRectangle(3, correctWord.charAt(1)), 1, row);
                        grid.add(newRectangle(3, correctWord.charAt(2)), 2, row);
                        grid.add(newRectangle(3, correctWord.charAt(3)), 3, row);
                        grid.add(newRectangle(3, correctWord.charAt(4)), 4, row);
                        tryMessage.setText("Congratulations! You guessed the correct word!");
                        tryMessage.setFill(Color.LIMEGREEN);
                    } else { // Incorrect Guess
                        if (row == 5) { // Final Guess
                            grid.add(newRectangle(2, guess.charAt(0)), 0, row);
                            grid.add(newRectangle(2, guess.charAt(1)), 1, row);
                            grid.add(newRectangle(2, guess.charAt(2)), 2, row);
                            grid.add(newRectangle(2, guess.charAt(3)), 3, row);
                            grid.add(newRectangle(2, guess.charAt(4)), 4, row);
                            tryMessage.setText("GAME OVER! The word was " + correctWord + ".");
                            tryMessage.setFill(Color.RED);
                        } else {
                            for (int i = 0; i < 5; i++) {
                                if (guess.charAt(i) == correctWord.charAt(i)) {
                                    grid.add(newRectangle(3, guess.charAt(i)), i, row);
                                } else if (correctWord.contains(String.valueOf(guess.charAt(i)))) {
                                    grid.add(newRectangle(4, guess.charAt(i)), i, row);
                                } else {
                                    grid.add(newRectangle(2, guess.charAt(i)), i, row);
                                }
                            }
                            guess = "";
                            lettersInRow = 0;
                            column = 0;
                            ++row;
                            System.out.println(row);
                        }
                    }
                } else { // Invalid Guess
                    Alert invalidGuessAlert = new Alert(Alert.AlertType.ERROR, "You must guess a five letter word!",
                            ButtonType.CLOSE);
                    invalidGuessAlert.show();
                }
            }
        });

    }

    /**
     * This method resets the variables row, column, lettersInRow, guess to their default values and picks a new
     * correctWord at random.
     */
    public void reset() {
        this.row = 0;
        this.column = 0;
        this.lettersInRow = 0;
        this.guess = "";
        this.correctWord = Words.list.get((int) (Math.random() * Words.list.size())).toUpperCase();
    }

    /**
     * This method creates a rectangle in the form of a StackPane that will be displayed in different colors based on
     * their letter.
     * @param type The type of rectangle to be created: empty, correct, incorrect, included.
     * @param letter The letter that will be displayed inside the rectangle.
     * @return Returns a StackPane of a colored rectangle with a letter in the middle.
     */
    public StackPane newRectangle(int type, char letter) {
        StackPane letterOnRectangle = new StackPane();
        Rectangle rec = new Rectangle(60, 60);
        try {
            if (type == 1) { // Empty
                rec.setStroke(incorrectColor);
                rec.setStrokeWidth(1.5);
                rec.setFill(darkMode ? defaultColor : Color.WHITE);
            }
            if (type == 2) { // Incorrect Letter
                rec.setStroke(incorrectColor);
                rec.setStrokeWidth(1.5);
                rec.setFill(incorrectColor);
            }
            if (type == 3) { // Correct Letter
                rec.setFill(highContrast ? hcCorrectColor : correctColor);
                rec.setStroke(highContrast ? hcCorrectColor : correctColor);
                rec.setStrokeWidth(1.5);
            }
            if (type == 4) { // Close Letter
                rec.setFill(highContrast ? hcIncludedColor : includedColor);
                rec.setFill(highContrast ? hcIncludedColor : includedColor);
                rec.setStrokeWidth(1.5);
            }
            Text character = new Text(Character.toString(letter));
            character.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
            character.setFill(darkMode ? Color.WHITE : Color.BLACK);
            letterOnRectangle.getChildren().addAll(rec, character);
            return letterOnRectangle;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(type + " is not a valid rectangle type!");
        }
    }

    /**
     * This method creates a series of rectangles to be displayed next to each other.
     * @param word Word that rectangles will spell out.
     * @param indexWrong Indices of letters not in the correct word.
     * @param indexRight Indices of letters in the correct word and in the correct spot.
     * @param indexClose Indices of letters in the correct word but not in the correct spot.
     * @return Returns a series of colored rectangles with letters in the middle that form a word.
     */
    public GridPane newWord(String word, int[] indexWrong, int[] indexRight, int[] indexClose) {
        if (word.length() == 5) {
            GridPane grid = new GridPane();
            grid.setHgap(5.0);
            grid.setVgap(5.0);
            for (int i = 0; i < word.length(); i++) {
                for (int value : indexWrong) {
                    if (i == value) {
                        grid.add(newRectangle(2, word.charAt(i)), i, 0);
                    }
                }
                for (int k : indexRight) {
                    if (i == k) {
                        grid.add(newRectangle(3, word.charAt(i)), i, 0);
                    }
                }
                for (int j : indexClose) {
                    if (i == j) {
                        grid.add(newRectangle(4, word.charAt(i)), i, 0);
                    }
                }
            }
            return grid;
        } else {
            return new GridPane();
        }
    }
}