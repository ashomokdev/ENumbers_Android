/**
 * Created by y.belyaeva on 28.05.2015.
 */

package com.example.ENumbersData;
public class ENumber {
    private String code = "";
    private String name = "";
    private String purpose = "";
    private String status = "";
    private String comment = "";

    public ENumber(String code, String name, String purpose, String status){
        this.code = code;
        this.name = name;
        this.purpose = purpose;
        this.status = status;
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

    public String getComment() {return comment;}

    public void setComment(String _comment) {
        this.comment += _comment;
    }


}
