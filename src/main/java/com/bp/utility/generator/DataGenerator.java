package com.bp.utility.generator;

import com.bp.entity.Capability;
import com.bp.entity.DefaultCapability;
import com.bp.entity.Patient;
import com.bp.entity.Procedure;
import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.CapabilityRepository;
import com.bp.repository.DefaultCapabilityRepository;
import com.bp.repository.PatientRepository;
import com.bp.repository.ProcedureRepository;
import com.bp.repository.UserRepository;
import com.bp.repository.UserRoleRepository;
import com.bp.repository.UserVisitRepository;
import com.bp.repository.VisitRepository;
import com.bp.service.IDefaultCapabilityService;
import com.bp.usecase.VisitInputBoundary;
import com.bp.utility.CS;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Data generator
 */
public class DataGenerator {
	
	private static final int numberOfUsers = 100000;
	
	private static final int numberOfVisits = 100000;
	
	private static final int numberOfProcedures = 100000;
	
	private static final int numberOfCapProcedures = 100000;
	
	private static final int numberOfCapabilities = 100000;
	
	private final UserRoleRepository userRoleRepository;
	
	private final UserVisitRepository userVisitRepository;
	
	private final PatientRepository patientRepository;
	
	private final IDefaultCapabilityService defaultCapabilityService;
	
	private final DefaultCapabilityRepository defaultCapabilityRepository;
	
	private final AuthorizeModel authorizeModel = new AuthorizeModel();
	
	private final UserRepository userRepository;
	
	private final VisitRepository visitRepository;
	
	private final ProcedureRepository procedureRepository;
	
	private final CapabilityRepository capabilityRepository;
	
	private final VisitInputBoundary visitInput;
	
	private final PasswordEncoder passwordEncoder;
	
	public DataGenerator(
		DefaultCapabilityRepository defaultCapabilityRepository,
		UserRoleRepository userRoleRepository,
		VisitInputBoundary visitInput,
		IDefaultCapabilityService defaultCapabilityService,
		UserRepository userRepository,
		VisitRepository visitRepository,
		ProcedureRepository procedureRepository,
		PatientRepository patientRepository,
		UserVisitRepository userVisitRepository,
		CapabilityRepository capabilityRepository,
		PasswordEncoder passwordEncoder) {
		this.defaultCapabilityService = defaultCapabilityService;
		this.userRepository = userRepository;
		this.userVisitRepository = userVisitRepository;
		this.visitRepository = visitRepository;
		this.userRoleRepository = userRoleRepository;
		this.procedureRepository = procedureRepository;
		this.patientRepository = patientRepository;
		this.defaultCapabilityRepository = defaultCapabilityRepository;
		this.capabilityRepository = capabilityRepository;
		this.passwordEncoder = passwordEncoder;
		this.visitInput = visitInput;
	}
	
	public void generateDataXS() {
		generateUsers();
		userRepository.createUserIdIndexOnUser();
		userRepository.createEmailIndexOnUser();
		userRepository.createTokenIndexOnUser();
		
		generateVisits();
		visitRepository.createVisitIdIndexOnVisit();
		
		generateProcedure();
		procedureRepository.createProcedureIdIndexOnProcedure();
		
		addUsersToVisit();
		generateCapability();
		generateProcedureCapability();
		capabilityRepository.createIndexOnCapability();
		capabilityRepository.createIndexOnCapabilityRole();
		capabilityRepository.createIndexOnCapabilityUser();
		capabilityRepository.createIndexOnCapabilityEntityTypeRole();
		capabilityRepository.createIndexOnCapabilityWithoutEntityId();
		
		userRoleRepository.createIndexOnUserRoleIdx();
	}
	
	private void generateCapability() {
		Random random = new Random();
		Capability capability;
		UserEntity userEntity;
		int randomUser;
		int randomEntityType;
		int randomEntityId;
		int numberOfPatient = patientRepository.findAll().size();
		
		String[] entityType = {"visit", "procedure", "patient"};
		
		for (int i = 0; i < DataGenerator.numberOfCapabilities; i++) {
			randomUser = random.nextInt(DataGenerator.numberOfUsers - 1);
			userEntity = userRepository.findById(randomUser);
			if (userEntity != null) {
				capability = new Capability();
				randomEntityType = random.nextInt(2);
				capability.setEntityType(entityType[randomEntityType]);
				if (entityType[randomEntityType].equals(CS.VISIT)) {
					randomEntityId = random.nextInt(numberOfVisits - 1);
				} else if (entityType[randomEntityType].equals(CS.PROCEDURE)) {
					randomEntityId = random.nextInt(numberOfProcedures - 1);
				} else {
					randomEntityId = random.nextInt(numberOfPatient - 1);
				}
				capability.setEntityId(randomEntityId);
				capability.setRole(CS.PERSONAL);
				capability.setUser(userEntity.getEmail());
				capability.setPriority(3);
				capability.setRead(random.nextBoolean());
				capability.setCreate(random.nextBoolean());
				capability.setDelete(random.nextBoolean());
				capability.setUpdate(random.nextBoolean());
				capability.setCapability(random.nextBoolean());
				capabilityRepository.insert(capability);
			}
		}
	}
	
	private void generateProcedureCapability() {
		Random random = new Random();
		int randomUser;
		int randomProcedure;
		UserEntity user;
		
		for (int i = 0; i < DataGenerator.numberOfCapProcedures; i++) {
			randomUser = random.nextInt(numberOfUsers - 1);
			randomProcedure = random.nextInt(numberOfProcedures - 1);
			user = userRepository.findById(randomUser);
			if (user != null) {
				Capability capability = new Capability();
				capability.setEntityId(randomProcedure);
				capability.setEntityType(CS.PROCEDURE);
				capability.setRole(CS.PERSONAL);
				capability.setUser(user.getEmail());
				capability.setCreate(random.nextBoolean());
				capability.setRead(random.nextBoolean());
				capability.setUpdate(random.nextBoolean());
				capability.setDelete(random.nextBoolean());
				capabilityRepository.insert(capability);
			}
		}
	}
	
	public void generateUsers() {
		FakeValuesService fakeValuesService = new FakeValuesService(
			new Locale("cs-CZ"), new RandomService());
		
		Faker faker = new Faker();
		UserEntity user = new UserEntity();
		String firstName;
		String lastName;
		String email;
		ArrayList<String> roles = new ArrayList<>();
		roles.add("DOCTOR");
		roles.add("SISTER");
		Random random = new Random();
		
		authorizeModel.setEntityType(CS.VISIT);
		
		for (int i = 0; i < DataGenerator.numberOfUsers; i++) {
			authorizeModel.setGivenRole(roles.get(random.nextInt(roles.size())));
			firstName = faker.name().firstName();
			lastName = faker.name().lastName();
			email = firstName.toLowerCase() + "." + lastName.toLowerCase() + fakeValuesService.bothify("########@gmail.com");
			user.setEmail(email);
			user.setPassword("$2y$10$4WXyw3LZjuv7yXSnL1mK/eupM9WfUIbHdvttuPsrCDumKgHYYvUnm");
			user.setPhone(faker.phoneNumber().phoneNumber());
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setBirthdate(faker.date().birthday());
			user.setActive(1);
			userRepository.insert(user);
			userRoleRepository.addRole(user.getUserId(), "USER", null);
			if (i % 3 == 0) {
				userRoleRepository.addRole(user.getUserId(), roles.get(random.nextInt(roles.size())), null);
				userRoleRepository.addRole(user.getUserId(), "EMPLOYER", null);
			}
		}
	}
	
	public void generateVisits() {
		FakeValuesService fakeValuesService = new FakeValuesService(
			new Locale("cs-CZ"), new RandomService());
		
		ArrayList<String> symptoms = new ArrayList<>();
		symptoms.add("tiredness");
		symptoms.add("dry cough");
		symptoms.add("fever");
		symptoms.add("aches and pains");
		symptoms.add("sore throat");
		symptoms.add("diarrhoea");
		symptoms.add("conjunctivitis");
		symptoms.add("loss of taste or smell");
		symptoms.add("difficulty breathing or shortness of breath");
		
		ArrayList<String> reasons = new ArrayList<>();
		reasons.add("Test");
		reasons.add("Test on Antibodies");
		reasons.add("Vaccination");
		
		Faker faker = new Faker();
		String firstName;
		String lastName;
		long identification;
		String phone;
		String email;
		String city;
		String address;
		Date birthdate;
		Random random = new Random();
		
		for (int i = 0; i < DataGenerator.numberOfVisits; i++) {
			identification = generateIdentification();
			
			Visit visit = new Visit();
			Patient patient = patientRepository.findByIdentification(identification);
			
			if (patient != null) {
				firstName = patient.getFirstName();
				lastName = patient.getLastName();
				email = patient.getEmail();
				phone = patient.getPhone();
				city = patient.getCity();
				address = patient.getAddress();
				birthdate = patient.getBirthdate();
				visit.setPatientId(patient.getPatientId());
			} else {
				firstName = faker.name().firstName();
				lastName = faker.name().lastName();
				email = firstName.toLowerCase() + "." + lastName.toLowerCase() + fakeValuesService.bothify("####@gmail.com");
				phone = faker.phoneNumber().phoneNumber();
				city = faker.address().city();
				address = faker.address().streetAddress();
				birthdate = faker.date().birthday();
				Patient patient1 = new Patient();
				patient1.setIdentification(identification);
				patient1.setEmail(email);
				patient1.setPhone(phone);
				patient1.setAddress(address);
				patient1.setCity(city);
				patient1.setFirstName(firstName);
				patient1.setLastName(lastName);
				patientRepository.insert(patient1);
				visit.setPatientId(patient1.getPatientId());
			}
			visit.setEmail(email);
			visit.setPhone(phone);
			visit.setFirstName(firstName);
			visit.setLastName(lastName);
			visit.setIdentification(identification);
			visit.setSymptoms(symptoms.get(random.nextInt(symptoms.size())));
			visit.setReason(reasons.get(random.nextInt(reasons.size())));
			visit.setCity(city);
			visit.setAddress(address);
			visit.setVisitTime(faker.date().future(50, TimeUnit.DAYS));
			visit.setBirthdate(birthdate);
			if (visit.getPatientId() != 0) {
				visitRepository.insert(visit);
			}
		}
	}
	
	public long generateIdentification() {
		return (long)(Math.random() * 100000000 + 3333000L);
	}
	
	public void generateProcedure() {
		ArrayList<String> type = new ArrayList<>();
		type.add("Test");
		type.add("Vaccination");
		type.add("Test on Antibodies");
		
		ArrayList<String> result1 = new ArrayList<>();
		result1.add("Positive");
		result1.add("Negative");
		
		ArrayList<String> result2 = new ArrayList<>();
		result2.add("All right");
		
		ArrayList<String> result3 = new ArrayList<>();
		result3.add("have antibodies");
		result3.add("Don't have antibodies");
		
		Faker faker = new Faker();
		Random random = new Random();
		
		String procedureType;
		String result = null;
		String billing = null;
		
		for (int i = 0; i < DataGenerator.numberOfCapProcedures; i++) {
			Procedure procedure = new Procedure();
			procedureType = type.get(random.nextInt(type.size()));
			
			switch (procedureType) {
				case "Test":
					result = result1.get(random.nextInt(result1.size()));
					billing = "50$";
					break;
				case "Vaccination":
					result = result2.get(random.nextInt(result2.size()));
					billing = "free";
					break;
				case "Test on Antibodies":
					result = result3.get(random.nextInt(result3.size()));
					billing = "20$";
					break;
				default:
					break;
			}
			procedure.setProcedureType(procedureType);
			procedure.setResult(result);
			procedure.setBilling(billing);
			procedure.setProcedureTime(faker.date().future(50, TimeUnit.DAYS));
			procedure.setVisitId(random.nextInt(DataGenerator.numberOfVisits - 1) + 1);
			try {
				if (procedure.getVisitId() != 0) {
					procedureRepository.insert(procedure);
				}
			} catch (Exception ignored) { }
 		}
	}
	
	public void addUsersToVisit() {
		Random random = new Random();
		String role;
		ArrayList<String> roles = new ArrayList<>();
		roles.add("DOCTOR");
		roles.add("SISTER");
		roles.add("OWNER");
		
		for (int i = 0; i < DataGenerator.numberOfUsers; i++) {
			int random1 = random.nextInt(10 - 1) + 1;
			int random2 = random.nextInt(DataGenerator.numberOfUsers - 1) + 1;
			UserEntity userEntity = userRepository.findById(random2);
			authorizeModel.setHostUserId(userEntity.getUserId());
			
			for (int j = 0; j < random1; j++) {
				try {
					authorizeModel.setGivenRole(roles.get(random.nextInt(roles.size())));
					int random3 = random.nextInt(DataGenerator.numberOfVisits - 1) + 1;
					Visit visit = visitRepository.findById(random3);
					
					int numOfRole = userRoleRepository.getRoles(userEntity.getUserId()).size();
					if (numOfRole > 0) {
						authorizeModel.setEntityType(CS.VISIT);
						while (true) {
							role = userRoleRepository.getRoles(userEntity.getUserId()).get(random.nextInt(numOfRole)).getName();
							if (role.equals(CS.DOCTOR) || role.equals(CS.SISTER) || role.equals(CS.USER)) {
								DefaultCapability capability = defaultCapabilityService.findByRoleAndByEntity(authorizeModel);
								authorizeModel.setDefaultCapability(capability);
								userVisitRepository.addUserVisit(userEntity.getUserId(), visit.getVisitId());
								break;
							} else if (role.equals(CS.OWNER)) {
								DefaultCapability capability = defaultCapabilityService.findByRoleAndByEntityAndPosition(authorizeModel, CS.OWNER);
								authorizeModel.setDefaultCapability(capability);
								userVisitRepository.addUserVisit(userEntity.getUserId(), visit.getVisitId());
							}
						}
					}
				} catch (Exception ignored) {
				}
			}
		}
	}
}