package com.bp.service;

import com.bp.entity.Capability;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.CapabilityRepository;
import com.bp.usecase.capability.CapabilityInteractor;
import com.bp.utility.CS;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service implementation of capability.
 */
public class CapabilityService implements ICapabilityService {
	
	Logger logger = Logger.getLogger(VisitService.class.getName());
	
	/**
	 * Capability repository.
	 */
	private final CapabilityRepository capabilityRepository;
	
	/**
	 * Capability application logic.
	 */
	@Inject
	private CapabilityInteractor capabilityInteractor;
	
	public CapabilityService(CapabilityRepository capabilityRepository) {
		this.capabilityRepository = capabilityRepository;
	}
	
	@Override
	public List<Capability> findAll(AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.sCAPABILITY);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return capabilityRepository.findAll();
	}
	
	@Override
	public Capability findById(int capId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			Capability capability = capabilityRepository.findById(capId);
			authorizeModel.setEntityType(capability.getEntityType());
			authorizeModel.setEntityId(capability.getEntityId());
			authorizeModel.setGivenRole(capability.getRole());
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return capabilityRepository.findById(capId);
	}
	
	@Override
	public void createCapability(Capability capability, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setOperation(CS.sCAPABILITY);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		capabilityRepository.insert(capability);
		logger.info("Creating capability for: " +
			capability.getRole() + " | " + capability.getUser());
		logger.info("(rules: read|" + capability.isRead() +
			", update|" + capability.isUpdate() +
			", create|" + capability.isCreate() +
			", delete|" + capability.isDelete() + ")");
	}
	
	@Override
	public void deleteCapability(int capId, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setOperation(CS.sCAPABILITY);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		capabilityRepository.delete(capId);
		logger.log(Level.INFO, "Deleting capability with id: " + capId);
	}
	
	@Override
	public void saveCapability(Capability capability, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.sCAPABILITY);
			authorizeModel.setOperation(CS.sUPDATE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		try {
			capabilityRepository.update(capability);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Resource already exists");
		}
	}
	
	@Override
	public List<Capability> findByEntity(String entityType, int id) {
		return capabilityRepository.findByEntity(entityType, id);
	}
	
	@Override
	public Capability findCapability(AuthorizeModel authorizeModel) {
		Capability capability;
		List<Capability> capabilities = capabilityRepository.findCapability(
			authorizeModel.getRoles(),
			authorizeModel.getEntityType(),
			authorizeModel.getEntityId(),
			authorizeModel.getAuthenticatedUser().getEmail());
		capabilities = getWithHighestPriority(capabilities);
		capability = findThisBest(capabilities, authorizeModel.getOperation());
		return capability;
	}
	
	@Override
	public Capability findBySpecificRole(AuthorizeModel authorizeModel) {
		return capabilityRepository.findBySpecificRole(authorizeModel.getEntityType(), authorizeModel.getEntityId(), authorizeModel.getGivenRole());
	}
	
	@Override
	public Capability findByUser(String entityType, Integer entityId, String user) {
		return capabilityRepository.findByUser(entityType, entityId, user);
	}
	
	/**
	 * Return list of capabilities with highest priority.
	 * @param capabilities of user
	 * @return capabilities with highest priority
	 */
	private List<Capability> getWithHighestPriority(List<Capability> capabilities) {
		List<Capability> withHighestPriority = new ArrayList<>();
		if (capabilities.size() > 0) {
			int priority = capabilities.get(0).getPriority();
			for (Capability capability : capabilities) {
				if (capability.getPriority() == priority) {
					withHighestPriority.add(capability);
				}
			}
		}
		return withHighestPriority;
	}
	
	/**
	 * Return capability, which allow specific operation,
	 * when all capabilities not allow operation return one.
	 * @param capabilities of user
	 * @param operation CRUD operation
	 * @return best capability
	 */
	private Capability findThisBest(List<Capability> capabilities, String operation) {
		Capability capability;
		if (capabilities.size() > 0) {
			if (operation == null) {
				return capabilities.get(0);
			}
			capability = capabilities.get(0);
			for (Capability capability1 : capabilities) {
				if (capability1.getOperation(operation)) {
					capability = capability1;
					break;
				}
			}
			return capability;
		}
		return null;
	}
}
