package com.example.etas.services;

import com.example.etas.models.Cab;
import com.example.etas.repositories.CabRepository;
import io.reactivex.Single;

import java.util.List;

public class CabService {

  private final CabRepository cabRepository;

  public CabService(CabRepository cabRepository) {
    this.cabRepository = cabRepository;
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
