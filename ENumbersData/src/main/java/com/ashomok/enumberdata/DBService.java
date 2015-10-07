package com.ashomok.enumberdata;

/**
 * Created by Iuliia on 10.09.2015.
 */
public interface DBService {
    void createDB();

    void insert(ENumber eNumber);

    ENumber selectRow(String code);

    void closeConnection();
}
