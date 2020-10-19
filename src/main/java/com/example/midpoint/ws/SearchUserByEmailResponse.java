package com.example.midpoint.ws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchUserByEmailResponseType",
        propOrder = {
                "users"
        })
public class SearchUserByEmailResponse {

    /**
     * Possibly multiple <b>user</b> elements without any wrapper.
     */
    @XmlElement(name = "user", required = true)
    protected List<CustomUser> users;

    @Override
    public String toString() {
        return "SearchUserByEmailResponse{" +
                "users=" + users +
                '}';
    }
}
