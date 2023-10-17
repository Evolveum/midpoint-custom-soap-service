/*
 * Copyright (C) 2010-2020 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.midpoint.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * We have to use custom types <b>distinct from built-in midPoint schema types</b>.
 * The classes used by midPoint look like JAXB but are not compliant and can't be used directly
 * in code-first approach.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomUserType",
        propOrder = {
                "oid",
                "username",
                "fullname",
                "email"
        })
public class CustomUser {

    @XmlElement
    public String oid;

    @XmlElement
    public String username;

    @XmlElement
    public String fullname;

    @XmlElement
    public String email;

    @Override
    public String toString() {
        return "CustomUser{" +
                "oid='" + oid + '\'' +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
