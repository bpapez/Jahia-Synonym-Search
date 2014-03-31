/**
 * This file is part of Jahia, next-generation open source CMS:
 * Jahia's next-generation, open source CMS stems from a widely acknowledged vision
 * of enterprise application convergence - web, search, document, social and portal -
 * unified by the simplicity of web content management.
 *
 * For more information, please visit http://www.jahia.com.
 *
 * Copyright (C) 2002-2011 Jahia Solutions Group SA. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL (or any later version), you may redistribute this Program in connection
 * with Free/Libre and Open Source Software ("FLOSS") applications as described
 * in Jahia's FLOSS exception. You should have received a copy of the text
 * describing the FLOSS exception, and it is also available here:
 * http://www.jahia.com/license
 *
 * Commercial and Supported Versions of the program (dual licensing):
 * alternatively, commercial and supported versions of the program may be used
 * in accordance with the terms and conditions contained in a separate
 * written agreement between you and Jahia Solutions Group SA.
 *
 * If you are unsure which license is appropriate for your use,
 * please contact the sales department at sales@jahia.com.
 */
package org.jahia.services.search.synonym;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


import org.apache.solr.core.SolrResourceLoader;
import org.jahia.bin.listeners.JahiaContextLoaderListener;
import org.jahia.registries.ServicesRegistry;
import org.jahia.utils.i18n.JahiaResourceBundle;
import org.jahia.utils.i18n.JahiaTemplatesRBLoader;
import org.slf4j.Logger;

/**
 * This class is extending Solr's SynonymFilterFactory in order to make the factory configurable through Jahia's Spring configuration.
 * 
 * @author Benjamin Papez
 * 
 */
public class SynonymFilterFactory extends org.apache.solr.analysis.SynonymFilterFactory {
    private transient static Logger logger = org.slf4j.LoggerFactory.getLogger(SynonymFilterFactory.class);
    /**
     * It's a Singleton *
     */
    private static SynonymFilterFactory theObject = null;
    
    private final static String TEMPLATE_PCKG_NAME = "Jahia Synonym Search";

    public SynonymFilterFactory() {
        super();
    }

    /**
     * Return the unique instance of this class.
     * 
     * @return the unique instance of this class
     */
    public static SynonymFilterFactory getInstance() {
        if (theObject == null) {
            theObject = new SynonymFilterFactory();
            theObject.init();
        }
        return theObject;
    }

    public void init() {
        Properties args = new Properties();
        try {
            args.load(JahiaTemplatesRBLoader.getInstance(Thread.currentThread().getContextClassLoader(), TEMPLATE_PCKG_NAME)
                    .getResourceAsStream(toResourceName(
                            ServicesRegistry.getInstance().getJahiaTemplateManagerService().getTemplatePackage(TEMPLATE_PCKG_NAME)
                                    .getResourceBundleHierarchy().get(0), "properties")));

            super.init(new HashMap(args));

            String instanceDir = JahiaContextLoaderListener.getServletContext().getRealPath("/WEB-INF");
            inform(new SolrResourceLoader(instanceDir));
        } catch (IOException e) {
            logger.error("Error initializing the SynonymFilterFactory. Synonym indexing will not work!!!");
        }
    }
    
    /**
     * Converts the given <code>bundleName</code> to the form required
     * by the {@link ClassLoader#getResource ClassLoader.getResource}
     * method by replacing all occurrences of <code>'.'</code> in
     * <code>bundleName</code> with <code>'/'</code> and appending a
     * <code>'.'</code> and the given file <code>suffix</code>. For
     * example, if <code>bundleName</code> is
     * <code>"foo.bar.MyResources_ja_JP"</code> and <code>suffix</code>
     * is <code>"properties"</code>, then
     * <code>"foo/bar/MyResources_ja_JP.properties"</code> is returned.
     *
     * @param bundleName
     *        the bundle name
     * @param suffix
     *        the file type suffix
     * @return the converted resource name
     * @exception NullPointerException
     *         if <code>bundleName</code> or <code>suffix</code>
     *         is <code>null</code>
     */
    public final String toResourceName(String bundleName, String suffix) {
        StringBuilder sb = new StringBuilder(bundleName.length() + 1 + suffix.length());
        sb.append(bundleName.replace('.', '/')).append('.').append(suffix);
        return sb.toString();
    }
}
