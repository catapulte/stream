package net.lolcat.workflow.mapper;

import net.lolcat.workflow.DecimalCoord;
import net.lolcat.workflow.model.CatData;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CatMapper implements MapFunction<String, CatData> {
    private FastDateFormat dateFormat = FastDateFormat.getInstance("d/M/yyyy'@'HH:mm:ss");

    @Override
    public CatData map(String rawData) throws Exception {
        // 1|4806.9043N|140.4209W|6/6/2017@21:5:3|20|51|4.25
        String regexp = "(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)";

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

            String ts = matcher.group(4);
            Date parse = dateFormat.parse(ts);
            cat.setTimestamp(parse.getTime());

            String temperature = matcher.group(5);
            cat.setTemperature(Double.parseDouble(temperature));

            String humidity = matcher.group(6);
            cat.setHumidity(Double.parseDouble(humidity));

            double vbat = Double.valueOf(matcher.group(7));
            cat.setVbat(vbat);

            return cat;

        }
        throw new Exception("not valid : " + rawData);
    }
}
