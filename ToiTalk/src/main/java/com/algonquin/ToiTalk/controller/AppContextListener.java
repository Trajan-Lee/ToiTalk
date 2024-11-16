package com.algonquin.ToiTalk.controller;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Called when the application is starting up
        System.out.println("Application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Called when the application is shutting down
        System.out.println("Application is shutting down");

        try {
            // Unregister any JDBC drivers to avoid memory leaks
            Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                java.sql.Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
                System.out.println("Deregistered JDBC driver: " + driver);
            }

            // Stop the MySQL abandoned connection cleanup thread
            AbandonedConnectionCleanupThread.checkedShutdown();
            System.out.println("Stopped MySQL abandoned connection cleanup thread.");

        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}