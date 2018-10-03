package com.example.licenses.testHelper;

import com.example.licenses.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestHelperService {

    @Autowired
    private TestHelperRepository testHelperRepository;

    public List<License> getAllLicenses() {
        return testHelperRepository.findAll();
    }

}
