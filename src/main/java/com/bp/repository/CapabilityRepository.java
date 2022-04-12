package com.bp.repository;

import com.bp.entity.Capability;
import com.bp.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CapabilityRepository {
	
	/**
	 * Find all capabilities.
	 * @return all capabilities
	 */
	@Select("SELECT * FROM capability")
	List<Capability> findAll();
	
	/**
	 * Find capability by id.
	 * @param id of visit
	 * @return visit
	 */
	@Select("SELECT * FROM capability WHERE capId=#{capId}")
	Capability findById(int id);
	
	/**
	 * Find specific capabilities.
	 * @param roles of user.
	 * @param entityType type of entity.
	 * @param entityId id of entity.
	 * @param user user email.
	 * @return user/role capabilities.
	 */
	@SelectProvider(type = com.bp.utility.SqlBobTheBuilder.class, method = "findBySpecificRole")
	List<Capability> findCapability(@Param("roles") List<Role> roles, String entityType, Integer entityId, String user);
	
	/**
	 * Find capability specific by role.
	 * @param entityType type of entity
	 * @param entityId id of entity
	 * @param role of user.
	 * @return capability specific by role.
	 */
	@Select("SELECT * FROM capability WHERE entityType=#{entityType} AND entityId=#{entityId} AND role=#{role}")
	Capability findBySpecificRole(String entityType, Integer entityId, String role);
	
	/**
	 * Find capability by entity.
	 * @param entityType type of entity.
	 * @param entityId id of entity.
	 * @return capabilities specific by entity.
	 */
	@Select("SELECT * FROM capability WHERE entityType=#{entityType} AND entityId=#{entityId}")
	List<Capability> findByEntity(String entityType, Integer entityId);
	
	/**
	 * Find capability by user.
	 * @param entityType type of entity.
	 * @param entityId id of entity.
	 * @param user user email.
	 * @return capability specific by user email.
	 */
	@Select("SELECT * FROM capability WHERE entityType=#{entityType} AND entityId=#{entityId} AND user=#{user}")
	Capability findByUser(String entityType, Integer entityId, String user);
	
	/**
	 * Insert capability to database.
	 * @param capability for insert
	 */
	@Insert(
		" INSERT INTO capability(                                " +
		"   role, user, entityType, entityId,                    " +
		"   create, update, delete, read, capability, priority)  " +
		" VALUES(                                                " +
		"   #{role},#{user},#{entityType},#{entityId},           " +
		"   #{create},#{update},#{delete},#{read},#{capability}, " +
		"   #{priority})                                         ")
	@Options(useGeneratedKeys = true, keyProperty = "capId")
	void insert(Capability capability);
	
	/**
	 * Update procedure in database.
	 * @param capability for update
	 */
	@Update(
		" UPDATE capability SET       " +
		"   role=#{role},             " +
		"   user=#{user},             " +
		"   entityType=#{entityType}, " +
		"   entityId=#{entityId},     " +
		"   read=#{read},             " +
		"   create=#{create},         " +
		"   update=#{update},         " +
		"   delete=#{delete},         " +
		"   capability=#{capability}, " +
		"   priority=#{priority}      " +
		" WHERE capId=#{capId};       ")
	void update(Capability capability);
	
	/**
	 * Delete visit from db.
	 * @param id of visit
	 */
	@Delete("DELETE FROM capability WHERE capId=#{capId}")
	void delete(int id);
	
	/**
	 * Delete all capabilities from db.
	 */
	@Delete("DELETE FROM capability")
	void deleteAll();
	
	/**
	 * Create index on capability.
	 */
	@Insert("CREATE INDEX capability_entityType_entityId_user_role_idx ON capability (entityType, entityId, role, user)")
	void createIndexOnCapability();
	
	/**
	 * Create index on capability.
	 */
	@Insert("CREATE INDEX capability_entityType_entityId_role_idx ON capability (entityType, entityId, role, user)")
	void createIndexOnCapabilityRole();
	
	/**
	 * Create index on capability.
	 */
	@Insert("CREATE INDEX capability_entityType_entityId_user_idx ON capability (entityType, entityId, user)")
	void createIndexOnCapabilityUser();
	
	/**
	 * Create index on capability.
	 */
	@Insert("CREATE INDEX capability_entityType_role_idx ON capability (entityType, role)")
	void createIndexOnCapabilityEntityTypeRole();
	
	/**
	 * Create index on capability.
	 */
	@Insert("CREATE INDEX capability_entityType_user_role_idx ON capability (entityType, role, user)")
	void createIndexOnCapabilityWithoutEntityId();
}
