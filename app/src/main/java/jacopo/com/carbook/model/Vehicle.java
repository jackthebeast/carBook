package jacopo.com.carbook.model;

import java.io.Serializable;

/**
 * Created by jacop on 19/11/2017.
 */

public class Vehicle implements Serializable {
    public VehicleType vehicle_type;
    public Integer total_price;
    public String currency;
    public String currency_symbol;
    public String price_formatted;
}
