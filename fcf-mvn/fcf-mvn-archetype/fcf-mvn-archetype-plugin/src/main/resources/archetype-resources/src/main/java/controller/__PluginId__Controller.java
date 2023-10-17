package $

public class ${PluginId}Controller extends PluginController {

    private static final Log log = LogFactory.getLog(${PluginId}Controller.class);

    /**
     * @see org.fujionclinical.ui.controller.FrameworkController${symbol_pound}afterInitialized(org.fujion.component.BaseComponent)
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        super.afterInitialized(comp);
        log.trace("Controller initialized");
    }
    
    /**
     * @see org.fujionclinical.shell.plugins.IPluginEvent${symbol_pound}onLoad(org.fujionclinical.shell.elements.ElementPlugin)
     */
    @Override
    public void onLoad(ElementPlugin plugin) {
        super.onLoad(plugin);
    }
    
    /**
     * @see org.fujionclinical.shell.plugins.IPluginEvent${symbol_pound}onUnload()
     */
    @Override
    public void onUnload() {
        super.onUnload();
    }
    
    /**
     * @see org.fujionclinical.shell.plugins.IPluginEvent${symbol_pound}onActivate()
     */
    @Override
    public void onActivate() {
        super.onActivate();
    }
    
    /**
     * @see org.fujionclinical.shell.plugins.IPluginEvent${symbol_pound}onInactivate()
     */
    @Override
    public void onInactivate() {
        super.onInactivate();
    }

}
