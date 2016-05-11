package domain;

/**
 *
 * @author Simon & Robert
 */
public class Seat {

    private final String planeNo;
    private final String seatNo;
    private long reserved;
    private long booked;
    private long bookingTime;

    public Seat(String planeNo, String seatNo, long reserved, long booked, long bookingTime) {
        this.planeNo = planeNo;
        this.seatNo = seatNo;
        this.reserved = reserved;
        this.booked = booked;
        this.bookingTime = bookingTime;
    }

    public String getPlaneNo() {
        return planeNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public long getReserved() {
        return reserved;
    }

    public long getBooked() {
        return booked;
    }

    public long getBookingTime() {
        return bookingTime;
    }

    public void setReserved(long reserved) {
        this.reserved = reserved;
    }

    public void setBooked(long booked) {
        this.booked = booked;
    }

    public void setBookingTime(long bookingTime) {
        this.bookingTime = bookingTime;
    }

    @Override
    public String toString() {
        return "Seat{" + "planeNo=" + planeNo + ", seatNo=" + seatNo
                + ", reserved=" + reserved + ", booked=" + booked
                + ", bookingTime=" + bookingTime + '}';
    }
}
