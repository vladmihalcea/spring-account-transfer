package com.vladmihalcea.spring.transfer.service;

import com.vladmihalcea.spring.transfer.model.Country;
import com.vladmihalcea.spring.transfer.model.dto.GeoLocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Vlad Mihalcea
 */
@Service
public class GeoLocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationService.class);

    private static final String GEO_LOCATION_URL_TEMPLATE = "http://ip-api.com/json/%s";

    @Autowired
    private RestOperations restTemplate;

    public Country resolveCountry(String ipAddressValue) {
        Country country = null;
        GeoLocationDto geoLocationDTO = getGeoLocation(ipAddressValue);
        if (geoLocationDTO != null) {
            country = Country.resolveOrNull(geoLocationDTO.getCountryCode());
            if (country == null) {
                throw new IllegalArgumentException(
                    String.format(
                        "The IP address [%s] comes from an unsupported country!",
                        ipAddressValue
                    )
                );
            }
        }
        return country;
    }

    private GeoLocationDto getGeoLocation(String ipAddressValue) {
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(ipAddressValue);
            if (ipAddress.isLoopbackAddress()) {
                return null;
            }
        } catch (UnknownHostException e) {
            LOGGER.error("Unknown host for IP address", e);
        }
        try {
            return restTemplate.getForObject(
                String.format(GEO_LOCATION_URL_TEMPLATE, ipAddress),
                GeoLocationDto.class
            );
        } catch (RestClientException e) {
            LOGGER.error("Cannot access geo location service", e);
        }
        return null;
    }
}