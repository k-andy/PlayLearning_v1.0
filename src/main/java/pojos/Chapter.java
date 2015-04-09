package pojos;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "chapter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Chapter {
    @XmlElement(name = "number")
    private String number;
    @XmlElement(name = "name")
    private String name;
    @XmlElementWrapper(name="subChapters")
    @XmlElement(name = "subChapter")
    private List<SubChapter> subChapters;

    public String getNumber() {
        return number;
    }

    public void setNumber(String chpterNumber) {
        this.number = chpterNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubChapter> getSubChapters() {
        return subChapters;
    }

    public void setSubChapters(List<SubChapter> subChapters) {
        this.subChapters = subChapters;
    }
}
