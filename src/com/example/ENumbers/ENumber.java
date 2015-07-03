package com.example.ENumbers;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by y.belyaeva on 25.06.2015.
 */
@Root(name = "eNumber")
public class ENumber {
    @Element(name = "code")
    private String _code = "";
    @Element(name = "name")
    private String _name = "";
    @Element(name = "purpose")
    private String _purpose = "";
    @Element(name = "status")
    private String _status = "";

    public ENumber(String code, String name, String purpose, String status){
        _code = code;
        _name = name;
        _purpose = purpose;
        _status = status;
    }

    @Override
    public String toString()
    {
        return "eNumber{" + "code=" + _code + ", name=" + _name + ", purpose=" + _purpose + ", status=" + _status +'}';
    }

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
}
