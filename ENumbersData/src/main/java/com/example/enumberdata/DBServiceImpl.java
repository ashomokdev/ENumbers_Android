package com.example.enumberdata;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Iuliia on 10.09.2015.
 */
public class DBServiceImpl implements DBService {

    private Dao<ENumber, String> eNumbersDao;
    private ConnectionSource connectionSource;

    @Override
    public void createDB() {
        try {
            String databaseUrl = "jdbc:sqlite:C:\\Users\\Iuliia\\IdeaProjects\\ENumbers_Android_Java_Maven\\assets\\databases\\Enumbers.db";
            // create a connection source to our database
            connectionSource = new JdbcConnectionSource(databaseUrl);

            eNumbersDao = DaoManager.createDao(connectionSource, ENumber.class);

            TableUtils.createTableIfNotExists(connectionSource, ENumber.class);

            TableUtils.clearTable(connectionSource, ENumber.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(ENumber eNumber) {
        try {
            // persist the eNumber object to the database
            eNumbersDao.create(eNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ENumber selectRow(String code) {
        try {
            // retrieve the eNumber

            ENumber eNumber = eNumbersDao.queryForId(code);
            return eNumber;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void closeConnection() {
        try {
            // close the connection source
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

