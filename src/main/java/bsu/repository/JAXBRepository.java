package bsu.repository;

import bsu.RMI.RMIInterface;
import bsu.model.CitizenAndAddress;
import bsu.model.Citizens;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JAXBRepository implements RMIInterface {
    private static final String PATH = "src/main/resources/jaxb_address.xml";
    private static final String SCHEMA_PATH = "src/main/resources/schema.xsd";
    private Unmarshaller reader;
    private Marshaller writer;
    private final File file = new File(PATH);

    public JAXBRepository() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Citizens.class);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(SCHEMA_PATH));
            reader = jaxbContext.createUnmarshaller();
            reader.setSchema(schema);
            writer = jaxbContext.createMarshaller();
            writer.setProperty(Marshaller.JAXB_ENCODING, "windows-1251");
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CitizenAndAddress> getList() throws RemoteException {
        try {
            Citizens citizens = (Citizens) reader.unmarshal(file);
            return citizens.getCitizens();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, Integer> getCitiesAndPopulation() throws RemoteException {
        return CitizensUtil.getCitiesAndPopulationFromList(this.getList());
    }

    @Override
    public int getNumber() throws RemoteException {
        return getList().size();
    }

    @Override
    public void addCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException {
        List<CitizenAndAddress> citizenAndAddresses = this.getList();
        citizenAndAddresses.add(citizenAndAddress);
        Citizens citizens = new Citizens();
        citizens.setCitizens(citizenAndAddresses);
        try {
            writer.marshal(citizens, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException {
        List<CitizenAndAddress> citizenAndAddresses = this.getList();
        citizenAndAddresses.remove(citizenAndAddress);
        Citizens citizens = new Citizens();
        citizens.setCitizens(citizenAndAddresses);
        try {
            writer.marshal(citizens, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
