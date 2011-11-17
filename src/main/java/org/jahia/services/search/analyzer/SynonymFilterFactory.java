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
package org.jahia.services.search.analyzer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.solr.core.SolrResourceLoader;
import org.springframework.web.context.ServletContextAware;

/**
 * This class is extending Solr's SynonymFilterFactory in order to make the factory
 * configurable through Jahia's Spring configuration.
 * 
 * @author Benjamin Papez
 *
 */
public class SynonymFilterFactory extends org.apache.solr.analysis.SynonymFilterFactory implements ServletContextAware {
    private String synonyms;
    private Boolean ignoreCase;
    private Boolean expand;
    private String tokenizerFactory;
    private ServletContext servletContext;

    public SynonymFilterFactory() {
        super();
    }

    public void init() {
        Map<String, String> args = new HashMap<String, String>();
        args.put("synonyms", getSynonyms());
        args.put("ignoreCase", getIgnoreCase() != null ? getIgnoreCase().toString() : null);
        args.put("expand", getExpand() != null ? getExpand().toString() : null);
        args.put("tokenizerFactory", getTokenizerFactory());

        super.init(args);

        inform(new SolrResourceLoader(servletContext.getRealPath("/WEB-INF")));
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public Boolean getIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }

    public String getTokenizerFactory() {
        return tokenizerFactory;
    }

    public void setTokenizerFactory(String tokenizerFactory) {
        this.tokenizerFactory = tokenizerFactory;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
