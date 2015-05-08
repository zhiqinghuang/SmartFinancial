package com.manydesigns.portofino.modules;

public interface Module {
    String getModuleVersion();

    int getMigrationVersion();

    double getPriority();

    String getId(); // un nome tecnico senza spazi e caratteri strani (in genere coincidente con l'artifact id).

    String getName();

    int install();

    void init();

    void start();

    void stop();

    void destroy();

    ModuleStatus getStatus();

}