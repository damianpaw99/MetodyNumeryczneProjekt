package edu.ib;

import java.util.ArrayList;

public class ConsoleStepper implements ODEUpdate{

    private ArrayList<Double> tList;
    private ArrayList<Double> x1List;
    private ArrayList<Double> y1List;
    private ArrayList<Double> x2List;
    private ArrayList<Double> y2List;



    public ConsoleStepper() {
        tList=new ArrayList<>();
        x1List=new ArrayList<>();
        x2List=new ArrayList<>();
        y1List=new ArrayList<>();
        y2List=new ArrayList<>();


    }

    @Override
    public void update(double t, double x1, double y1, double x2, double y2) {
        tList.add(t);
        x1List.add(x1);
        x2List.add(x2);
        y1List.add(y1);
        y2List.add(y2);
    }

    public ArrayList<Double> gettList() {
        return tList;
    }

    public ArrayList<Double> getX1List() {
        return x1List;
    }

    public ArrayList<Double> getY1List() {
        return y1List;
    }

    public ArrayList<Double> getX2List() {
        return x2List;
    }

    public ArrayList<Double> getY2List() {
        return y2List;
    }

}
