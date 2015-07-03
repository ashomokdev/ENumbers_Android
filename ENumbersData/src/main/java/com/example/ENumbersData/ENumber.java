/**
 * Created by y.belyaeva on 28.05.2015.
 */

package com.example.ENumbersData;
public class ENumber {
    private String _code = "";
    private String _name = "";
    private String _purpose = "";
    private String _status = "";

    public ENumber(String code, String name, String purpose, String status){
        _code = code;
        _name = name;
        _purpose = purpose;
        _status = status;
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
