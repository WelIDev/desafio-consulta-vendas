package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT obj FROM Sale obj JOIN FETCH obj.seller " +
            "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :sellerName, '%')) " +
            "AND obj.date BETWEEN :initialDate AND :finalDate",
            countQuery = "SELECT COUNT(obj) FROM Sale obj " +
                    "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :sellerName, '%')) " +
                    "AND obj.date BETWEEN :initialDate AND :finalDate")
    Page<Sale> searchSalesForDate(Pageable pageable, LocalDate initialDate, LocalDate finalDate, String sellerName);

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name, SUM(obj.amount)) " +
            "FROM Sale obj JOIN obj.seller " +
            "WHERE obj.date BETWEEN :initialDate AND :finalDate " +
            "GROUP BY obj.seller.name")
    List<SaleSummaryDTO> getSalesSummaryByDate(LocalDate initialDate, LocalDate finalDate);
}
