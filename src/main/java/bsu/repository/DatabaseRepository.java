package bsu.repository;

import bsu.RMI.RMIInterface;
import bsu.model.CitizenAndAddress;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DatabaseRepository implements RMIInterface {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost/";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private Connection connection;

    public DatabaseRepository() {
        try {
            connection = DriverManager
                    .getConnection(DATABASE_URL, USERNAME, PASSWORD);
            connection.setSchema("lab_1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CitizenAndAddress> getList() {
        List<CitizenAndAddress> list = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            try (ResultSet resultSet = statement.executeQuery("select * from lab_1.citizen c\n" +
                    "         left outer join lab_1.address a on c.address_id = a.id\n" +
                    "order by c.id\n")) {
                while (resultSet.next()) {
                    list.add(new CitizenAndAddress(resultSet.getString("name"),
                            resultSet.getString("surname"), resultSet.getString("city"),
                            resultSet.getString("street"), resultSet.getInt("building")));
                }
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Integer> getCitiesAndPopulation() {
        Map<String, Integer> citiesAndPopulation = new LinkedHashMap<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select a.city, count(*) num\n" +
                    "from lab_1.citizen c\n" +
                    "         left outer join lab_1.address a on c.address_id = a.id\n" +
                    "group by a.city\n" +
                    "order by num desc ");
            while (resultSet.next()) {
                citiesAndPopulation.put(
                        resultSet.getString("city"), Integer.parseInt(resultSet.getString("num")));
            }
            return citiesAndPopulation;
        } catch (SQLException e) {
            System.out.println("error");
        }
        return citiesAndPopulation;
    }

    public int getNumber() {
        int number = -1;
        try {
            Statement statement = connection.createStatement();
            try (ResultSet resultSet = statement.executeQuery("select count(*) number from lab_1.citizen")) {
                resultSet.next();
                number = resultSet.getInt("number");
                return number;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public void addCitizen(CitizenAndAddress citizenAndAddress) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO lab_1.citizen(name, surname, address_id) " +
                    "values (?,?,?)");
            preparedStatement.setString(1, citizenAndAddress.getName());
            preparedStatement.setString(2, citizenAndAddress.getSurname());
            preparedStatement.setInt(3, getAddressIdAndIfNotExistsAdd(citizenAndAddress));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getAddressIdAndIfNotExistsAdd(CitizenAndAddress citizenAndAddress) {
        int id = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM lab_1.address " +
                    "WHERE city=? AND street=? AND building=?");
            setDataToPreparedStatement(preparedStatement, citizenAndAddress);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                preparedStatement = connection.prepareStatement("INSERT INTO lab_1.address(city, street, building) " +
                        "values (?,?,?)");
                setDataToPreparedStatement(preparedStatement, citizenAndAddress);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement("SELECT id FROM lab_1.address " +
                        "WHERE city=? AND street=? AND building=?");
                setDataToPreparedStatement(preparedStatement, citizenAndAddress);
            }
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            id = resultSet.getInt("id");
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    private void setDataToPreparedStatement(PreparedStatement preparedStatement, CitizenAndAddress
            citizenAndAddress) {
        try {
            preparedStatement.setString(1, citizenAndAddress.getCity());
            preparedStatement.setString(2, citizenAndAddress.getStreet());
            preparedStatement.setInt(3, citizenAndAddress.getBuilding());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCitizen(CitizenAndAddress citizenAndAddress) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select  c.id from lab_1.citizen c" +
                    "                left outer join lab_1.address a on a.id = c.address_id" +
                    "                where name=? and surname=? and a.city=? and a.street=?" +
                    "                  and a.building=?");
            preparedStatement.setString(1, citizenAndAddress.getName());
            preparedStatement.setString(2, citizenAndAddress.getSurname());
            preparedStatement.setString(3, citizenAndAddress.getCity());
            preparedStatement.setString(4, citizenAndAddress.getStreet());
            preparedStatement.setInt(5, citizenAndAddress.getBuilding());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            preparedStatement = connection.prepareStatement("delete from lab_1.citizen where id=?");
            preparedStatement.setInt(1, resultSet.getInt("id"));
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("select count(*) num " +
                    "from lab_1.citizen c " +
                    "         left join lab_1.address a on a.id = c.address_id " +
                    "where city =? and street=? and building=?");
            preparedStatement.setString(1, citizenAndAddress.getCity());
            preparedStatement.setString(2, citizenAndAddress.getStreet());
            preparedStatement.setInt(3, citizenAndAddress.getBuilding());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int num = resultSet.getInt("num");
            if (num == 0) {
                preparedStatement = connection.prepareStatement("delete from lab_1.address " +
                        "where city=? and street = ? and building = ?");
                preparedStatement.setString(1, citizenAndAddress.getCity());
                preparedStatement.setString(2, citizenAndAddress.getStreet());
                preparedStatement.setInt(3, citizenAndAddress.getBuilding());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

