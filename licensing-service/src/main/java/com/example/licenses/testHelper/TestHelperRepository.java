package com.example.licenses.testHelper;

import com.example.licenses.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestHelperRepository extends CrudRepository<License, String> {
    public List<License> findAll();


}
