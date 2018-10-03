package com.example.licenses.testHelper;

import com.example.licenses.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestHelperController {

    @Autowired
    private TestHelperService testHelperService;

    @RequestMapping(value = "v1/test-helper/allLicenses", method = RequestMethod.GET)
    public List<License> getAllLicenses() {
        return testHelperService.getAllLicenses();
    }

}
