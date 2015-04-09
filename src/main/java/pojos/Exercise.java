package pojos;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "exercise")
@XmlAccessorType(XmlAccessType.FIELD)
public class Exercise {
    @XmlElement(name = "number")
    private String number;
    @XmlElement(name = "text")
    private String text;
    @XmlElement(name = "result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

