package datasource;

import domain.Seat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Simon
 */
public class Reservation implements MapperIf {

    private ArrayList<Seat> seats = new ArrayList();
    private ConnectionIf conManager;
    private Connection conn;

    public Reservation(String user, String password) {
        conManager = new DbConnection();
        conn = conManager.getConnection(user, password);
    }

    private boolean loadSeats() {
        //String selectSeat = "SELECT * FROM seat WHERE booked IS NULL";

        String selectSeat = "SELECT * FROM (SELECT * FROM seat where (booked IS NULL AND booking_time < ?) OR reserved IS NULL ORDER BY dbms_random.value) Where ROWNUM = 1";
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement(selectSeat);
            statement.setLong(1, System.currentTimeMillis());
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                seats.add(new Seat(res.getString(1), res.getString(2), res.getLong(3), res.getLong(4), res.getLong(5)));
            }
        } catch (SQLException e) {
            System.out.println("book: " + e);
        }
        return !seats.isEmpty();
    }

    private Seat selectSeat() {
        Seat selectedSeat = null;
        for (Seat seat : seats) {
            if (seat.getReserved() == 0) {
                selectedSeat = seat;
                break;
            } else if (System.currentTimeMillis() - seat.getBookingTime() > 0) {
                selectedSeat = seat;
                break;
            }
        }
        return selectedSeat;
    }

    @Override
    public Seat reserve(String planeNo, long id) {
        String reserveSeat = "UPDATE seat SET reserved = ?, booking_time = ? WHERE seat_no = ?";
        int updated = 0;
        Seat s = null;
        PreparedStatement statement;

        if (loadSeats()) {
            s = seats.get(0);

            if (s == null) {
                return null;
            }

            long time = System.currentTimeMillis() + 5000;

            try {
                statement = conn.prepareStatement(reserveSeat);
                statement.setLong(1, id);
                statement.setLong(2, time);
                statement.setString(3, s.getSeatNo());
                updated = statement.executeUpdate();
                //System.out.println(updated);
            } catch (SQLException e) {
                System.out.println("res: " + e);
                conManager.releaseConnection();
                return null;
            }

            if (updated != 0) {
                s.setReserved(id);
                s.setBookingTime(time);
                return s;
            }
        }
        return null;
    }

    public Seat getReservedSeat(String seatNo) {
        String selectSeat = "SELECT * FROM seat WHERE seat_no = ?";
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement(selectSeat);
            statement.setString(1, seatNo);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return new Seat(res.getString(1), res.getString(2), res.getLong(3), res.getLong(4), res.getLong(5));
            }
        } catch (SQLException e) {
            System.out.println("get Reserved seat: " + e);
        }
        return null;
    }

    @Override
    public int book(String planeNo, String seatNo, long id) {
        String bookSeatSql = "UPDATE seat SET booked = ?, booking_time = ? WHERE seat_no = ?";
        PreparedStatement statement;
        int rows = 0;
        Seat seat = getReservedSeat(seatNo);

        if (seat == null) {
            return -5;
        }

        if (seat.getBooked() != 0) {
            return -4;
        }

        if (seat.getBookingTime() < System.currentTimeMillis()) {
            return -3;
        }

        if (seat.getReserved() != id && seat.getReserved() != 0) {
            return -2;
        }

        if (seat.getReserved() == 0) {
            return -1;
        }

        try {
            statement = conn.prepareStatement(bookSeatSql);
            statement.setLong(1, id);
            statement.setLong(2, System.currentTimeMillis());
            statement.setString(3, seatNo);
            rows = statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            conManager.releaseConnection();
            return -5;
        }
        if (rows > 0) {
            return 0;
        } else {
            return -5;
        }
    }

    @Override
    public void bookAll(String planeNo) {
        /*  
        String reserveSeat = "UPDATE seat SET booked = ?, booking_time = ? WHERE plane_no = 'CR9'";
        PreparedStatement statement;

        try {
            statement = conn.prepareStatement(reserveSeat);
            statement.setLong(1, 1);
            statement.setLong(2, System.currentTimeMillis());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            conManager.releaseConnection();
        }
        conManager.releaseConnection();
         */
    }

    @Override
    public void clearAllBookings(String planeNo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAllBooked(String planeNo) {
        String selectSeat = "SELECT * FROM seat WHERE booked IS NULL";
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement(selectSeat);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("isAllBooked errored");
        }
        return true;
    }

    @Override
    public boolean isAllReserved(String planeNo) {
        String selectSeat = "SELECT * FROM seat WHERE reserved IS NULL";
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement(selectSeat);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("isAllReserved errored");
        }
        return true;
    }

    @Override
    public void end() {
        conManager.releaseConnection();
    }

}
