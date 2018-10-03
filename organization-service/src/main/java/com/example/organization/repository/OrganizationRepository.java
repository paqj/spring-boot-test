package com.example.organization.repository;

import com.example.organization.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization,String>  {
    public Organization findByOrganizationId(String organizationId);
}

