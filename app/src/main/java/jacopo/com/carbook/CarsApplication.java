package jacopo.com.carbook;

import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import jacopo.com.carbook.data.CarsFactory;
import jacopo.com.carbook.data.CarsService;


/**
 * Created by jacop on 19/10/2017.
 */

public class CarsApplication extends Application {

    private static CarsApplication instance;
    private Scheduler scheduler;
    private CarsService carsService;

    static{
        instance = new CarsApplication();
    }

    public static CarsApplication getApp(){
        return instance;
    }


    public Scheduler subscribeScheduler(){
        if(scheduler == null)
            scheduler = Schedulers.io();
        return scheduler;
    }


}
