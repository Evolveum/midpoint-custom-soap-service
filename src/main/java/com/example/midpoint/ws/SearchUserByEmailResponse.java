package com.example.midpoint.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.List;

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
