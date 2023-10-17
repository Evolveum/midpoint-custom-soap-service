
package com.example.midpoint.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

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
