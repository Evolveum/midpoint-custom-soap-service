
package com.example.midpoint.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchUserByEmailRequestType",
        propOrder = {
                "email"
        })
public class SearchUserByEmailRequest {

    @XmlElement(required = true)
    public String email;

    @Override
    public String toString() {
        return "SearchUserByEmailRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
