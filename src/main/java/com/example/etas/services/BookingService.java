package com.example.etas.services;

import com.example.etas.HttpServerVerticle;
import com.example.etas.models.Booking;
import com.example.etas.models.Cab;
import com.example.etas.repositories.BookingRepository;
import com.example.etas.repositories.CabRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Singleton
public class BookingService {

  private final BookingRepository bookingRepository;
  private final CabRepository cabRepository;
  private Random random = new Random();

  private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

  @Inject
  public BookingService(BookingRepository bookingRepository, CabRepository cabRepository) {
    this.bookingRepository = bookingRepository;
    this.cabRepository = cabRepository;
    logger.debug("BookingService Created");
  }

  public Single<List<Booking>> getAllBookings() {
    return bookingRepository.findAll();
  }

  public Single<Booking> getBookingById(Long bookingId) {
    return bookingRepository.findById(bookingId);
  }

  public Single<Boolean> deleteBooking(Long bookingId) {
    return bookingRepository.delete(bookingId);
  }

  public Single<Long> requestCab(String sourceLocation, String dateTimeOfJourney, Long employeeId) {
    // Validate request time
    LocalDateTime journeyTime = LocalDateTime.parse(dateTimeOfJourney, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    LocalDateTime now = LocalDateTime.now();
    if (journeyTime.isBefore(now.plusHours(12)) || journeyTime.isAfter(now.plusDays(2))) {
      return Single.error(new RuntimeException("REQUEST NOT POSSIBLE: Request needs to be raised at least 12 hours in advance and not before 2 days of the journey."));
    }

    // Check for valid travel time (between 10pm and 1am)
    int hour = journeyTime.getHour();
    if (hour < 22 && hour >= 1) {
      return Single.error(new RuntimeException("INVALID TRIP TIME: Travel time must be between 10pm and 1am."));
    }

    // Check for cab availability
    return cabRepository.findAvailableCabs()
      .flatMap(cabs -> {
        if (cabs.isEmpty()) {
          return Single.error(new RuntimeException("CAB NOT AVAILABLE: No cab available during the required time slot."));
        }

        // Select the first available cab
        Cab selectedCab = cabs.get(0);


        Booking booking = new Booking();
        booking.setBookingId(random.nextLong());
        booking.setBookingStatus("GENERATED");
        booking.setRequestId(random.nextLong());
        booking.setSourceLocation(sourceLocation);
        booking.setDateTimeOfJourney(dateTimeOfJourney);
        booking.setRequestCreationDate(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        booking.setRequestGenerator(employeeId);
        booking.setEmployeeId(employeeId);
        booking.setRequestStatus("GENERATED");
        booking.setComments("Cab requested");
        booking.setCabId(selectedCab.getCabId());
        booking.setDriverId(selectedCab.getDriverId());

        return bookingRepository.save(booking).map(savedBooking -> savedBooking.getRequestId());
      });
  }

  public Single<Booking> getRequestStatus(Long requestId) {
    return bookingRepository.findByRequestId(requestId)
      .map(booking -> {
        if (booking == null) {
          throw new RuntimeException("REQUEST NOT FOUND");
        }
        return booking;
      });
  }

}
