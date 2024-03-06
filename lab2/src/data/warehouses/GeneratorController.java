package data.warehouses;

import data.warehouses.generator.CsvDataGenerator;
import data.warehouses.generator.DbDataGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GeneratorController {
    private CsvDataGenerator csvDataGenerator = new CsvDataGenerator();

    private DbDataGenerator dbDataGenerator = new DbDataGenerator();

    @FXML
    private Button generateBtn;

    @FXML
    private TextField csvTextField;

    @FXML
    private TextField secondCsvTextField;

    @FXML
    private Slider updateSlider;

    @FXML
    private Label sliderLabel;

    @FXML
    private TextField serviceTextField;

    @FXML
    private TextField secondServiceTextField;

    @FXML
    private TextField breakdownTextField;

    @FXML
    private TextField secondBreakdownTextField;

    @FXML
    private TextField repairTextField;

    @FXML
    private TextField secondRepairTextField;

    @FXML
    private TextField flightTextField;

    @FXML
    private TextField secondFlightTextField;

    @FXML
    private TextField delayTextField;

    @FXML
    private TextField secondDelayTextField;

    @FXML
    private CheckBox updateCheckBox;

    @FXML
    private void generateData() {
        try {
            generateCsvFiles();
            dbDataGenerator.loadSourceData(csvDataGenerator.getPlanes(), csvDataGenerator.getUpdatedPlanes());
            generate();

            alertCorrectGeneration();
        }
        catch (NumberFormatException exc) {
            alertBadNumbers();
        }
        catch (BadInputException exc) {
            alertTooSmallDifference(exc.getMessage());
        }
    }

    private void generateCsvFiles() throws NumberFormatException {
        int planesNumber = Integer.parseInt(csvTextField.getText());
        int toUpdateNumber = (int) updateSlider.getValue();
        int newPlanesNumber = Integer.parseInt(secondCsvTextField.getText());

        checkInput(new int[] {planesNumber, newPlanesNumber});

        csvDataGenerator.savePlanesToCsvFiles(planesNumber, toUpdateNumber, newPlanesNumber);
    }

    private void generate() throws NumberFormatException, BadInputException {
        int[] input = {
                Integer.parseInt(serviceTextField.getText()), Integer.parseInt(secondServiceTextField.getText()),
                Integer.parseInt(breakdownTextField.getText()), Integer.parseInt(secondBreakdownTextField.getText()),
                Integer.parseInt(repairTextField.getText()), Integer.parseInt(secondRepairTextField.getText()),
                Integer.parseInt(flightTextField.getText()), Integer.parseInt(secondFlightTextField.getText()),
                Integer.parseInt(delayTextField.getText()), Integer.parseInt(secondDelayTextField.getText())
        };

        checkInput(input);
        checkInputDifferences(input);
        boolean update = updateCheckBox.isSelected();

        dbDataGenerator.generateAndSaveData(input, update);
    }

    @FXML
    private void changeButtonColor() {
        generateBtn.setStyle("-fx-background-color: #0c4cab");
    }

    @FXML
    private void resetButtonColor() {
        generateBtn.setStyle("-fx-background-color: #b00c3d");
    }

    @FXML
    private void changeSliderParameters() {
        try {
            double maxValue = Double.parseDouble(csvTextField.getText());
            changeSlider(maxValue);
        }
        catch (NumberFormatException exc) {
            updateSlider.setDisable(true);
        }
    }

    @FXML
    private void changeValue() {
        double value = updateSlider.getValue();
        sliderLabel.setText(Integer.toString((int)value));
    }

    public void showSampleUpdate() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sample update");
        alert.setHeaderText(null);
        alert.setContentText(csvDataGenerator.getPlaneUpdateInfo());
        alert.showAndWait();
    }

    private void alertCorrectGeneration() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Files have been correctly generated.");
        alert.showAndWait();
    }

    private void alertBadNumbers() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please, enter correct, positive numbers!");
        alert.showAndWait();
    }

    private void alertTooSmallDifference(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void changeSlider(double max) {
        if (max >= 1) {
            updateSlider.setMax(max);
            updateSlider.setDisable(false);
        }
        else {
            sliderLabel.setText("0");
            updateSlider.setDisable(true);
        }
    }

    private void checkInput(int[] input) throws NumberFormatException {
        for (int i : input) {
            if (i <= 0) throw new NumberFormatException();
        }
    }

    private void checkInputDifferences(int[] input) throws BadInputException {
        if ((input[3] - input[5] < 5) || (input[2] - input[4] < 5))
            throw new BadInputException("The number of repairs must be slightly less than the number of breakdowns");
        if ((input[6] - input[8] < 5) || (input[7] - input[9] < 5))
            throw new BadInputException("The number of delays must be slightly less than the number of flights");
        if (input[0] + input[1] > 25)
            throw new BadInputException("Too many AircraftServices (max 25)");
    }

    @FXML
    private void setFirstSampleValues() {
        csvTextField.setText("150");
        secondCsvTextField.setText("25");
        changeSliderParameters();
        updateSlider.setValue(5.0);
        sliderLabel.setText("5");
        serviceTextField.setText("10");
        secondServiceTextField.setText("2");
        breakdownTextField.setText("1000");
        secondBreakdownTextField.setText("200");
        repairTextField.setText("900");
        secondRepairTextField.setText("170");
        flightTextField.setText("20000");
        secondFlightTextField.setText("2000");
        delayTextField.setText("1000");
        secondDelayTextField.setText("125");
        updateCheckBox.setSelected(true);
    }

    @FXML
    private void setSecondSampleValues() {
        csvTextField.setText("700");
        secondCsvTextField.setText("100");
        changeSliderParameters();
        updateSlider.setValue(15.0);
        sliderLabel.setText("15");
        serviceTextField.setText("18");
        secondServiceTextField.setText("7");
        breakdownTextField.setText("7000");
        secondBreakdownTextField.setText("1000");
        repairTextField.setText("6800");
        secondRepairTextField.setText("885");
        flightTextField.setText("120000");
        secondFlightTextField.setText("30000");
        delayTextField.setText("2200");
        secondDelayTextField.setText("250");
        updateCheckBox.setSelected(true);
    }
}