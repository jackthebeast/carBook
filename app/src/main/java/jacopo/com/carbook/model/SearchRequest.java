package jacopo.com.carbook.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jacop on 19/11/2017.
 */

public class SearchRequest implements Serializable{

    public Stop[] stops;
    public Date start_at;

    public void setStops(Stop... stops){
        this.stops = stops;
    }
}
