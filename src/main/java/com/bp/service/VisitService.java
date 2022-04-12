package com.bp.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bp.entity.Capability;
import com.bp.entity.Patient;
import com.bp.entity.Procedure;
import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.UserVisitRepository;
import com.bp.repository.VisitRepository;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;
import com.bp.utility.UserUtility;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.springframework.transaction.annotation.Transactional;

public class VisitService extends VisitServiceHelper implements IVisitService {
	
	Logger logger = Logger.getLogger(VisitService.class.getName());
	
	/**
	 * Visit repository.
	 */
	private final VisitRepository visitRepository;
	
	/**
	 * Patient service.
	 */
	private final IPatientService patientService;
	
	/**
	 * Capability service.
	 */
	private final ICapabilityService capabilityService;
	
	/**
	 * Capability, application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	/**
	 * Procedure service.
	 */
	private final IProcedureService procedureService;
	
	/**
	 * User visit repository.
	 */
	private final UserVisitRepository userVisitRepository;
	
	public VisitService(
		IProcedureService procedureService,
		VisitRepository visitRepository,
		IPatientService patientService,
		ICapabilityService capabilityService,
		UserVisitRepository userVisitRepository,
		CapabilityInteractor capabilityInteractor) {
		this.procedureService = procedureService;
		this.visitRepository = visitRepository;
		this.patientService = patientService;
		this.capabilityService = capabilityService;
		this.userVisitRepository = userVisitRepository;
		this.capabilityInteractor = capabilityInteractor;
	}
	
	@Override
	public Visit findById(final int id, AuthorizeModel authorizeModel) {
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = metricRegistry.timer("Timer 1 (GET VISIT)");
		Timer.Context context = timer.time();
		
		Timer timer2 = new Timer();
		metricRegistry.register("Timer 2 (GET VISIT)", timer2);
		double timeAuth = 0;
		Timer.Context context2 = timer2.time();
		
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setEntityId(id);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
			timeAuth = (double) context2.stop();
		}
		Visit visit = visitRepository.findById(id);
		
		// Metrics
		double timeResult = (double) context.stop();
		double timeWithoutAuth = timeResult - timeAuth;
		String timeStrAuth = UserUtility.getTimeFormat(timeResult);
		String timeStrWithoutAuth = UserUtility.getTimeFormat(timeWithoutAuth);
		logger.info("Time with authorization (GET VISIT): " + timeStrAuth);
		logger.info("Time without authorization (GET VISIT): " + timeStrWithoutAuth);
		
		return visit;
	}
	
	@Override
	public List<Visit> findAll(AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return visitRepository.findAll();
	}

	@Override
	@Transactional
	public void createVisit(final Visit visit, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setOperation(CS.sCREATE);
			authorizeModel.setEntityId(visit.getVisitId());
			capabilityInteractor.isPermitted(authorizeModel);
		}
		logger.log(Level.INFO, "Creating a visit.");
		Patient patient = patientService.findByIdentification(visit.getIdentification(), null);
		
		if (patient == null) {
			logger.log(Level.INFO, "Creating a patient card.");
			patient = new Patient();
			setPatientInformation(visit, patient);
			patient.setIdentification(visit.getIdentification());
			patientService.createPatient(patient, null);
		}
		visit.setPatientId(patient.getPatientId());
		visitRepository.insert(visit);
		if (authorizeModel != null) {
			authorizeModel.setEntityId(visit.getVisitId());
		}
	}

	@Override
	@Transactional
	public void saveVisit(Visit visit, AuthorizeModel authorizeModel) {
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = metricRegistry.timer("Timer 1 (UPDATE VISIT)");
		Timer.Context context = timer.time();
		
		Timer timer2 = new Timer();
		metricRegistry.register("Timer 2 (UPDATE VISIT)", timer2);
		double timeAuth = 0;
		Timer.Context context2 = timer2.time();
		
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setOperation(CS.sUPDATE);
			authorizeModel.setEntityId(visit.getVisitId());
			capabilityInteractor.isPermitted(authorizeModel);
			timeAuth = (double) context2.stop();
		}
		visitRepository.update(visit);
		logger.log(Level.INFO, "Updating a visit with id: " + visit.getVisitId() + " | " + visit.getEmail());
		
		// Metrics
		double timeResult = (double) context.stop();
		double timeWithoutAuth = timeResult - timeAuth;
		String timeStrAuth = UserUtility.getTimeFormat(timeResult);
		String timeStrWithoutAuth = UserUtility.getTimeFormat(timeWithoutAuth);
		logger.info("Time with authorization (UPDATE VISIT): " + timeStrAuth);
		logger.info("Time without authorization (UPDATE VISIT): " + timeStrWithoutAuth);
	}

	@Override
	@Transactional
	public void deleteVisit(final int id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setOperation(CS.sDELETE);
			authorizeModel.setEntityId(id);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		List<Capability> capabilities = capabilityService.findByEntity(CS.VISIT, id);
		for (Capability capability : capabilities) {
			capabilityService.deleteCapability(capability.getCapId(), null);
		}
		List<Procedure> procedures = procedureService.findProceduresByVisitId(id, null);
		for (Procedure procedure : procedures) {
			procedureService.deleteProcedure(procedure.getProcedureId(), null);
		}
		List<UserEntity> users = userVisitRepository.getUsers(id);
		for (UserEntity userEntity : users) {
			userVisitRepository.deleteVisitFromUser(userEntity.getUserId(), id);
		}
		
		visitRepository.delete(id);
		logger.log(Level.INFO, "Deleting visit with id: " + id);
	}
	
	@Override
	public List<Visit> findVisitsByPatientId(final long patientId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PATIENT);
			authorizeModel.setOperation(CS.sUPDATE);
			authorizeModel.setEntityId((int) patientId);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return visitRepository.findVisitsByPatientId(patientId);
	}
}
