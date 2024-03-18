package com.stm.pegelhub.lib.internal.dto;

/**
 * DTO to create contact data.
 */
public record ContactSendDto(String organization, String contactPerson,
                         String contactStreet, String contactPlz,
                         String location, String contactCountry,
                         String emergencyNumber, String emergencyNumberTwo,
                         String emergencyMail, String serviceNumber,
                         String serviceNumberTwo, String serviceMail,
                         String administrationPhoneNumber,
                         String administrationPhoneNumberTwo,
                         String administrationMail, String contactNodes) {

}