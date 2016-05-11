package controller;

import datasource.Reservation;
import domain.Seat;
import datasource.MapperIf;

/**
 *
 * @author Simon
 */
public class User implements Runnable {

    private final long id;

    public User(long id) {
        this.id = id;
    }

    public void act() {
        StringBuilder sb = new StringBuilder();

        MapperIf res = new Reservation("db_033", "123");
        Seat s = res.reserve("CR9", id);
        sb.append("user ").append(id);
        if (s != null) {
            sb.append(" reserved ").append(s.getSeatNo());

            try {
                long time = (long) (Math.random() * 10000);
                Thread.sleep(time);
                sb.append(" slept ").append(time);

            } catch (InterruptedException ex) {
            }

            double chance = Math.random() * 100;
            sb.append(" chance ").append(chance);
            if (chance > 25) {
                int status = res.book("CR9", s.getSeatNo(), id);
                sb.append(" booking status: ").append(status);
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
        System.out.println(id + " Thread started");
        act();
    }

}
