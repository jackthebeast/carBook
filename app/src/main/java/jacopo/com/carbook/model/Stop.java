package jacopo.com.carbook.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by jacop on 19/11/2017.
 */

public class Stop implements Serializable {

    public Double[] loc;
    public String name;
    public String addr;
    public String num;
    public String city;
    public String country;
    public String instr;
    public Contact contact;

    public void setLoc(LatLng position){
        loc = new Double[2];
        loc[0] = position.latitude;
        loc[1] = position.longitude;
    }
}
