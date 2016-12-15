/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONException;
import org.json.JSONObject;
import testapp.clock.DigitalClock;
import testapp.comm.SerialCommunicator;
import testapp.hibernate.dao.DAO;
import testapp.hibernate.pojo.DeviceInput;
import testapp.hibernate.pojo.DeviceSettings;

/**
 *
 * @author fazeem
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    Label clockLbl;

    //ChartSection
    @FXML
    StackedAreaChart phChart;
    @FXML
    StackedAreaChart salinityChart;

    @FXML
    StackedAreaChart tmpChart;

    @FXML
    StackedAreaChart nitrateChart;

    @FXML
    Label graphTempLbl;
    @FXML
    Label graphSalinityLbl;
    @FXML
    Label graphPhLbl;
    @FXML
    Label graphNitrateLbl;

    @FXML
    Label currentTempLbl;
    @FXML
    Label currentSalinityLbl;
    @FXML
    Label currentPhLbl;
    @FXML
    Label currentNitrateLbl;

    //Table section
    @FXML
    TableView relayTable;
    @FXML
    TableColumn relay;
    @FXML
    TableColumn onTime;
    @FXML
    TableColumn offTime;
    @FXML
    TableColumn repeatTime;
    @FXML
    TableColumn repeatInterval;

    @FXML
    TableView censorTable;
    @FXML
    TableColumn censor;
    @FXML
    TableColumn actual;
    @FXML
    TableColumn multiplier;
    @FXML
    TableColumn correction;
    @FXML
    TableColumn finalReading;

    @FXML
    TableView censorTable2;
    @FXML
    TableColumn digital;
    @FXML
    TableColumn status;
    @FXML
    TableColumn enable;

    //Spinners
    @FXML
    Spinner tempSpinner;
    @FXML
    Spinner salinitySpinner;
    @FXML
    Spinner phSpinner;
    @FXML
    Spinner nitrateSpinner;
    public SerialCommunicator serialCommunicator = null;
    

    private final ObservableList<Relay> data
            = FXCollections.observableArrayList();
    private final ObservableList<CensorReading> censorReadingdata
            = FXCollections.observableArrayList();
    private final ObservableList<CensorStatus> censorStatusgdata
            = FXCollections.observableArrayList();
    
    private DeviceSettings deviceSettings;

    @FXML
    private void handleEditBtnAction(ActionEvent event) {
        tempSpinner.setDisable(false);
        salinitySpinner.setDisable(false);
        phSpinner.setDisable(false);
        nitrateSpinner.setDisable(false);;
    }

    @FXML
    private void handleSaveBtnAction(ActionEvent event) {
        tempSpinner.setDisable(true);
        salinitySpinner.setDisable(true);
        phSpinner.setDisable(true);
        nitrateSpinner.setDisable(true);
        JSONObject obj=new JSONObject();
        try {
            obj.put("T1", Integer.parseInt(tempSpinner.getValue().toString()));
           
            obj.put("SL", Integer.parseInt(salinitySpinner.getValue().toString()));
            
            obj.put("PH", Integer.parseInt(phSpinner.getValue().toString()));
            
            obj.put("NI", Integer.parseInt(nitrateSpinner.getValue().toString()));
            
             this.deviceSettings.setT1(Integer.parseInt(tempSpinner.getValue().toString()));
            this.deviceSettings.setSL(Integer.parseInt(salinitySpinner.getValue().toString()));
            this.deviceSettings.setPH(Integer.parseInt(phSpinner.getValue().toString()));
            this.deviceSettings.setNI(Integer.parseInt(nitrateSpinner.getValue().toString()));
            DAO.updateDeviceSettings(this.deviceSettings);
            SerialCommunicator.reqBlockingQueue.put(obj);
        } catch (JSONException | InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        new DigitalClock().bindToTime(clockLbl);
        drawLastHourGraph();
        initializeSettings();
        
        startGraphTimer();

        createRelayTable();
        createCensorTable();

        this.serialCommunicator = new SerialCommunicator();
        try {
            this.serialCommunicator.fetchandConnectSerialPorts();
            //this.serialCommunicator.connect("COM3");
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  this.setOnHiding(windowEventHandler);
    }

    public void initializeSettings() {
        DeviceSettings deviceSettings=DAO.fetchDeviceSettings();
        this.deviceSettings=deviceSettings;
        if (deviceSettings != null) {
            tempSpinner.getValueFactory().setValue(deviceSettings.getT1());
            salinitySpinner.getValueFactory().setValue(deviceSettings.getSL());
            phSpinner.getValueFactory().setValue(deviceSettings.getPH());
            nitrateSpinner.getValueFactory().setValue(deviceSettings.getNI());
        }
    }

    public void startGraphTimer() {
        Timer timer = new java.util.Timer();
        Calendar calendar = Calendar.getInstance();
        int mins = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, mins + 1);
        calendar.set(Calendar.SECOND, 0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    drawLastHourGraph();
                });
            }

        }, calendar.getTime(), 60 * 1000);
    }

    public void drawLastHourGraph() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date lastHour = cal.getTime();
        List<DeviceInput> deviceInputs = DAO.readLasthourData(lastHour);
        System.out.println("Last HourData Is" + deviceInputs);
        createGraphs(deviceInputs, lastHour);
    }

    public void createGraphs(List<DeviceInput> deviceInputs, Date lastHour) {
        XYChart.Series phData = new XYChart.Series();
        XYChart.Series salinityData = new XYChart.Series();
        XYChart.Series tempData = new XYChart.Series();
        XYChart.Series nitrateData = new XYChart.Series();
        int redColor = 0, greenColor = 127, blueColor = 195;
        double opacity = 0.4;

        tempData.getData().clear();
        nitrateData.getData().clear();
        phData.getData().clear();
        salinityData.getData().clear();
        Date currentTime = new Date();
        for (DeviceInput deviceInput : deviceInputs) {
            long diff = currentTime.getTime() - deviceInput.getCreated().getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            // System.out.println("diffMinutes"+diffMinutes);
            // System.out.println(""+deviceInput.getT1());
            tempData.getData().add(new XYChart.Data(diffMinutes, deviceInput.getT1()));
            nitrateData.getData().add(new XYChart.Data(diffMinutes, deviceInput.getNI()));
            salinityData.getData().add(new XYChart.Data(diffMinutes, deviceInput.getSL()));
            phData.getData().add(new XYChart.Data(diffMinutes, deviceInput.getPH()));
        }
        if (deviceInputs.size() > 0) {
            DeviceInput latestDeviceInput = deviceInputs.get(deviceInputs.size() - 1);

            graphTempLbl.setText(latestDeviceInput.getT1().toString());
            currentTempLbl.setText(latestDeviceInput.getT1().toString());

            graphSalinityLbl.setText(latestDeviceInput.getSL().toString());
            currentSalinityLbl.setText(latestDeviceInput.getSL().toString());

            currentPhLbl.setText(latestDeviceInput.getPH().toString());
            graphPhLbl.setText(latestDeviceInput.getPH().toString());

            currentNitrateLbl.setText(latestDeviceInput.getNI().toString());
            graphNitrateLbl.setText(latestDeviceInput.getNI().toString());

            tmpChart.getData().clear();
            salinityChart.getData().clear();
            phChart.getData().clear();
            nitrateChart.getData().clear();

            tmpChart.getData().add(tempData);
            salinityChart.getData().add(salinityData);
            phChart.getData().add(phData);
            nitrateChart.getData().add(nitrateData);
        }
        /*        ampChart.setStyle("CHART_COLOR_1: blue;" +
        "CHART_COLOR_1_TRANS_20: grey;");
        
        ocdChart.setStyle("CHART_COLOR_1: green;" +
        "CHART_COLOR_1_TRANS_20: grey;");
        
        tmpChart.setStyle("CHART_COLOR_1: red;" +
        "CHART_COLOR_1_TRANS_20: grey;");
        
        ampChar.setStyle("CHART_COLOR_1: black;" +
        "CHART_COLOR_1_TRANS_20: grey;");*/
    }

    private void createCensorTable() {
        GUIUtils.autoFitTable(censorTable);
        censorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        actual.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 50% width
        multiplier.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 30% width
        correction.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        finalReading.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        censor.setMaxWidth(1f * Integer.MAX_VALUE * 20);

        actual.setCellValueFactory(
                new PropertyValueFactory<>("actual"));
        multiplier.setCellValueFactory(
                new PropertyValueFactory<>("multiplier"));
        correction.setCellValueFactory(
                new PropertyValueFactory<>("correction"));
        finalReading.setCellValueFactory(
                new PropertyValueFactory<>("finalReading"));
        censor.setCellValueFactory(
                new PropertyValueFactory<>("censor"));
        censorTable.setItems(censorReadingdata);

        String[] censorArray = {"Ph", "Nitrate", "Oxygen", "carbon"};
        int j = 0;
        for (int i = 0; i < 100; i++) {
            CensorReading censor = new CensorReading();
            censor.setActual("343");
            censor.setMultiplier("4554");
            censor.setCorrection("-1");
            censor.setFinalReading("435.56");
            if (i % 4 == 0) {
                j = 0;
            }
            censor.setCensor(censorArray[j]);
            j++;
            censorReadingdata.add(censor);
        }

        censorTable2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        digital.setMaxWidth(1f * Integer.MAX_VALUE * 33.3); // 50% width
        status.setMaxWidth(1f * Integer.MAX_VALUE * 33.3); // 30% width
        enable.setMaxWidth(1f * Integer.MAX_VALUE * 33.3); // 20% width

        digital.setCellValueFactory(
                new PropertyValueFactory<>("digital"));
        status.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        enable.setCellValueFactory(
                new PropertyValueFactory<>("enable"));
        censorTable2.setItems(censorStatusgdata);
        for (int i = 0; i < 100; i++) {
            CensorStatus censorStatus = new CensorStatus();
            censorStatus.setDigital("UV Censoe");
            censorStatus.setEnable("Enable");
            censorStatus.setStatus("ON");
            censorStatusgdata.add(censorStatus);
        }

    }

    private void createRelayTable() {
//         Callback<TableColumn<Relay, String>, TableCell<Relay, String>> cellFactory = (
//        TableColumn<Relay, String> p) -> new EditingCell();

        GUIUtils.autoFitTable(relayTable);
        relayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        relay.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 50% width
        onTime.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 30% width
        offTime.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        repeatTime.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        repeatInterval.setMaxWidth(1f * Integer.MAX_VALUE * 20);

        relay.setCellValueFactory(
                new PropertyValueFactory<>("relay"));
        onTime.setCellValueFactory(
                new PropertyValueFactory<>("onTime"));
        offTime.setCellValueFactory(
                new PropertyValueFactory<>("offTime"));
        repeatTime.setCellValueFactory(
                new PropertyValueFactory<>("repeatTime"));
        repeatInterval.setCellValueFactory(
                new PropertyValueFactory<>("repeatInterval"));

        relayTable.setItems(data);
        for (int i = 0; i < 100; i++) {
            Relay relay1 = new Relay();
            relay1.setRelay("Relay " + i);
            relay1.setOffTime("10:10");
            relay1.setOnTime("10:10");
            relay1.setRepeatInterval("10");
            relay1.setRepeatTime("10;10");
            data.add(relay1);
        }

    }

}
