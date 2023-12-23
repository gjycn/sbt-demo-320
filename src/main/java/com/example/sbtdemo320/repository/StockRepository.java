package com.example.sbtdemo320.repository;

import com.example.sbtdemo320.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long>, PagingAndSortingRepository<Stock,Long> {

}
