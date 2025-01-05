package utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;

import java.io.File;
import java.net.InetAddress;

public class ipCountry {
    public ipCountry(){

    }
    public String getNameCountry(String ip){
        String CountryName = "";
        try{
            File database = new File("src/main/resources/GeoLite2-Country.mmdb");
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = reader.country(ipAddress);

            // Lấy mã quốc gia và tên quốc gia
            CountryName = response.getCountry().getName();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return CountryName;
    }
}
