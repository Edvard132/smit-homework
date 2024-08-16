package com.smit.tire_change_app.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactInformation {
    @JacksonXmlProperty(localName = "contactInformation")
    private String contactInformation;

}
