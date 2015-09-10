package com.example.enumberdata;

import org.testng.TestNG;
import org.testng.annotations.Test;

/**
 * Created by Iuliia on 08.08.2015.
 */
@Test
public class DBServiceTest extends TestNG {

    private DBService dbService;

    @org.testng.annotations.BeforeClass
    public void init() {
        dbService = new DBServiceImpl();
        dbService.createDB();
    }

    @Test
    public void testInsert() {
        ENumber data = new ENumber("code2","name","purpose","status");
        dbService.insert(data);
        ENumber gettedData = dbService.selectRow("code2");
        assert(gettedData.getName().equals("name"));
    }




}
