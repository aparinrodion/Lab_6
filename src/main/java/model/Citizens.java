package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "citizens")
@XmlAccessorType(XmlAccessType.FIELD)
public class Citizens {
    @XmlElement(name = "citizen")
    private List<CitizenAndAddress> citizens;

    public Citizens() {
        citizens = new ArrayList<>();
    }

    public List<CitizenAndAddress> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<CitizenAndAddress> citizens) {
        this.citizens = citizens;
    }
}
