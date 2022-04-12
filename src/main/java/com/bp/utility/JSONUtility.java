package com.bp.utility;

import com.bp.entity.Procedure;
import com.bp.entity.Role;
import com.bp.entity.Visit;
import com.bp.model.user.UserRequestModel;
import com.bp.model.user.UserResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

/**
 * JSON utility class.
 */
public class JSONUtility {
	
	/**
	 * Object to json format.
	 * @param obj object
	 * @return json
	 * @throws JsonProcessingException exception
	 */
	public static String objToJSON(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(obj);
	}
	
	/**
	 * Json deserialize to visit model.
	 * @param json json
	 * @return visit
	 */
	public static Visit jsonToVisit(String json) {
		Gson g = new Gson();
		return g.fromJson(json, Visit.class);
	}
	
	/**
	 * Json deserialize to procedure model.
	 * @param json json
	 * @return procedure
	 */
	public static Procedure jsonToProcedure(String json) {
		Gson g = new Gson();
		return g.fromJson(json, Procedure.class);
	}
	
	/**
	 * Json deserialize to role model.
	 * @param json json
	 * @return role
	 */
	public static Role jsonToRole(String json) {
		Gson g = new Gson();
		return g.fromJson(json, Role.class);
	}
	
	/**
	 * Json deserialize to user model.
	 * @param json json
	 * @return user
	 */
	public static UserRequestModel jsonToUser(String json) {
		Gson g = new Gson();
		return g.fromJson(json, UserRequestModel.class);
	}
	
	/**
	 * Json deserialize to user model.
	 * @param json json
	 * @return user
	 */
	public static UserResponseModel jsonToUserResponse(String json) {
		Gson g = new Gson();
		return g.fromJson(json, UserResponseModel.class);
	}
}
