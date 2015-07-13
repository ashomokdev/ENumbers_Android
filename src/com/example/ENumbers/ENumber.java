package com.example.ENumbers;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by y.belyaeva on 25.06.2015.
 */
@Root(name = "eNumber")
public class ENumber {
    @Element(name = "code", required = false)
    private String _code;
    @Element(name = "name", required = false)
    private String _name;
    @Element(name = "purpose", required = false)
    private String _purpose;
    @Element(name = "status", required = false)
    private String _status;




    public String get_code() {
        return _code;
    }

    public String get_name() {
        return _name;
    }

    public String get_purpose() {
        return _purpose;
    }

    public String get_status() {
        return _status;
    }

    @Override
    public String toString() {
        return "\ncode: " + _code + "\nname: " + _name + "\npurpose: " + _purpose + "\nstatus: " + _status;
    }
}
