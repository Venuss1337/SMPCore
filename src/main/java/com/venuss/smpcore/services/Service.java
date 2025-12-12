package com.venuss.smpcore.services;

public abstract class Service {

    public String serviceName;
    public String status;

    public Service(String serviceName) {
        this.serviceName = serviceName;
        this.status = "OFFLINE";
    }

    public void onStart() {

    }

    public final void start() {
        this.status = "RUNNING";
        this.onStart();
    }

    public void onStop() {

    }

    public final void stop() {
        this.status = "STOPPED";
        this.onStop();
    }

    public void onRestart() {

    }

    public final void restart() {
        this.status = "RESTARTING";
        this.onRestart();
    }
}
