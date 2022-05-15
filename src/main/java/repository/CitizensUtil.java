package repository;

import model.CitizenAndAddress;

import java.util.*;
import java.util.stream.Collectors;


public class CitizensUtil {
    public static Map<String, Integer> getCitiesAndPopulationFromList(List<CitizenAndAddress> citizens) {
        Map<String, Integer> citiesAndPopulation = new LinkedHashMap<>();
        citizens.stream().map(CitizenAndAddress::getCity).forEach(el -> {
                    if (citiesAndPopulation.containsKey(el)) {
                        Integer num = citiesAndPopulation.get(el);
                        citiesAndPopulation.put(el, ++num);
                    } else {
                        citiesAndPopulation.put(el, 1);
                    }
                }
        );
        return citiesAndPopulation;
    }
}
