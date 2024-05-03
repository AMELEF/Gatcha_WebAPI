package fr.imt.gatcha_webapi.Beans.Attacks;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Ratio {
    String stat;
    float percent;
}
