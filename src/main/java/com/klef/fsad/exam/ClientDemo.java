package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.Scanner;

public class ClientDemo {

    public static void main(String[] args) {
        // Initialize Hibernate SessionFactory
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("\n--- Inventory Operations ---");
            System.out.println("1. Insert a new record");
            System.out.println("2. Delete a record based on ID");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (choice == 1) {
                insertRecord(sessionFactory, scanner);
            } else if (choice == 2) {
                System.out.print("Enter Inventory ID to delete: ");
                Long id = scanner.nextLong();
                deleteRecord(sessionFactory, id);
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
        
        scanner.close();
        sessionFactory.close();
        System.out.println("Session closed. Exiting.");
    }

    public static void insertRecord(SessionFactory sessionFactory, Scanner scanner) {
        System.out.print("Enter Inventory Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Inventory Description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter Inventory Status: ");
        String status = scanner.nextLine();

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            Inventory inventory = new Inventory();
            inventory.setName(name);
            inventory.setDescription(description);
            inventory.setDate(new Date());
            inventory.setStatus(status);
            
            session.save(inventory);
            
            transaction.commit();
            System.out.println("Record inserted successfully with automatically generated ID: " + inventory.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void deleteRecord(SessionFactory sessionFactory, Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            Inventory inventory = session.get(Inventory.class, id);
            if (inventory != null) {
                session.delete(inventory);
                System.out.println("Record with ID " + id + " deleted successfully.");
            } else {
                System.out.println("Record with ID " + id + " not found.");
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
