package com.example.etas.services;

import com.example.etas.HttpServerVerticle;
import com.example.etas.models.Cab;
import com.example.etas.repositories.CabRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class CabService {

  private final CabRepository cabRepository;
  private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

  @Inject
  public CabService(CabRepository cabRepository) {
    this.cabRepository = cabRepository;
    logger.debug("CabService Created");
  }

  public Single<List<Cab>> getAllCabs() {
    return cabRepository.findAll();
  }

  public Single<Cab> getCabById(Long cabId) {
    return cabRepository.findById(cabId);
  }

  public Single<Cab> addCab(Cab cab) {
    return cabRepository.save(cab);
  }

  public Single<Cab> updateCab(Cab cab) {
    return cabRepository.update(cab);
  }

  public Single<Boolean> deleteCab(Long cabId) {
    return cabRepository.delete(cabId);
  }

  public Single<Boolean> setCabStatus(Long cabId, String status) {
    return cabRepository.setCabStatus(cabId, status);
  }

}
