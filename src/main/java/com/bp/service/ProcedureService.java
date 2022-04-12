package com.bp.service;

import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.ProcedureRepository;
import com.bp.entity.Procedure;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;
import com.bp.utility.UserUtility;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.List;
import java.util.logging.Logger;

public class ProcedureService implements IProcedureService {
	
	Logger logger = Logger.getLogger(ProcedureService.class.getName());
	
	/**
	 * Procedure repository.
	 */
	private final ProcedureRepository procedureRepository;
	
	/**
	 * Capability application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	public ProcedureService(ProcedureRepository procedureRepository, CapabilityInteractor capabilityInteractor) {
		this.procedureRepository = procedureRepository;
		this.capabilityInteractor = capabilityInteractor;
	}
	
	@Override
	public Procedure findById(final int id, AuthorizeModel authorizeModel) {
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = metricRegistry.timer("Timer 1 (GET PROCEDURE)");
		Timer.Context context = timer.time();
		
		Timer timer2 = new Timer();
		metricRegistry.register("Timer 2 (GET PROCEDURE)", timer2);
		double timeAuth = 0;
		Timer.Context context2 = timer2.time();
		
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PROCEDURE);
			authorizeModel.setOperation(CS.sREAD);
			authorizeModel.setEntityId(id);
			capabilityInteractor.isPermitted(authorizeModel);
			timeAuth = (double) context2.stop();
		}
		Procedure procedure = procedureRepository.findById(id);
		// Metrics
		double timeResult = (double) context.stop();
		double timeWithoutAuth = timeResult - timeAuth;
		String timeStrAuth = UserUtility.getTimeFormat(timeResult);
		String timeStrWithoutAuth = UserUtility.getTimeFormat(timeWithoutAuth);
		logger.info("Time with authorization (GET PROCEDURE): " + timeStrAuth);
		logger.info("Time without authorization (GET PROCEDURE): " + timeStrWithoutAuth);
		return procedure;
	}
	
	@Override
	public List<Procedure> findAll(AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PROCEDURE);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return procedureRepository.findAll();
	}

	@Override
	public void createProcedure(final Procedure procedure, AuthorizeModel authorizeModel, int visitId) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setEntityId(visitId);
			authorizeModel.setOperation(CS.sUPDATE);
			capabilityInteractor.isPermitted(authorizeModel);
			AuthorizeModel a = new AuthorizeModel();
			a.setEntityId(null);
			a.setEntityType(CS.PROCEDURE);
			a.setOperation(CS.sCREATE);
			a.setAuthenticatedUser(authorizeModel.getAuthenticatedUser());
			capabilityInteractor.isPermitted(a);
		}
		procedureRepository.insert(procedure);
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PROCEDURE);
			authorizeModel.setEntityId(procedure.getProcedureId());
		}
	}
	
	@Override
	public void saveProcedure(Procedure procedure, AuthorizeModel authorizeModel) {
		// Metrics
		MetricRegistry metricRegistry = new MetricRegistry();
		Timer timer = metricRegistry.timer("Timer 1 (UPDATE PROCEDURE)");
		Timer.Context context = timer.time();
		
		Timer timer2 = new Timer();
		metricRegistry.register("Timer 2 (UPDATE PROCEDURE)", timer2);
		double timeAuth = 0;
		Timer.Context context2 = timer2.time();
		
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.VISIT);
			authorizeModel.setOperation(CS.sUPDATE);
			authorizeModel.setEntityId(procedure.getVisitId());
			capabilityInteractor.isPermitted(authorizeModel);
			authorizeModel.setEntityType(CS.PROCEDURE);
			authorizeModel.setOperation(CS.sUPDATE);
			authorizeModel.setEntityId(procedure.getProcedureId());
			capabilityInteractor.isPermitted(authorizeModel);
			timeAuth = (double) context2.stop();
		}
		procedureRepository.update(procedure);
		// Metrics
		double timeResult = (double) context.stop();
		double timeWithoutAuth = timeResult - timeAuth;
		String timeStrAuth = UserUtility.getTimeFormat(timeResult);
		String timeStrWithoutAuth = UserUtility.getTimeFormat(timeWithoutAuth);
		logger.info("Time with authorization (UPDATE PROCEDURE): " + timeStrAuth);
		logger.info("Time without authorization (UPDATE PROCEDURE): " + timeStrWithoutAuth);
	}
	
	@Override
	public void deleteProcedure(final int id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PROCEDURE);
			authorizeModel.setOperation(CS.sDELETE);
			authorizeModel.setEntityId(id);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		procedureRepository.delete(id);
	}
	
	@Override
	public List<Procedure> findProceduresByVisitId(int visitId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.PROCEDURE);
			authorizeModel.setOperation(CS.sDELETE);
			authorizeModel.setEntityId(visitId);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return procedureRepository.findProceduresByVisitId(visitId);
	}
}
