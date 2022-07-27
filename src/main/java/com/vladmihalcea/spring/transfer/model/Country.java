package com.vladmihalcea.spring.transfer.model;

import java.util.Locale;

/**
 * @author Vlad Mihalcea
 */
public enum Country {
    //Europe
    AUSTRIA("AT"),
    BELGIUM("BE"),
    BULGARIA("BG"),
    CROATIA("HR"),
    CYPRUS("CY"),
    CZECHIA("CZ"),
    DENMARK("DK"),
    ESTONIA("EE"),
    SPAIN("ES"),
    FINLAND("FI"),
    FRANCE("FR"),
    GERMANY("DE"),
    GREECE("GR"),
    HUNGARY("HU"),
    ICELAND("IS"),
    IRELAND("IE"),
    ITALY("IT"),
    LATVIA("LV"),
    LIECHTENSTEIN("LI"),
    LITHUANIA("LT"),
    LUXEMBOURG("LU"),
    MALTA("MT"),
    MONACO("MC"),
    NETHERLANDS("NL"),
    NORWAY("NO"),
    POLAND("PL"),
    PORTUGAL("PT"),
    ROMANIA("RO"),
    SLOVAKIA("SK"),
    SLOVENIA("SI"),
    SWEDEN("SE"),
    SWITZERLAND("CH"),
    UK("GB"),
    //North America
    US("US"),
    CANADA("CA"),
    MEXICO("MX"),
    //South America
    ARGENTINA("AR"),
    BOLIVIA("BO"),
    BRAZIL("BR"),
    CHILE("CL"),
    COLOMBIA("CO"),
    COSTA_RICA("CR"),
    PERU("PE"),
    URUGUAY("UY"),
    //Middle East
    ISRAEL("IL"),
    JORDAN("JO"),
    KUWAIT("KW"),
    LEBANON("LB"),
    SAUDI_ARABIA("SA"),
    QATAR("QA"),
    TURKEY("TR"),
    UNITED_ARAB_EMIRATES("AE"),
    //Asia
    AUSTRALIA("AU"),
    HONG_KONG("HK"),
    MALAYSIA("MY"),
    NEW_ZEELAND("NZ"),
    SINGAPORE("SG"),
    SOUTH_KOREA("KR"),
    JAPAN("JP"),
    TAIWAN("TW"),
    THAILAND("TH"),
    //Africa
    EGYPT("EG"),
    KENYA("KE"),
    MOROCCO("MA"),
    SOUTH_AFRICA("ZA"),
    ;

    private String countryCode;

    Country(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public static Country resolveOrNull(String token) {
        for(Country country : values()) {
            if(country.getCountryCode().toLowerCase(Locale.ROOT).contains(token.toLowerCase(Locale.ROOT))) {
                return country;
            }
        }
        return null;
    }
}
