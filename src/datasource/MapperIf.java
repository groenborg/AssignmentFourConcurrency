package datasource;

import domain.Seat;

/**
 *
 * @author Simon & Robert
 */
public interface MapperIf {

    public Seat reserve(String planeNo, long id);

    public int book(String planeNo, String seatNo, long id);

    public void bookAll(String planeNo);

    public void clearAllBookings(String planeNo);

    public boolean isAllBooked(String planeNo);

    public boolean isAllReserved(String planeNo);

    public void end();
    
}
