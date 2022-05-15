package repository;

import RMI.RMIInterface;
import model.CitizenAndAddress;

import javax.xml.stream.*;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StAXRepository implements RMIInterface {
    private static final String PATH = "src/main/resources/stax_address.xml";
    public static final String ADDRESS_TAG = "citizen";
    public static final String NAME_TAG = "name";
    public static final String SURNAME_TAG = "surname";
    public static final String CITY_TAG = "city";
    public static final String STREET_TAG = "street";
    public static final String BUILDING_TAG = "building";
    private XMLEventReader reader;
    private final XMLInputFactory xmlInputFactory;
    private final XMLOutputFactory xmlOutputFactory;

    public StAXRepository() {
        xmlInputFactory = XMLInputFactory.newInstance();
        xmlOutputFactory = XMLOutputFactory.newFactory();
    }

    private void openReader() {
        try {
            reader = xmlInputFactory.createXMLEventReader(new FileReader(PATH));
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeReader() {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CitizenAndAddress> getList() throws RemoteException {
        openReader();
        List<CitizenAndAddress> list = new ArrayList<>();
        CitizenAndAddress citizenAndAddress = null;
        while (reader.hasNext()) {
            try {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    switch (startElement.getName().toString()) {
                        case ADDRESS_TAG:
                            citizenAndAddress = new CitizenAndAddress();
                            break;
                        case NAME_TAG:
                            event = reader.nextEvent();
                            if (citizenAndAddress != null) {
                                citizenAndAddress.setName(event.asCharacters().getData());
                            }
                            break;
                        case SURNAME_TAG:
                            event = reader.nextEvent();
                            if (citizenAndAddress != null) {
                                citizenAndAddress.setSurname(event.asCharacters().getData());
                            }
                            break;
                        case CITY_TAG:
                            event = reader.nextEvent();
                            if (citizenAndAddress != null) {
                                citizenAndAddress.setCity(event.asCharacters().getData());
                            }
                            break;
                        case STREET_TAG:
                            event = reader.nextEvent();
                            if (citizenAndAddress != null) {
                                citizenAndAddress.setStreet(event.asCharacters().getData());
                            }
                            break;
                        case BUILDING_TAG:
                            event = reader.nextEvent();
                            if (citizenAndAddress != null) {
                                citizenAndAddress.setBuilding(Integer.parseInt(event.asCharacters().getData()));
                            }
                            break;
                    }
                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(ADDRESS_TAG)) {
                        list.add(citizenAndAddress);
                    }
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        closeReader();
        return list;
    }

    @Override
    public Map<String, Integer> getCitiesAndPopulation() throws RemoteException {
        openReader();
        Map<String, Integer> citiesAndPopulation = CitizensUtil.getCitiesAndPopulationFromList(this.getList());
        closeReader();
        return citiesAndPopulation;
    }

    @Override
    public int getNumber() throws RemoteException {
        openReader();
        int size = getList().size();
        closeReader();
        return size;
    }

    @Override
    public void addCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException {
        try {
            openReader();
            List<CitizenAndAddress> list = getList();
            closeReader();
            if (!list.contains(citizenAndAddress)) {
                list.add(citizenAndAddress);
                XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(new FileWriter(PATH));
                writer.writeStartDocument("windows-1251", "1.0");
                writer.writeStartElement("citizens");
                list.forEach(el -> writeCitizen(el, writer));
                writer.writeEndElement();
                writer.flush();
                writer.close();
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    private void writeCitizen(CitizenAndAddress citizenAndAddress, XMLStreamWriter writer) {
        try {
            writer.writeStartElement(ADDRESS_TAG);
            writer.writeStartElement(NAME_TAG);
            writer.writeCharacters(citizenAndAddress.getName());
            writer.writeEndElement();
            writer.writeStartElement(SURNAME_TAG);
            writer.writeCharacters(citizenAndAddress.getSurname());
            writer.writeEndElement();
            writer.writeStartElement(CITY_TAG);
            writer.writeCharacters(citizenAndAddress.getCity());
            writer.writeEndElement();
            writer.writeStartElement(STREET_TAG);
            writer.writeCharacters(citizenAndAddress.getStreet());
            writer.writeEndElement();
            writer.writeStartElement(BUILDING_TAG);
            writer.writeCharacters(String.valueOf(citizenAndAddress.getBuilding()));
            writer.writeEndElement();
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException {
        openReader();
        List<CitizenAndAddress> list = getList();
        closeReader();
        try {
            XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(new FileWriter(PATH));
            writer.writeStartDocument("windows-1251", "1.0");
            writer.writeStartElement("citizens");
            list.stream().filter(el -> !el.equals(citizenAndAddress)).forEach(el -> writeCitizen(el, writer));
            writer.writeEndElement();
            writer.flush();
            writer.close();
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }
}
