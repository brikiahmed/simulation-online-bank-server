package examen.test.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;
import examen.test.entities.Request;
import examen.test.repositories.RequestRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;


@Service
public class RequestServices implements IRequestServices {
	
	@Autowired
	RequestRepository requestRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Request CreateRequest(Request request) {
		
		return requestRepository.save(request);
	}

	@Override
	public Request UpdateRequest(Request newRequest, Long id) {
		Request p = requestRepository.findById(id).orElse(null) ;
		
		if( p!= null) {
			
			//RequestRepository.delete(p);
			p.setAddress(newRequest.getAddress());
			p.setCin(newRequest.getCin());
			p.setEmail(newRequest.getEmail());
			p.setTypeRequest(newRequest.getTypeRequest());
			p.setFirstName(newRequest.getFirstName());
			p.setLastName(newRequest.getLastName());
			p.setBirthDate(newRequest.getBirthDate());
			p.setReference(newRequest.getReference());
			p.setCin(newRequest.getCin());
			p.setStatus(newRequest.getStatus());
			p.setArchived(newRequest.getArchived());
			p.setReasonOfRefuse(newRequest.getReasonOfRefuse());
			p.setUpdatedDate(String.valueOf(LocalDateTime.now()));
			p.setNextService(newRequest.getNextService());
			p.setComplaintRequestAllowed(newRequest.getComplaintRequestAllowed());
			requestRepository.save(p);
			requestRepository.flush();
		}
		
		return p ;
	}

	public List<Request> GetAllRequest() {
		
		return requestRepository.findByArchivedOrArchivedIsNull(false);
		}

	@Override
	public Request getRequestbyId(Long id) {
		
		return requestRepository.findById(id).orElse(null) ;
	}

	public List<Request> GetAllArchivedRequest() {
		return requestRepository.getByArchived(true);
	}

	@Override
	public void RemoveRequest(Long id) {
		requestRepository.deleteById(id);
	}

	public boolean isDuplicateRequest(String email, String name, String typeRequest) {
		// Perform a query to check for duplicate requests
		long count = requestRepository.countByEmailAndFirstNameAndTypeRequest(email, name, typeRequest);
		return count > 0;
	}

	public byte[] generateExcelFile(List<Request> requests) {
		// Create a new workbook
		Workbook workbook = new XSSFWorkbook();

		// Create a new sheet
		Sheet sheet = workbook.createSheet("Requests");

		// Create header row
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("demande ID");
		headerRow.createCell(1).setCellValue("Type de demande");
		headerRow.createCell(2).setCellValue("Date de création");
		headerRow.createCell(3).setCellValue("Nom de client");
		headerRow.createCell(4).setCellValue("Prénom de client");
		headerRow.createCell(5).setCellValue("Status");
		headerRow.createCell(6).setCellValue("Service suivant");

		// Fill data rows
		int rowNum = 1;
		for (Request request : requests) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(request.getId());
			row.createCell(1).setCellValue(request.getTypeRequest());
			row.createCell(2).setCellValue(request.getCreationDate());
			row.createCell(3).setCellValue(request.getFirstName());
			row.createCell(4).setCellValue(request.getLastName());
			row.createCell(5).setCellValue(request.getStatus());
			row.createCell(6).setCellValue(request.getNextService());
		}

		// Auto-size columns
		for (int i = 0; i < 3; i++) {
			sheet.autoSizeColumn(i);
		}

		// Convert workbook to byte array
		byte[] excelBytes;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			workbook.write(outputStream);
			excelBytes = outputStream.toByteArray();
		} catch (IOException e) {
			// Handle any exception
			throw new RuntimeException("Failed to generate Excel file.", e);
		}

		return excelBytes;
	}

	public List<Request> searchRequests(String typeRequest, String creationDate, String status) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Request> cq = cb.createQuery(Request.class);
		Root<Request> root = cq.from(Request.class);

		List<Predicate> predicates = new ArrayList<>();

		if (typeRequest != null && !typeRequest.isEmpty()) {
			predicates.add(cb.equal(root.get("typeRequest"), typeRequest));
		}

		if (creationDate != null && !creationDate.isEmpty()) {
			predicates.add(cb.equal(root.get("creationDate"), creationDate));
		}

		if (status != null && !status.isEmpty()) {
			predicates.add(cb.equal(root.get("status"), status));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));

		TypedQuery<Request> query = entityManager.createQuery(cq);
		return query.getResultList();
	}


}
