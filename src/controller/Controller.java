/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Simon
 */
public class Controller {

    private int numberOfThreads = 0;
    private int unsuccesfullBookings = 0;

    public synchronized void incrementThread() {
        this.numberOfThreads++;
    }

    public synchronized void incrementFailedBookings() {
        this.unsuccesfullBookings++;
    }

    public void simulate() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(new User(i + 100));
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        Controller t = new Controller();
        t.simulate();
    }

}
