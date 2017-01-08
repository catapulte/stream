package net.lolcat.workflow.mapper;

import net.lolcat.workflow.DecimalCoord;
import net.lolcat.workflow.model.CatData;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CatMapper implements MapFunction<String, CatData> {
    private FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy'@'HH:mm:ss.SSS");

    @Override
    public CatData map(String rawData) throws Exception {
        String regexp = "#(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)#";

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(rawData);

        if (matcher.matches()) {
            CatData cat = new CatData();

            String id = matcher.group(1);
            cat.setId(id);

            String lat = matcher.group(2);
            String lng = matcher.group(3);
            DecimalCoord decimalCoord = new DecimalCoord(lat, lng);
            cat.setPosition(decimalCoord.getDecimalCoord());

            String angle = matcher.group(4);
            cat.setAngle(Double.parseDouble(angle));

            String alt = matcher.group(5);
            cat.setAltitude(Double.parseDouble(alt));


            String satellitesNb = matcher.group(6);
            cat.setSatelliteNb(Integer.parseInt(satellitesNb));

            String ts = matcher.group(7);
            Date parse = dateFormat.parse(ts);
            cat.setTimestamp(parse.getTime());

            String temperature = matcher.group(8);
            cat.setTemperature(Double.parseDouble(temperature));

            String humidity = matcher.group(9);
            cat.setHumidity(Double.parseDouble(humidity));

            double vbat = Double.valueOf(matcher.group(10));
            cat.setVbat(vbat);

            return cat;

        }
        throw new Exception("not valid : " + rawData);
    }
}
