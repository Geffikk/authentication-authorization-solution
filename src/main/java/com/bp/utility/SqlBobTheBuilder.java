package com.bp.utility;

import com.bp.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SQL builder, for building dynamic sql queries.
 */
@Component
public class SqlBobTheBuilder {
	
	public String findBySpecificRole(Map<String, Object> parameters) {
		List<Role> roles = (List<Role>) parameters.get("roles");
		StringBuilder builder = new StringBuilder(
			"SELECT capId, role, create, read, update, delete, capability, priority FROM capability WHERE " +
			"(entityType=#{entityType} AND entityId ");
		
		Integer entityId = (Integer) parameters.get("entityId");
		String entityType = (String) parameters.get("entityType");
		if (entityId == null && entityType.equals(CS.PROCEDURE)) {
			builder.append("IS NULL");
		} else {
			builder.append("= #{entityId}");
		}
		builder.append(" AND role='PERSONAL' AND user=#{user}) OR (entityType=#{entityType} AND entityId=#{entityId} AND role IN (");
		
		if (roles != null && roles.size() != 0) {
			for (Role i : roles) {
				builder.append("'").append(i.getName()).append("'").append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append(") AND user IS NULL) OR (entityType=#{entityType} AND entityId IS NULL AND role IN (");
		
		if (roles != null && roles.size() != 0) {
			for (Role i : roles) {
				builder.append("'").append(i.getName()).append("'").append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append(") AND user IS NULL) ORDER BY priority DESC");
		
		return builder.toString();
	}
	
	public String findByRoleAndByEntityAndPosition(Map<String, Object> parameters) {
		List<Role> roles = (List<Role>) parameters.get("roles");
		StringBuilder builder =
			new StringBuilder("SELECT * FROM default_capability WHERE entity=#{entity} AND name=#{name} AND role IN (");
		
		if (roles != null && roles.size() != 0) {
			for (Role i : roles) {
				builder.append("'").append(i.getName()).append("'").append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append(")");
		return builder.toString();
	}
}
