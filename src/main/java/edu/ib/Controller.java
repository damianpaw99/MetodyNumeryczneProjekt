package edu.ib;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;

public class Controller {

    @FXML
    private Text txttime;

    @FXML
    private TextField txtm1;

    @FXML
    private TextField txtx1;

    @FXML
    private TextField txty1;

    @FXML
    private TextField txtvx1;

    @FXML
    private TextField txtvy1;

    @FXML
    private TextField txtm2;

    @FXML
    private TextField txtx2;

    @FXML
    private TextField txty2;

    @FXML
    private TextField txtvx2;

    @FXML
    private TextField txtvy2;

    @FXML
    private Button btncalculate;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField txtendt;

    @FXML
    private Button btnstart;

    @FXML
    private Button btnstop;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private LineChart<Double, Double> plot;

    @FXML
    private Button btnreset;

    private static final double STEP = 0.01;
    private int n = 0;
    private ArrayList<Double> x1Array = new ArrayList<>();
    private ArrayList<Double> y1Array = new ArrayList<>();
    private ArrayList<Double> x2Array = new ArrayList<>();
    private ArrayList<Double> y2Array = new ArrayList<>();
    private ScheduledExecutorService service;

    @FXML
    void calculate(ActionEvent event) {
        btncalculate.setDisable(true);
        n=0;
        try {
            double x1 = Double.parseDouble(txtx1.getText());
            double y1 = Double.parseDouble(txty1.getText());

            double x2 = Double.parseDouble(txtx2.getText());
            double y2 = Double.parseDouble(txty2.getText());

            double vx1 = Double.parseDouble(txtvx1.getText());
            double vy1 = Double.parseDouble(txtvy1.getText());

            double vx2 = Double.parseDouble(txtvx2.getText());
            double vy2 = Double.parseDouble(txtvy2.getText());

            double m1= Double.parseDouble(txtm1.getText());
            double m2= Double.parseDouble(txtm2.getText());

            double tStop = Double.parseDouble(txtendt.getText());

            ConsoleStepper consoleStepper = new ConsoleStepper();

            Particle p1 = new Particle(x1, y1, vx1, vy1, m1,(r) -> 24*(2.0/Math.pow(r,13)-1.0/Math.pow(r, 7)));
            Particle p2 = new Particle(x2, y2, vx2, vy2, m2,(r) -> 24*(2.0/Math.pow(r,13)-1.0/Math.pow(r, 7)));
            Particle[] particle = {p1, p2};
            VerletIntegrator verletIntegrator = new VerletIntegrator(particle, consoleStepper);
            verletIntegrator.integrate(0, tStop, STEP);
            btnstart.setDisable(false);

            x1Array.clear();
            x2Array.clear();
            y1Array.clear();
            y2Array.clear();

            x1Array.addAll(consoleStepper.getX1List());
            x2Array.addAll(consoleStepper.getX2List());
            y1Array.addAll(consoleStepper.getY1List());
            y2Array.addAll(consoleStepper.getY2List());
            btnstart.setDisable(false);
            btnreset.setDisable(false);
            XYChart.Series point1 = new XYChart.Series();
            XYChart.Series point2 = new XYChart.Series();

            point1.getData().add(new XYChart.Data(x1,y1));
            point2.getData().add(new XYChart.Data(x2,y2));

            plot.getData().clear();
            plot.getData().add(point1);
            plot.getData().add(point2);

        } catch (Exception e) {
            e.getStackTrace();
            btnstart.setDisable(true);
            btnstop.setDisable(true);
            btnreset.setDisable(true);
            n = 0;
        }
        progressBar.setProgress(0);
        txttime.setText("0");
        btncalculate.setDisable(false);
    }

    @FXML
    void start(ActionEvent event) {
        btnstop.setDisable(false);
        btnstart.setDisable(true);
        btncalculate.setDisable(true);
        if (n >= x1Array.size()) {
            n = 0;
        }
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                updateGUI();
            });
            n+=10;

            if (n >= x1Array.size()) {
                Platform.runLater(()->{
                    endAnimation();
                });
                service.shutdown();

            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    @FXML
    void stop(ActionEvent event) {
        service.shutdown();
        endAnimation();
    }

    @FXML
    void reset(ActionEvent event) {
        try {
            stop(event);
        } catch (Exception e){
            e.getStackTrace();
        }
        n=0;
        progressBar.setProgress(0);
        txttime.setText("0");
    }
    private void updateGUI() {
        XYChart.Series point1 = new XYChart.Series();
        XYChart.Series point2 = new XYChart.Series();

        point1.getData().add(new XYChart.Data(x1Array.get(n - 1), y1Array.get(n - 1)));
        point2.getData().add(new XYChart.Data(x2Array.get(n - 1), y2Array.get(n - 1)));

        plot.getData().clear();
        plot.getData().add(point1);
        plot.getData().add(point2);
        progressBar.setProgress(n / (double) x1Array.size());
        txttime.setText(String.valueOf(Math.floor(n*STEP*10)/10));
    }

    private void endAnimation() {
        btnstop.setDisable(true);
        btnstart.setDisable(false);
        btncalculate.setDisable(false);
    }

    @FXML
    void initialize() {
        assert txtm1 != null : "fx:id=\"txtm1\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtx1 != null : "fx:id=\"txtx1\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txty1 != null : "fx:id=\"txty1\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtvx1 != null : "fx:id=\"txtvx1\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtvy1 != null : "fx:id=\"txtvy1\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtm2 != null : "fx:id=\"txtm2\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtx2 != null : "fx:id=\"txtx2\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txty2 != null : "fx:id=\"txty2\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtvx2 != null : "fx:id=\"txtvx2\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtvy2 != null : "fx:id=\"txtvy2\" was not injected: check your FXML file 'simulation.fxml'.";
        assert btncalculate != null : "fx:id=\"btncalculate\" was not injected: check your FXML file 'simulation.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txtendt != null : "fx:id=\"txtendt\" was not injected: check your FXML file 'simulation.fxml'.";
        assert btnstart != null : "fx:id=\"btnstart\" was not injected: check your FXML file 'simulation.fxml'.";
        assert btnstop != null : "fx:id=\"btnstop\" was not injected: check your FXML file 'simulation.fxml'.";
        assert plot != null : "fx:id=\"plot\" was not injected: check your FXML file 'simulation.fxml'.";
        assert xAxis != null : "fx:id=\"xAxis\" was not injected: check your FXML file 'simulation.fxml'.";
        assert yAxis != null : "fx:id=\"yAxis\" was not injected: check your FXML file 'simulation.fxml'.";
        assert txttime != null : "fx:id=\"txttime\" was not injected: check your FXML file 'simulation.fxml'.";
        assert btnreset != null : "fx:id=\"btnreset\" was not injected: check your FXML file 'simulation.fxml'.";

        btnstart.setDisable(true);
        btnstop.setDisable(true);
        plot.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        xAxis.setLowerBound(-10);
        yAxis.setLowerBound(-10);
        xAxis.setUpperBound(10);
        yAxis.setUpperBound(10);
        plot.setAnimated(false);
        btnreset.setDisable(true);
        plot.setLegendVisible(false);
    }

}
