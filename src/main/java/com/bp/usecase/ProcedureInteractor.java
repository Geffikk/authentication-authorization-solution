package com.bp.usecase;

import com.bp.entity.Procedure;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.procedure.ProcedureRequestModel;
import com.bp.repository.UserVisitRepository;
import com.bp.service.IDefaultCapabilityService;
import com.bp.service.IProcedureService;
import com.bp.service.IUserRoleService;
import com.bp.usecase.capability.ICapabilityInteractor;

import java.util.List;

public class ProcedureInteractor implements ProcedureInputBoundary {
	
	/**
	 * Procedure service.
	 */
	private final IProcedureService procedureService;
	
	/**
	 * Capability application logic.
	 */
	private final ICapabilityInteractor capabilityInteractor;
	
	/**
	 * User visit repository.
	 */
	private final UserVisitRepository userVisitRepository;
	
	/**
	 * Default capability service.
	 */
	private final IDefaultCapabilityService defaultCapabilityService;
	
	/**
	 * User role service.
	 */
	private final IUserRoleService userRoleService;
	
	public ProcedureInteractor(
		IUserRoleService userRoleService,
		IDefaultCapabilityService defaultCapabilityService,
		IProcedureService procedureService,
		ICapabilityInteractor capabilityInteractor,
		UserVisitRepository userVisitRepository) {
		this.procedureService = procedureService;
		this.capabilityInteractor = capabilityInteractor;
		this.userVisitRepository = userVisitRepository;
		this.defaultCapabilityService = defaultCapabilityService;
		this.userRoleService = userRoleService;
	}
	
	@Override
	public void createProcedure(int visitId, Procedure procedure, AuthorizeModel authorizeModel) {
		procedureService.createProcedure(procedure, authorizeModel, visitId);
		authorizeModel.setEntityId(procedure.getProcedureId());
		capabilityInteractor.createOwner(authorizeModel);
		
		// Create capabilities for procedure members (existing in visit)
		List<UserEntity> userEntities = userVisitRepository.getUsers(visitId);
		for (UserEntity userEntity : userEntities) {
			AuthorizeModel am = AuthorizeModel.copyAuthorizeModel(authorizeModel);
			capabilityInteractor.copyCapabilitiesForProcedure(userEntity, authorizeModel);
		}
	}
	
	@Override
	public List<Procedure> findAll(AuthorizeModel authorizeModel) {
		return procedureService.findAll(authorizeModel);
	}
	
	@Override
	public Procedure findById(int procedureId, AuthorizeModel authorizeModel) {
		return procedureService.findById(procedureId, authorizeModel);
	}
	
	@Override
	public void saveProcedure(Procedure procedure, AuthorizeModel authorizeModel) {
		procedureService.saveProcedure(procedure, authorizeModel);
	}
	
	@Override
	public void deleteProcedure(int procedureId, AuthorizeModel authorizeModel) {
		procedureService.deleteProcedure(procedureId, authorizeModel);
	}
	
	@Override
	public List<Procedure> findProceduresByVisit(int visitId, AuthorizeModel authorizeModel) {
		return procedureService.findProceduresByVisitId(visitId, authorizeModel);
	}
	
	@Override
	public Procedure fillProcedure(Procedure procedure, ProcedureRequestModel procedureRequestModel) {
		if (procedureRequestModel.getBilling() != null) {
			procedure.setBilling(procedureRequestModel.getBilling());
		}
		if (procedureRequestModel.getProcedureTime() != null) {
			procedure.setProcedureTime(procedureRequestModel.getProcedureTime());
		}
		if (procedureRequestModel.getProcedureType() != null) {
			procedure.setProcedureType(procedureRequestModel.getProcedureType());
		}
		if (procedureRequestModel.getResult() != null) {
			procedure.setResult(procedureRequestModel.getResult());
		}
		if (procedureRequestModel.getVisitId() != 0) {
			procedure.setVisitId(procedureRequestModel.getVisitId());
		}
		return procedure;
	}
}
