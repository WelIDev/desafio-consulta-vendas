package com.devsuperior.dsmeta.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> searchSalesForDate(Pageable pageable, String minDate, String maxDate, String name) {
		LocalDate initialDate = LocalDate.now().minusYears(1);
		LocalDate finalDate = LocalDate.now();

		if (!minDate.isEmpty()){
			initialDate = LocalDate.parse(minDate);
		}
		if (!maxDate.isEmpty()){
			finalDate = LocalDate.parse(maxDate);
		}

		Page<Sale> sales = repository.searchSalesForDate(pageable, initialDate, finalDate, name);
        return sales.map(SaleReportDTO::new);
	}

	public List<SaleSummaryDTO> searchSalesOfSeller(String minDate, String maxDate) {
		LocalDate initialDate = LocalDate.now().minusYears(1);
		LocalDate finalDate = LocalDate.now();

		if (!minDate.isEmpty()){
			initialDate = LocalDate.parse(minDate);
		}
		if (!maxDate.isEmpty()){
			finalDate = LocalDate.parse(maxDate);
		}

        return repository.getSalesSummaryByDate(initialDate, finalDate);
	}
}
