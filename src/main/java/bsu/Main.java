package bsu;

import bsu.RMI.RMIInterface;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import bsu.model.CitizenAndAddress;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Main extends Application {
    private static RMIInterface service;

    public static void main(String[] args) {
        try {
            System.out.println("Connecting to server");
            Registry registry = LocateRegistry.getRegistry(1099);
            service = (RMIInterface) registry.lookup("Server");
            System.out.println("Connected to server");
            launch(args);
        } catch (NotBoundException | RemoteException e) {
            System.out.println("Cannot connect to server");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Lab 2");
        GridPane pane = new GridPane();
        pane.setHgap(30);
        Scene scene = new Scene(pane, 700, 500);
        ListView<CitizenAndAddress> listView = new ListView<>();
        ObservableList<CitizenAndAddress> list = FXCollections.observableList(service.getList());
        listView.setItems(list);
        listView.setPrefHeight(500);
        pane.add(listView, 0, 0);
        ListView<String> listOfCitiesAndPopulations = new ListView<>();
        listOfCitiesAndPopulations.setPrefHeight(500);
        Label label = new Label();
        Button buttonCount = new Button("Count");
        buttonCount.setOnAction(actionEvent -> {
            try {
                label.setText("Number of citizens " + service.getNumber());
                if (!pane.getChildren().contains(label)) {
                    pane.getChildren().remove(listOfCitiesAndPopulations);
                } else {
                    pane.getChildren().remove(label);
                }
                pane.add(label, 2, 0);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        Button buttonCities = new Button("Cities");
        buttonCities.setOnAction(actionEvent -> {
            try {
                List<String> cities = new ArrayList<>();
                Set<Map.Entry<String, Integer>> citiesAndPopulationEntrySet = service.getCitiesAndPopulation().entrySet();
                for (Map.Entry<String, Integer> element : citiesAndPopulationEntrySet
                ) {
                    cities.add(element.getKey() + " " + element.getValue());
                }
                ObservableList<String> citiesAndPopulation = FXCollections.observableList(cities);
                listOfCitiesAndPopulations.setItems(citiesAndPopulation);
                if (!pane.getChildren().contains(listOfCitiesAndPopulations)) {
                    pane.getChildren().remove(label);
                } else {
                    pane.getChildren().remove(listOfCitiesAndPopulations);
                }
                pane.add(listOfCitiesAndPopulations, 2, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        Button buttonAdd = new Button("Add citizen");
        buttonAdd.setOnAction(actionEvent -> {
            TextInputDialog textInputDialog = new TextInputDialog("Add citizen");
            Optional<String> result = textInputDialog.showAndWait();
            result.ifPresent(this::addToDB);
            refresh(listView, list);
        });

        Button buttonDelete = new Button("Delete citizen");
        buttonDelete.setOnAction(actionEvent -> {
            try {
                CitizenAndAddress citizenAndAddress = listView.getSelectionModel().getSelectedItem();
                if (citizenAndAddress != null) {
                    service.deleteCitizen(citizenAndAddress);
                    refresh(listView, list);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.add(buttonCount, 0, 0);
        gridPane.add(buttonCities, 1, 0);
        gridPane.add(buttonAdd, 0, 1);
        gridPane.add(buttonDelete, 1, 1);

        pane.add(gridPane, 1, 0);
        stage.setScene(scene);
        stage.show();
    }

    private void addToDB(String citizenAndAddressLine) {
        Scanner scanner = new Scanner(citizenAndAddressLine);
        try {
            String name = scanner.next();
            String surname = scanner.next();
            String city = scanner.next();
            String street = scanner.next();
            Integer building = Integer.parseInt(scanner.next());
            CitizenAndAddress citizenAndAddress = new CitizenAndAddress(name, surname, city, street, building);
            service.addCitizen(citizenAndAddress);
        } catch (NoSuchElementException | NumberFormatException | RemoteException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Wrong input");
            alert.show();
        }
    }

    private void refresh(ListView<CitizenAndAddress> listView, ObservableList<CitizenAndAddress> list) {
        list.clear();
        try {
            list.addAll(service.getList());
            listView.setItems(list);
        } catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.show();
        }
    }
}
