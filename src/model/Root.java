package model;


import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "root")
public class Root implements Serializable {
    private static final long serialVersionUID = 1L;

    private Header header;
    private List<Row> body;

    @XmlElement(name = "header")
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlElementWrapper(name = "body")
    @XmlElement(name = "row")
    public List<Row> getBody() {
        return body;
    }

    public void setBody(List<Row> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Root {\n");
        sb.append("  Header: ").append(header != null ? header.toString() : "null").append("\n");
        sb.append("  Body: \n");
        if (body != null && !body.isEmpty()) {
            for (Row row : body) {
                sb.append("    ").append(row != null ? row.toString() : "null").append("\n");
            }
        } else {
            sb.append("    null\n");
        }
        sb.append("}");
        return sb.toString();
    }
}




