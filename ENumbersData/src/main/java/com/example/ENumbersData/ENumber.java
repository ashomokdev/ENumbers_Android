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

    public ENumber(String code, String name, String purpose, String status) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String _comment) {
        if (_comment.equals(""))
        {
            return;
        }

        if (_comment.endsWith(". ")) {
            //ok
        } else if (_comment.endsWith(".")) {
            _comment += " ";
        } else {
            _comment += ". ";
        }

        if (Character.isUpperCase(_comment.charAt(0))) {
            //ok
        } else {
            //to capitalize the first letter of word in a string
            _comment = _comment.substring(0, 1).toUpperCase() + _comment.substring(1);
        }
        this.comment += _comment;
    }
}
