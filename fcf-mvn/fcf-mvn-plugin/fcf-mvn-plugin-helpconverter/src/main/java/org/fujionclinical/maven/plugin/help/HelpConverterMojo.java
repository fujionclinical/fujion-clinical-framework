/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.maven.plugin.help;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.fujionclinical.maven.plugin.core.BaseMojo;
import org.fujionclinical.maven.plugin.help.chm.ChmSourceLoader;
import org.fujionclinical.maven.plugin.iterator.ZipIterator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Goal which prepares a Help module in native format for repackaging into a FCF-compliant help
 * module.
 */
@Mojo(name = "prepare")
@Execute(goal = "prepare", phase = LifecyclePhase.PROCESS_SOURCES)
public class HelpConverterMojo extends BaseMojo {
    
    /**
     * Base folder.
     */
    @Parameter(property = "basedir/src/main/help/", required = true)
    private String baseDirectory;
    
    /**
     * Help module base path
     */
    @Parameter(property = "moduleBase", defaultValue = "org/fujionclinical/help/content/", required = true)
    private String moduleBase;
    
    /**
     * Module id
     */
    @Parameter(property = "maven.fujionclinical.help.moduleId")
    private String moduleId;
    
    /**
     * Module name
     */
    @Parameter(property = "maven.fujionclinical.help.moduleName")
    private String moduleName;
    
    /**
     * Help set format specifier.
     */
    @Parameter(property = "maven.fujionclinical.help.moduleFormat")
    private String moduleFormat;
    
    /**
     * Help set locale.
     */
    @Parameter(property = "maven.fujionclinical.help.moduleLocale")
    private String moduleLocale;
    
    /**
     * Source file name (Help jar file)
     */
    @Parameter(property = "maven.fujionclinical.help.moduleSource")
    private String moduleSource;
    
    /**
     * If true, failure to specify an existing help source jar will not result in build failure.
     */
    @Parameter(property = "maven.fujionclinical.help.ignoreMissingSource", defaultValue = "false", required = false)
    private boolean ignoreMissingSource;
    
    /**
     * Additional archive loader classes.
     */
    @Parameter(property = "maven.fujionclinical.help.archiveLoaders")
    private List<String> archiveLoaders;
    
    // Maps help format specifier to the associated source archive loader.
    private final Map<String, SourceLoader> sourceLoaders = new HashMap<>();
    
    public String getModuleBase() {
        return moduleBase;
    }
    
    public String getModuleName() {
        return moduleName;
    }
    
    public String getModuleId() {
        return moduleId;
    }
    
    public String getModuleLocale() {
        return moduleLocale;
    }
    
    /**
     * Main execution entry point for plug-in.
     */
    @Override
    public void execute() throws MojoExecutionException {
        
        if (StringUtils.isEmpty(moduleSource) && ignoreMissingSource) {
            getLog().info("No help module source specified.");
            return;
        }
        
        init("help", moduleBase);
        registerLoader(new SourceLoader("javahelp", "*.hs", ZipIterator.class));
        registerLoader(new SourceLoader("ohj", "*.hs", ZipIterator.class));
        registerLoader(new ChmSourceLoader());
        registerExternalLoaders();
        SourceLoader loader = sourceLoaders.get(moduleFormat);
        
        if (loader == null) {
            throw new MojoExecutionException("No source loader found for format " + moduleFormat);
        }
        
        try {
            String sourceFilename = FileUtils.normalize(baseDirectory + "/" + moduleSource);
            HelpProcessor processor = new HelpProcessor(this, sourceFilename, loader);
            processor.transform();
            addConfigEntry("help", moduleId, processor.getHelpSetFile(), moduleName, getModuleVersion(), moduleFormat,
                moduleLocale);
            assembleArchive();
        } catch (Exception e) {
            throw new MojoExecutionException("Unexpected error.", e);
        }
    }
    
    /**
     * Adds any additional source loaders specified in configuration.
     * 
     * @throws MojoExecutionException Error registering external loader.
     */
    private void registerExternalLoaders() throws MojoExecutionException {
        if (archiveLoaders != null) {
            for (String entry : archiveLoaders) {
                try {
                    SourceLoader loader = (SourceLoader) Class.forName(entry).newInstance();
                    registerLoader(loader);
                } catch (Exception e) {
                    throw new MojoExecutionException("Error registering archive loader for class: " + entry, e);
                }
            }
        }
    }
    
    private void registerLoader(SourceLoader loader) {
        sourceLoaders.put(loader.getFormatSpecifier(), loader);
    }
    
}
