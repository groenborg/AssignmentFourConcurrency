/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import datasource.Reservation;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon
 */
public class Controller {

    private int numberOfThreads = 0;
    private int unsuccesfullBookings = 0;
    private int successfull = 0;
    private int minusThree = 0;
    private int minusTwo = 0;
    private int minusOne = 0;

    public synchronized void incrementThread() {
        this.numberOfThreads++;
    }

    public synchronized void incrementFailedBookings() {
        this.unsuccesfullBookings++;
    }

    public synchronized void incrementSuccessfull() {
        this.successfull++;
    }

    public synchronized void incrementMinusThree() {
        this.minusThree++;
    }

    public synchronized void incrementMinusTwo() {
        this.minusTwo++;
    }

    public synchronized void incrementMinusOne() {
        this.minusOne++;
    }

    public void simulate() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        boolean running = true;

        for (int i = 0; i < 600; i++) {
            executor.submit(new User(i + 100, this));
        }
        executor.shutdown();

        Reservation v = new Reservation("cphre31", "krumme24");
        while (running) {

            boolean isBooked = v.isAllBooked("CR9");
            if (isBooked) {
                running = false;
                executor.shutdownNow();
                v.end();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("is all booked: " + isBooked);
        }

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            print();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

    }

    public void print() {
        System.out.println("-1: " + minusOne + " reserved is still null (!)");
        System.out.println("-2: " + minusTwo + " another has the seat reserved");
        System.out.println("-3: " + minusThree + " timeout");
        System.out.println(" 0: " + successfull + " success");
    }

    public static void main(String[] args) {
        Controller t = new Controller();
        t.simulate();
    }

}
