package com.example.licenses.services;

import com.example.licenses.clients.OrganizationDiscoveryClient;
import com.example.licenses.clients.OrganizationFeignClient;
import com.example.licenses.clients.OrganizationRestTemplateClient;
import com.example.licenses.config.ServiceConfig;
import com.example.licenses.model.License;
import com.example.licenses.model.Organization;
import com.example.licenses.repository.LicenseRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;
//  OrganizationRestTemplateClient organizationRestClient;
//  OrganizationDiscoveryClient organizationDiscoveryClient;

    public License getLicense(String organizationId,String licenseId) {
        Organization organization = organizationFeignClient.getOrganization(organizationId);
//      Organization organization = organizationRestClient.getOrganization(organizationId);
//      Organization organization = organizationDiscoveryClient.getOrganization(organizationId);

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        return license
                .withOrganizationName(organization.getName())
                .withContactName(organization.getContactName())
                .withContactEmail(organization.getContactEmail())
                .withContactPhone(organization.getContactPhone())
                .withComment(config.getExampleProperty());
    }

    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
        threadPoolKey = "licenseByOrgThreadPool",
        threadPoolProperties =
                {@HystrixProperty(name = "coreSize", value = "30"),
                @HystrixProperty(name = "maxQueueSize", value = "100")})
    public List<License> getLicensesByOrg(String organizationId){
        randomlyRunLong();

        return licenseRepository.findByOrganizationId( organizationId );
    }

    @HystrixCommand
    private Organization getOrganization(String organizationId){
        return organizationFeignClient.getOrganization(organizationId);
    }

    public List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId(organizationId)
                .withProductName("Sorry no Licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    public void saveLicense(License license){
        license.withId( UUID.randomUUID().toString());

        licenseRepository.save(license);

    }

    public void updateLicense(License license){
      licenseRepository.save(license);
    }

    public void deleteLicense(License license){
        licenseRepository.deleteById( license.getLicenseId());
    }

    private void randomlyRunLong() {
        Random rand = new Random();

        int randomNum = rand.nextInt(3) + 1;

        if(randomNum == 3) {
            System.out.println("Hit as delayed service");
            sleep();
        }
    }

    private void sleep(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
