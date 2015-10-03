package com.example.enumberdata;

import java.util.ArrayList;

/**
 * Created by Iuliia on 02.10.2015.
 */
public class XlsService {

    public void fillSQLiteDB(ArrayList<ENumber> data) {

        DBService dbService = new DBServiceImpl();
        dbService.createDB();
        for (ENumber item : data) {
            dbService.insert(item);
        }
        dbService.closeConnection();
    }
}
