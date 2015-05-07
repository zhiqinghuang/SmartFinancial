package com.manydesigns.portofino.servlets;

public class DemoTTListener extends PortofinoListener {
//    @Override
//    protected void discoverModules(ModuleRegistry moduleRegistry, ClassLoader classLoader) {
//        logger.info("Registering modules");
//        try {
//            Module[] modules = new Module[] {
//                    new BaseModule(), new PageactionsModule(), new PageactionsadminModule(),
//                    new DatabaseModule(), new MysqlModule(), new GooglecloudsqlModule(),
//                    new CrudModule(), new TextModule(), new ThemeModule(),
//                    (Module) classLoader.loadClass("com.manydesigns.portofino.modules.DemoTTModule").newInstance()
//            };
//            for(Module module : modules) {
//                logger.debug("Adding module " + module);
//                moduleRegistry.getModules().add(module);
//            }
//        } catch (Exception e) {
//            logger.error("Could not register modules", e);
//            super.discoverModules(moduleRegistry, classLoader);
//        }
//    }
}