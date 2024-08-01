package com.example.etas.repositories;

import com.example.etas.models.Booking;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.ext.jdbc.JDBCClient;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

  private final JDBCClient jdbcClient;

  public BookingRepository(JDBCClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Single<List<Booking>> findAll() {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQuery("SELECT * FROM bookings").map(rs -> {
        List<Booking> bookings = new ArrayList<>();
        rs.getRows().forEach(json -> {
          Booking booking = json.mapTo(Booking.class);
          bookings.add(booking);
        });
        return bookings;
      }).doFinally(conn::close)
    );
  }

  public Single<Booking> findById(Long bookingId) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQueryWithParams("SELECT * FROM bookings WHERE booking_id = ?", new JsonArray().add(bookingId)).map(rs -> {
        if (rs.getNumRows() == 0) {
          return null;
        } else {
          return rs.getRows().get(0).mapTo(Booking.class);
        }
      }).doFinally(conn::close)
    );
  }

  public Single<Booking> save(Booking booking) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("INSERT INTO bookings (booking_id, request_id, driver_id, cab_id, source_location, date_time_of_journey, request_creation_date, request_generator, employee_id, request_status, booking_status, comments) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        new JsonArray().add(booking.getBookingId()).add(booking.getRequestId()).add(booking.getDriverId()).add(booking.getCabId())
          .add(booking.getSourceLocation()).add(booking.getDateTimeOfJourney()).add(booking.getRequestCreationDate())
          .add(booking.getRequestGenerator()).add(booking.getEmployeeId()).add(booking.getRequestStatus())
          .add(booking.getBookingStatus()).add(booking.getComments())).map(updateResult -> booking
      ).doFinally(conn::close)
    );
  }

  public Single<Boolean> delete(Long bookingId) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("DELETE FROM bookings WHERE booking_id = ?", new JsonArray().add(bookingId)).map(updateResult ->
        updateResult.getUpdated() > 0
      ).doFinally(conn::close)
    );
  }

  public Single<Booking> findByRequestId(Long requestId) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQueryWithParams("SELECT * FROM bookings WHERE request_id = ?", new JsonArray().add(requestId)).map(rs -> {
        if (rs.getNumRows() == 0) {
          return null;
        } else {
          return rs.getRows().get(0).mapTo(Booking.class);
        }
      }).doFinally(conn::close)
    );
  }

}
