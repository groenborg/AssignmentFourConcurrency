package controller;

import datasource.Reservation;
import domain.Seat;
import datasource.MapperIf;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon
 */
public class User implements Runnable {

    private final long id;
    private Controller control;

    public User(long id, Controller control) {
        this.control = control;
        this.id = id;
    }

    public void act() {
        StringBuilder sb = new StringBuilder();

        MapperIf res = new Reservation("cphre31", "krumme24");

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {

        }
        Seat s = res.reserve("CR9", id);
        sb.append("user ").append(id);
        if (s != null) {
            sb.append(" reserved ").append(s.getSeatNo());

            try {
                long time = (long) (Math.random() * 8000);
                Thread.sleep(time);
                sb.append(" slept ").append(time);

            } catch (InterruptedException ex) {
                // System.out.println("User Interrupted by: " + ex);
            }

            double chance = Math.random() * 100;
            sb.append(" chance ").append(chance);
            if (chance > 15) {
                int status = res.book("CR9", s.getSeatNo(), id);
                sb.append(" booking status: ").append(status);

                switch (status) {
                    case -1:
                        control.incrementMinusOne();
                        break;
                    case -2:
                        control.incrementMinusTwo();
                        break;
                    case -3:
                        control.incrementMinusThree();
                        break;
                    case 0:
                        control.incrementSuccessfull();
                        break;
                }
            } else {
                sb.append(" decided not to book");
            }

        } else {
            sb.append(" no available reservations ");
        }
        System.out.println(sb.toString());
        res.end();
    }

    @Override
    public void run() {
        act();
    }

}
