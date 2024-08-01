package com.example.etas.repositories;

import com.example.etas.models.Cab;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.ext.jdbc.JDBCClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CabRepository {

  private final JDBCClient jdbcClient;
  Random random = new Random();

  public CabRepository(JDBCClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Single<List<Cab>> findAll() {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQuery("SELECT * FROM cabs").map(rs -> {
        List<Cab> cabs = new ArrayList<>();
        rs.getRows().forEach(json -> {
          Cab cab = json.mapTo(Cab.class);
          cabs.add(cab);
        });
        return cabs;
      }).doFinally(conn::close)
    );
  }

  public Single<Cab> findById(Long cabId) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQueryWithParams("SELECT * FROM cabs WHERE cab_id = ?", new JsonArray().add(cabId)).map(rs -> {
        if (rs.getNumRows() == 0) {
          return null;
        } else {
          return rs.getRows().get(0).mapTo(Cab.class);
        }
      }).doFinally(conn::close)
    );
  }

  public Single<Cab> save(Cab cab) {
    cab.setCabId(random.nextLong());
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("INSERT INTO cabs (cab_id, registration_number, driver_id, cab_status, comments, vacancy) VALUES (?, ?, ?, ?, ?, ?)",
        new JsonArray().add(cab.getCabId()).add(cab.getRegistrationNumber()).add(cab.getDriverId()).add(cab.getCabStatus())
          .add(cab.getComments()).add(cab.getVacancy())).map(updateResult -> cab
      ).doFinally(conn::close)
    );
  }

  public Single<Cab> update(Cab cab) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("UPDATE cabs SET registration_number = ?, driver_id = ?, cab_status = ?, comments = ?, vacancy = ? WHERE cab_id = ?",
        new JsonArray().add(cab.getRegistrationNumber()).add(cab.getDriverId()).add(cab.getCabStatus())
          .add(cab.getComments()).add(cab.getVacancy()).add(cab.getCabId())).flatMap(updateResult ->
        findById(cab.getCabId())
      ).doFinally(conn::close)
    );
  }

  public Single<Boolean> delete(Long cabId) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("DELETE FROM cabs WHERE cab_id = ?", new JsonArray().add(cabId)).map(updateResult ->
        updateResult.getUpdated() > 0
      ).doFinally(conn::close)
    );
  }

  public Single<Boolean> setCabStatus(Long cabId, String status) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("UPDATE cabs SET cab_status = ? WHERE cab_id = ?", new JsonArray().add(status).add(cabId)).map(updateResult ->
        updateResult.getUpdated() > 0
      ).doFinally(conn::close)
    );
  }

  public Single<List<Cab>> findAvailableCabs() {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQuery("SELECT * FROM cabs WHERE cab_status = 'AVAILABLE' AND vacancy > 0").map(rs -> {
        List<Cab> availableCabs = new ArrayList<>();
        rs.getRows().forEach(json -> {
          Cab cab = json.mapTo(Cab.class);
          availableCabs.add(cab);
        });
        return availableCabs;
      }).doFinally(conn::close)
    );
  }

}
