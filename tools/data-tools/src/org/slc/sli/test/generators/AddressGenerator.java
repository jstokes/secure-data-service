package org.slc.sli.test.generators;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.slc.sli.test.edfi.entities.Address;
import org.slc.sli.test.edfi.entities.AddressType;
import org.slc.sli.test.edfi.entities.CountryCodeType;
import org.slc.sli.test.edfi.entities.StateAbbreviationType;

public class AddressGenerator {

    public static final String RIGHT_NOW = "2011-03-04";

    private static final Logger log = Logger.getLogger(AddressGenerator.class);

    private String file_city_US = "database/address/city_US.csv";
    private String file_street_US = "database/address/street_US.csv";
    private int cityCount;
    private int streetCount;
    private Random rand = new Random();
    private List<String[]> cities = new ArrayList<String[]>();
    private List<String[]> streets = new ArrayList<String[]>();
    private String today =  null;
    private String yearAgo =  null;
    private String yearHence =  null;

    public AddressGenerator(StateAbbreviationType state) throws Exception{
        loadData(state);
        today   =  "2011-03-04";
        yearAgo =  "2010-03-04";
        yearHence =  "2012-03-04";
    }

    private void loadData(StateAbbreviationType stateParam) throws Exception
    {

        BufferedReader cityReader   = new BufferedReader(new InputStreamReader(new FileInputStream(file_city_US)));
        BufferedReader streetReader = new BufferedReader(new InputStreamReader(new FileInputStream(file_street_US)));

        //NH;PORTSMOUTH;Rockingham;210;603
        String cityLine;
        while((cityLine = cityReader.readLine()) != null)
        {
            String []cityParts = cityLine.split(";");
            if(cityParts.length > 4)
            {
                String state = cityParts[0];
                if(state.equals(stateParam.value()))
                {
                    //												String city = cityParts[1];
                    //												String county = cityParts[2];
                    //												String postalCode = cityParts[3];
                    //												String areaCode = cityParts[4];
                    cities.add(cityParts);
                }
            }
            else
            {
                log.warn(file_city_US + ": Invalid line [" + cityLine + "]. Less than 5 components.");
            }
        }
        cityCount = cities.size();
        //Maple Street,6103
        String streetLine;
        while((streetLine = streetReader.readLine()) != null)
        {
            String []streetParts = streetLine.split(",");
            if(streetParts.length > 1)
            {
                //												String streetName = streetParts[0];
                //												String bldgNumber = streetParts[1];
                streets.add(streetParts);
            }
            else
            {
                log.warn(file_street_US + ": Invalid line [" + streetLine + "]. Less than 2 components.");
            }
        }
        streetCount = streets.size();
    }

    private int getRand()
    {
        int num = rand.nextInt();
        return num < 0? -1 * num:num;
    }

    public Address getRandomAddress()
    {
        Address add = new Address();

        int selectedCity   =  getRand() % cityCount;
        int selectedStreet =  getRand() % streetCount;

        String [] selCity   = cities.get(selectedCity);
        String [] selStreet = streets.get(selectedStreet);

        String state        = selCity[0];
        String city         = selCity[1];
        String county       = selCity[2];
        String postalCode   = selCity[3];
        String areaCode     = selCity[4];

        String streetName = selStreet[0];
        String bldgNumber = selStreet[1];

        add.setStreetNumberName(streetName);
        add.setApartmentRoomSuiteNumber(String.valueOf(getRand()  % 500));
        add.setBuildingSiteNumber(bldgNumber);
        add.setCity(city);
        add.setStateAbbreviation(StateAbbreviationType.fromValue(state));
        add.setPostalCode(postalCode);
        add.setNameOfCounty(county);
        add.setCountyFIPSCode("    ");
        add.setCountryCode(CountryCodeType.US);
        add.setLatitude(String.valueOf(getRand() % 90));
        add.setLongitude(String.valueOf(getRand() % 180));
        add.setBeginDate(yearAgo);
        add.setEndDate(yearHence);
        add.setAddressType(AddressType.HOME);
        return add;
    }

    public static Address generateLowFi() {
        Address add = new Address();

        add.setStreetNumberName("streetNumberName");
        add.setApartmentRoomSuiteNumber("7C");
        add.setBuildingSiteNumber("BuildingSiteNumber");
        add.setCity("City");
        add.setStateAbbreviation(StateAbbreviationType.AK);
        add.setPostalCode("12345");
        add.setNameOfCounty("County");
        add.setCountyFIPSCode("FIPS");
        add.setCountryCode(CountryCodeType.US);
        add.setLatitude("latitiude");
        add.setLongitude("longitude");
        add.setBeginDate(RIGHT_NOW);
        add.setEndDate(RIGHT_NOW);
        add.setAddressType(AddressType.HOME);
        return add;
    }

    public static void main(String[] args) throws Exception
    {
        AddressGenerator nyAddressFactory = new AddressGenerator(StateAbbreviationType.NY);
        AddressGenerator njAddressFactory = new AddressGenerator(StateAbbreviationType.NJ);
        List<AddressGenerator> factoryList = new ArrayList<AddressGenerator>();
        factoryList.add(nyAddressFactory);
        factoryList.add(njAddressFactory);

        for(AddressGenerator addressFactory:factoryList)
            for(String addressId: "1 2 3".split(" "))
            {
                Address address = addressFactory.getRandomAddress();
                String addressString = "\n\n" + addressId + ".\n" +
                        "StreetNumberName : " + address.getStreetNumberName() + ",\n" +
                        "ApartmentRoomSuiteNumber : " + address.getApartmentRoomSuiteNumber() + ",\n" +
                        "BuildingSiteNumber : " + address.getBuildingSiteNumber() + ",\n" +
                        "City : " + address.getCity() + ",\n" +
                        "StateAbbreviation : " + address.getStateAbbreviation() + ",\n" +
                        "PostalCode : " + address.getPostalCode() + ",\n" +
                        "NameOfCounty : " + address.getNameOfCounty() + ",\n" +
                        "CountyFIPSCode : " + address.getCountyFIPSCode() + ",\n" +
                        "CountryCode : " + address.getCountryCode() + ",\n" +
                        "Latitude : " + address.getLatitude() + ",\n" +
                        "Longitude : " + address.getLongitude() + ",\n" +
                        "BeginDate : " + address.getBeginDate() + ",\n" +
                        "EndDate : " + address.getEndDate() + ",\n" +
                        "AddressType : " + address.getAddressType();
                log.info(addressString);
                System.out.println(addressString);
            }
    }
}
