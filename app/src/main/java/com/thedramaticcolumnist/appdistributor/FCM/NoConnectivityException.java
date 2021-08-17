package com.thedramaticcolumnist.appdistributor.FCM;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}

