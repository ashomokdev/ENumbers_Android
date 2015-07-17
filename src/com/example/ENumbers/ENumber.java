package com.example.ENumbers;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by y.belyaeva on 25.06.2015.
 */
@Root(name = "eNumber")
public class ENumber {
    @Element(name = "code", required = false)
    private String code;
    @Element(name = "name", required = false)
    private String name;
    @Element(name = "purpose", required = false)
    private String purpose;
    @Element(name = "status", required = false)
    private String status;
    @Element(name = "comment", required = false)
    private String comment;


    public String getComment() {
        return comment;
    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "\ncode: " + code + "\nname: " + name + "\npurpose: " + purpose + "\nstatus: " + status;
    }
}
